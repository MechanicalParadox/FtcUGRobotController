package org.firstinspires.ftc.teamcode.odometry;
//import static java.lang.Math;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class InitialPosCalcs implements Runnable{
//All values in cm
//Declare values(W is constant in cm)
    private DcMotor verticalEncoderLeft, verticalEncoderRight, horizontalEncoder;
    public double verticalEncoderWidth = 13.18 * 1440/(Math.PI * 2.83); //CHANGE WIDTH TO INCHES
    public /*static*/ double dThetaV = 0, dThetaH = 0;
    private Pos2d posEstimate;
    private List<Double> lastWheelPositions;
    private double x = 0.0, y = 0.0, theta = 0.0, lastTheta = 0.0; //X EQUALS NOTHING
    double verticalRightEncoderWheelPosition = 0, verticalLeftEncoderWheelPosition = 0, normalEncoderWheelPosition = 0,  changeInRobotOrientation = 0;
    private double robotGlobalXCoordinatePosition = 0, robotGlobalYCoordinatePosition = 0, robotOrientationRadians = 0;
    private double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0;

    private boolean isRunning = true;
    //Sleep time interval (milliseconds) for the position update thread
    private int sleepTime;
    //Telemetry
    public Telemetry telemetry;

    private double dX;
//function to find positions
    public InitialPosCalcs(DcMotor verticalEncoderLeft, DcMotor verticalEncoderRight, DcMotor horizontalEncoder) {
        this.verticalEncoderLeft = verticalEncoderLeft;
        this.verticalEncoderRight = verticalEncoderRight;
        this.horizontalEncoder = horizontalEncoder;

        verticalEncoderWidth = verticalEncoderWidth;
    }

    public void InitialPosCalcsUpdate() {
        List<Double> wheelPositons = getWheelPositions();

        if(lastWheelPositions != null && !lastWheelPositions.isEmpty()){
            double dL = wheelPositons.get(0) - lastWheelPositions.get(0);
            double dR = wheelPositons.get(1) - lastWheelPositions.get(1);
            double dC = wheelPositons.get(2) - lastWheelPositions.get(2);

            double dS = (dL + dR)/2.0;
            lastTheta = theta;
            dThetaV = (dL + dR)/ verticalEncoderWidth;

            double avgTheta = (theta + dThetaV) / 2.0;


            //find x by parts
            double xSides = 0;
            double xCenter = 0;
            if(dThetaV != 0){
                xSides = (1-Math.cos(dThetaV))*(dR+dL)/(2*dThetaV);
                xCenter = Math.sin(dThetaV)*dC/dThetaV;
            }
            if(dThetaH != 0){
                xCenter = Math.sin(dThetaH)*dC/dThetaH;
            }
            double dX = xSides + xCenter; //May need to revise depending on c encoder variation

            /*double dX = dS * Math.cos(avgTheta) + dC * Math.sin(avgTheta);
            double dY = dS * Math.sin(avgTheta) - dC * Math.cos(avgTheta);*/

            //find y by parts
            double ySides = 0;
            double yCenter = 0;
            if(dThetaV != 0) {
                ySides = Math.sin(dThetaV) * (dR + dL) / (2 * dThetaV);
                yCenter = (1-Math.cos(dThetaV))*dC/dThetaV;
            }
            if(dThetaH != 0){ //testing making them both the same theta
                yCenter = (1-Math.cos(dThetaH))*dC/dThetaH;
            }

            double dY = ySides + yCenter;

            //Calculate new vertical distance, horizontal distance, and angle
            x += dX;
            x *= Math.cos(theta);
            y += dY;
            y *= Math.sin(theta);
            theta += dThetaV;

            //posEstimate = new Pos2d(x, y, theta);
            //lastWheelPositions = getWheelPositions();
        }
        //Update change in wheel positions regardless
        lastWheelPositions = wheelPositons;
    }

    public double getDeltaX(){return dX;}


    /*public Pos2d getPosEstimate(){
        InitialPosCalcsUpdate();
        double degrees = /*3.1415 / 2 +/ posEstimate.getHeading() * (180 / Math.PI);
        Pos2d posEstimateCorrected = new Pos2d(
                posEstimate.getX(),
                posEstimate.getY(),
                degrees
        );


        return posEstimateCorrected;
    } */

    public List<Double> getWheelPositions(){
        verticalLeftEncoderWheelPosition = verticalEncoderLeft.getCurrentPosition();
        verticalRightEncoderWheelPosition = verticalEncoderRight.getCurrentPosition();
        normalEncoderWheelPosition = horizontalEncoder.getCurrentPosition();
        List<Double> WheelPositions = new ArrayList(){{
            add(verticalLeftEncoderWheelPosition);
            add(verticalRightEncoderWheelPosition);
            add(normalEncoderWheelPosition);
        }};
        return  WheelPositions;
    }

    // Return global x coordinate
    public double returnXCoordinate(){
        return x;
    }

    // Return global y coordinate
    public double returnYCoordinate(){
        return y;
    }

    // Return global theta orientation in degrees
    public double returnTheta(){
        return Math.toDegrees(theta)/* % 360*/;
    }

    //stops update if necessary
    public void stop(){
        isRunning = false;
    }


    @Override
    public void run() {
        while(isRunning) {
            InitialPosCalcsUpdate();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
