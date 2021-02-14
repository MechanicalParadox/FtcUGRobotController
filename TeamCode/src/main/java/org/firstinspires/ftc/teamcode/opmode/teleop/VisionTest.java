package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.vision.LeftPosFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp(name="VisionTest")
public class VisionTest extends LinearOpMode{
    OpenCvCamera webCam;

    private LeftPosFrameGrabber leftPosFrameGrabber;

    private enum POSITION {
        A,
        B,
        C,
    }

    POSITION position;



    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webCam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        //Specify Image processing pipeline
        leftPosFrameGrabber = new LeftPosFrameGrabber();
        webCam.setPipeline(leftPosFrameGrabber);
        // start the vision system
        webCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
        });

        while (!isStarted()) { // 1 stack W: 96. H: 42.  4 stack: W: 105.  H: 84

            // Left Guide
            if (gamepad1.dpad_right == true) {
                leftPosFrameGrabber.leftGuide += 0.001;
            } else if (gamepad1.dpad_left == true) {
                leftPosFrameGrabber.leftGuide -= 0.001;
            }

            // Mask
            if (gamepad1.dpad_down == true) {
                leftPosFrameGrabber.mask += 0.001;
            } else if (gamepad1.dpad_up == true) {
                leftPosFrameGrabber.mask -= 0.001;
            }

            // Threshold
            if (gamepad2.x == true) {
                leftPosFrameGrabber.threshold += 0.01; // was 0.001
            } else if (gamepad2.b == true) {
                leftPosFrameGrabber.threshold -= 0.01;
            }

            telemetry.addData("Position", leftPosFrameGrabber.position);
            telemetry.addData("Position", leftPosFrameGrabber.position);
            telemetry.addData("Threshold", leftPosFrameGrabber.threshold);
            telemetry.addData("Rect width", leftPosFrameGrabber.rectWidth);
            telemetry.addData("Rect height", leftPosFrameGrabber.rectHeight);

            telemetry.update();
        }

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Position", position);
            telemetry.addData("Threshold", leftPosFrameGrabber.threshold);
        }
    }
}