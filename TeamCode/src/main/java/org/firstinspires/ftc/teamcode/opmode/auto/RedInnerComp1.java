package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.FullRobot;
import org.firstinspires.ftc.teamcode.hardware.vision.LeftPosFrameGrabber;
import org.firstinspires.ftc.teamcode.opmode.teleop.VisionTest;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "RedComp1", group = "Tests")
public class RedInnerComp1 extends LinearOpMode {
    //Vision Variables
    OpenCvCamera webCam;
    private LeftPosFrameGrabber leftPosFrameGrabber;
    /*private enum POSITION {
        A,
        B,
        C,
    }
    POSITION position;*/
    //Full Robot
    private FullRobot robot;
    public ElapsedTime elapsedTime;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new FullRobot(this, hardwareMap, telemetry);
        elapsedTime = new ElapsedTime();
        robot.mecanumDrive.reset();

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

            telemetry.update();//Stop Breaking
        }

        waitForStart();

        webCam.stopStreaming();
        robot.mecanumDrive.reset();
        elapsedTime.reset();
        //delay(1);

        //move left and hit 1st power shot
        while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -6) {//22 too far
            if (robot.mecanumDrive.getCenterPosition() < -4) {
                driveSideways(0.25);
            } else {
                driveSideways(0.5);
            }
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        driveForward(0);
        delay(0.5);

        //move forward to prepare to score power shots
        while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 48 && robot.mecanumDrive.getRightPosition() < 48) {
            if (robot.mecanumDrive.getLeftPosition() > 44) {
                driveForward(0.1);
            } else if (robot.mecanumDrive.getLeftPosition() > 30) {
                driveForward(0.25);
            } else if (robot.mecanumDrive.getLeftPosition() > 20) {
                driveForward(0.5);
            } else {
                driveForward(0.75);

            }
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        driveForward(0);
        delay(0.5); // Cut in half once tested
        //delay(2);

        robot.launchpad.shoot(0.9);
        delay(1);
        robot.launchpad.setConveyor(1.0);
        delay(0.5);
        //robot.launchpad.setConveyor(0.0);

        //Second Power Shot
        while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -32) {//35 too far
            driveSideways(0.43);

            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }

            driveSideways(0);
            delay(1);

            //robot.launchpad.setConveyor(1.0);
            delay(2.5);
            robot.launchpad.setConveyor(0.0);

            delay(1);

            //If Pos C, try and intake and shoot in high goal
        if(leftPosFrameGrabber.position != "A"){
            //move left and hit 1st power shot
            while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() < 12) {//22 too far
                if (robot.mecanumDrive.getCenterPosition() < 6) {
                    driveSideways(-0.5);
                } else {
                    driveSideways(-0.75);
                }
                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.5);

            robot.intake.intakeBack(1);
            delay(0.2);

            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > 36 && robot.mecanumDrive.getRightPosition() > 36) {
                if (robot.mecanumDrive.getLeftPosition() > 40) {
                    driveForward(-0.25);
                } else if (robot.mecanumDrive.getLeftPosition() < 44) {
                    driveForward(-0.5);
                } else {
                    driveForward(-0.75);

                }
                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.2);

            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 60 && robot.mecanumDrive.getRightPosition() < 60) {
                if (robot.mecanumDrive.getLeftPosition() > 59) {
                    driveForward(0.1);
                } else if (robot.mecanumDrive.getLeftPosition() > 58) {
                    driveForward(0.25);
                } else if (robot.mecanumDrive.getLeftPosition() > 54) {
                    driveForward(0.5);
                } else {
                    driveForward(0.75);

                }
                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            robot.intake.intakeBack(0);

            robot.launchpad.shoot(1.0);
            delay(1);
            robot.launchpad.setConveyor(1.0);
            delay(3);
        }
        //move forward to prepare to score power shots
        while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 72 && robot.mecanumDrive.getRightPosition() < 72) {
            if (robot.mecanumDrive.getLeftPosition() > 66) {
                driveForward(0.5);
            } else {
                driveForward(0.75);

            }
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        driveForward(0);

        while(opModeIsActive()){
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
            if(isStopRequested() == true)
                requestOpModeStop();
        }
    }
    public void delay(double seconds) {
        elapsedTime.reset();
        while (elapsedTime.time() < seconds && opModeIsActive()) ;
    }

    public void driveForward(double power){
        robot.mecanumDrive.frontR.setPower(-power);
        robot.mecanumDrive.backR.setPower(-power);
        robot.mecanumDrive.frontL.setPower(power);
        robot.mecanumDrive.backL.setPower(power);
    }
    public void driveSideways(double power){
        //treat left as positive
        robot.mecanumDrive.frontR.setPower(-power);
        robot.mecanumDrive.backR.setPower(power);
        robot.mecanumDrive.frontL.setPower(-power);
        robot.mecanumDrive.backL.setPower(power);
    }
}
