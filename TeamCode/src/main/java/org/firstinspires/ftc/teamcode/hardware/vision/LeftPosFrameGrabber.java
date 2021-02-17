package org.firstinspires.ftc.teamcode.hardware.vision;

import org.firstinspires.ftc.teamcode.hardware.vision.filters.Filter;
import org.firstinspires.ftc.teamcode.hardware.vision.filters.ColorFilter;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;


public class LeftPosFrameGrabber extends OpenCvPipeline {
    public Filter colorFilter = new ColorFilter();

    public double leftGuide = 320; // Need to confirm
    //public double rightGuide = 320;
    public double mask = 0; // Need to confirm
    public double mask2 = 0;
    public double threshold = 120.00; // was 82
    public String position = "";
    public double rectWidth = 0.0;
    public double rectHeight = 0.0;

    public double perfectRatio = 1;
    public double areaWeight = 0.01; // Since we're dealing with 100's of pixels
    public double minArea = 1000;
    public double ratioWeight = 100; // Since most of the time the area difference is a decimal place

    Point p1 = new Point(0, mask);
    Point p4 = new Point(960, 1280);

    private Mat workingMat = new Mat();
    private Mat displayMat = new Mat();
    private Mat yellowMask = new Mat();
    private Mat yellowMask2 = new Mat();
    private Mat hiarchy = new Mat();

    private boolean firstIteration = true;



    @Override
    public Mat processFrame(Mat rgba) {
        // Copy input mat to working/display mats

        //rgba.copyTo(displayMat);
        rgba.copyTo(workingMat);
        //rgba.release();



        if (firstIteration) {
            mask = rgba.height() / 4 * 2.5;
        }
        mask2 = rgba.width()/2*1.2;

        Imgproc.rectangle(workingMat, new Point(0, mask), new Point(rgba.width(), 0), new Scalar(255, 255, 255), -1, 8, 0);
        Imgproc.rectangle(rgba, new Point(0, mask), new Point(rgba.width(), 0), new Scalar(255, 255, 255), -1, 8, 0);
        Imgproc.rectangle(workingMat, new Point(mask2, 0), new Point(0, rgba.height()), new Scalar(255, 255, 255), -1, 8, 0);
        Imgproc.rectangle(rgba, new Point(mask2, 0), new Point(0, rgba.height()), new Scalar(255, 255, 255), -1, 8, 0);

        // Generate Masks
        colorFilter.processFrame(workingMat.clone(), yellowMask, mask, threshold);
        colorFilter.processFrame(workingMat.clone(), yellowMask2, mask2, threshold);

        // Blur and find the contours in the masks
        List<MatOfPoint> contoursYellow = new ArrayList<>();
        Imgproc.blur(yellowMask, yellowMask, new Size(4, 4));
        Imgproc.blur(yellowMask2, yellowMask2, new Size(4, 4));

        // Mask of the unneeded section
        Imgproc.line(rgba, new Point(0, mask), new Point(640, mask), new Scalar(255, 0, 0));
        Imgproc.line(rgba, new Point(mask2, 0), new Point(mask2, 640), new Scalar(255, 0, 0));
        Imgproc.findContours(yellowMask, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(rgba, contoursYellow, -1, new Scalar(230, 70, 70), 2);
        Imgproc.findContours(yellowMask2, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(rgba, contoursYellow, -1, new Scalar(230, 70, 70), 2);


        // Prepare to find best yellow (gold) results
        Rect chosenYellowRect = null;

        double chosenYellowScore = Integer.MAX_VALUE;

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for (MatOfPoint cont : contoursYellow) {
            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);

            double area = Imgproc.contourArea(cont);
            double areaDiffernce = 0.0;
            areaDiffernce = -area * areaWeight;

            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;

            Point centerPoint = new Point(x + (w / 2), y + (h / 2));

            double cubeRatio = Math.max(Math.abs(h / w), Math.abs(w / h)); // Get the ratio. We use max in case h and w get swapped, which happens when you account for rotation
            double ratioDiffernce = Math.abs(cubeRatio - perfectRatio);
            double finalDiffernce = (ratioDiffernce * ratioWeight) + (areaDiffernce * areaWeight);

            if (area > minArea) {
                chosenYellowScore = finalDiffernce;
                chosenYellowRect = rect;
            }
        }

        if (chosenYellowRect != null) {
            Imgproc.rectangle(rgba,
                    new Point(chosenYellowRect.x, chosenYellowRect.y),
                    new Point(chosenYellowRect.x + chosenYellowRect.width, chosenYellowRect.y + chosenYellowRect.height),
                    new Scalar(255, 0, 0), 2);
            Imgproc.putText(rgba,
                    "Gold: " + String.format("%.2f X=%.2f", chosenYellowScore, (double) chosenYellowRect.x),
                    new Point(chosenYellowRect.x - 5, chosenYellowRect.y - 10),
                    Imgproc.FONT_HERSHEY_PLAIN,
                    1.3,
                    new Scalar(0, 255, 255),
                    2);
            Imgproc.putText(rgba,
                    calcAlign(chosenYellowRect.x, chosenYellowRect.width, chosenYellowRect.height),
                    new Point(100, 100),
                    Imgproc.FONT_HERSHEY_PLAIN,
                    5,
                    new Scalar(0, 255, 255),
                    2);
        } else {
            Imgproc.putText(rgba,
                    calcAlignRight(),
                    new Point(100, 100),
                    Imgproc.FONT_HERSHEY_PLAIN,
                    5,
                    new Scalar(0, 255, 255),
                    2);
        }

        Imgproc.line(rgba, new Point(leftGuide, 0), new Point(leftGuide, 1280), new Scalar(255, 0, 0));

        firstIteration = false;
        return rgba;
    }
    /**
     * @return 0 if left, 1 if middle, 2 if right
     * May consider switch statement
     */
    /*
    public String calcAlign(double x) {
        if (x < leftGuide) {
            position = "LEFT";
            return "LEFT";
        } else if (x > leftGuide) {
            position = "MIDDLE";
            return "MIDDLE";
        } else /*if (x > rightGuide)/ {
            position = "RIGHT";
            return "RIGHT";
        }
    }
    */


    public String calcAlign(double x, double width, double height){
        rectWidth = width;
        rectHeight = height;
        // Because we are detecting yellow, we know that if the smallest x value is in the middle, the black skystone must be in the center
        if(rectHeight > 75){ //Still need to determine left guide
            position = "C";
            return "C";
        } else if(rectHeight > 35.0){ //Still need to determine the approximate width of two stones
            position = "B";
            return "B";
        } else{
            position = "A";
            return "A";
        }
    }


    public String calcAlignRight() {
        position = "A";
        return "A";
    }
}
