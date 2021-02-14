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

            telemetry.update();
        }

        waitForStart();

        webCam.stopStreaming();
        robot.mecanumDrive.reset();
        elapsedTime.reset();
        delay(1);

//POSITION A(starter stack of 0) ///////////////////////////////////////////////////////////////////////////////////////////////
        if(leftPosFrameGrabber.position == "A") {
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

            //move left and hit 1st power shot
            while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -6 && robot.mecanumDrive.getRightPosition() > -6) {
                if (robot.mecanumDrive.getCenterPosition() < 3) {
                    driveForward(0.25);
                } else {
                    driveForward(0.5);
                }
                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.5);

            robot.launchpad.shoot(0.9);
            delay(0.25);
            robot.launchpad.setConveyor(1);
            delay(0.25);
            robot.launchpad.setConveyor(0);

            //Second Power Shot
            while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -10) {
                driveSideways(0.25);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }

            driveSideways(0);
            delay(1);
            //SHOOT RINGS PLACEHOLDER

            delay(1);

            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 60 && robot.mecanumDrive.getRightPosition() < 60) {
                if (robot.mecanumDrive.getLeftPosition() > 55) {
                    driveForward(0.1);
                } else if (robot.mecanumDrive.getLeftPosition() > 45) {
                    driveForward(0.25);
                } else if (robot.mecanumDrive.getLeftPosition() > 30) {
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

            //Third Power Shot
        }
//POSITION B(starter stack of 1) ///////////////////////////////////////////////////////////////////////////////////////////////
        //move forward to place wobble grabber
/*
        while(opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 50 && robot.mecanumDrive.getRightPosition() < 50){
            if(robot.mecanumDrive.getLeftPosition() > 42){
                driveForward(0.1);
            } else if (robot.mecanumDrive.getLeftPosition() > 35) {
                driveForward(0.25);
            } else if (robot.mecanumDrive.getLeftPosition() > 25){
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
        delay(0.25);

        while(opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -12){
            if (robot.mecanumDrive.getCenterPosition() > 6){
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
        delay(2);

        //SHOOT RINGS PLACEHOLDER

        while(opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -26){
            if (robot.mecanumDrive.getCenterPosition() < -18){
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
        delay(2);

        while(opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 96 && robot.mecanumDrive.getRightPosition() < 96){
            if(robot.mecanumDrive.getLeftPosition() > 92){
                driveForward(0.1);
            } else if (robot.mecanumDrive.getLeftPosition() > 86) {
                driveForward(0.25);
            } else if (robot.mecanumDrive.getLeftPosition() > 74){
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
        delay(0.25);
        //RELEASE WOBBLER PLACEHOLDER
        delay(2);

        //Drive back and park
        while(opModeIsActive() && robot.mecanumDrive.getLeftPosition() > 60 && robot.mecanumDrive.getRightPosition() > 60){
            if(robot.mecanumDrive.getLeftPosition() < 63.5){
                driveForward(-0.1);
            } else if (robot.mecanumDrive.getLeftPosition() < 67.5) {
                driveForward(-0.25);
            } else if (robot.mecanumDrive.getLeftPosition() < 75){
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
        delay(2);

      */

//POSITION C(starter stack of 4) ///////////////////////////////////////////////////////////////////////////////////////////////
        //move forward to place wobble grabber
        while(opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 120 && robot.mecanumDrive.getRightPosition() < 120){
            if(robot.mecanumDrive.getLeftPosition() > 110){
                driveForward(0.1);
            } else if (robot.mecanumDrive.getLeftPosition() > 90) {
                driveForward(0.25);
            } else if (robot.mecanumDrive.getLeftPosition() > 75){
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
        delay(0.25);
        //RELEASE WOBBLER PLACEHOLDER
        delay(2);

        //go back and score rings
        while(opModeIsActive() && robot.mecanumDrive.getLeftPosition() > 45 && robot.mecanumDrive.getRightPosition() > 45){
            if(robot.mecanumDrive.getLeftPosition() < 47.5){
                driveForward(-0.1);
            } else if (robot.mecanumDrive.getLeftPosition() < 52.5) {
                driveForward(-0.25);
            } else if (robot.mecanumDrive.getLeftPosition() < 57.5){
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
        delay(2);
        while(opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -20){
            if (robot.mecanumDrive.getCenterPosition() < -10){
                driveSideways(0.25);
            } else {
                driveSideways(0.5);

            }
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }

        driveSideways(0);
        delay(1);
        //SHOOT RINGS PLACEHOLDER

        delay(1);

        while(opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 60 && robot.mecanumDrive.getRightPosition() < 60){
            if(robot.mecanumDrive.getLeftPosition() > 55){
                driveForward(0.1);
            } else if (robot.mecanumDrive.getLeftPosition() > 45) {
                driveForward(0.25);
            } else if (robot.mecanumDrive.getLeftPosition() > 30){
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
