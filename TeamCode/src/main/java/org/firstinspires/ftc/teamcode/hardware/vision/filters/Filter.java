package org.firstinspires.ftc.teamcode.hardware.vision.filters;

import org.opencv.core.Mat;

public abstract class Filter {
    public abstract void processFrame(Mat input, Mat mask, double maskVal, double threshold);
}
