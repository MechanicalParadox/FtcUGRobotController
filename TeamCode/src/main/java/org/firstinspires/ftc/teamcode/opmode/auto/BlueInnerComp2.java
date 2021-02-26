package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.FullRobot;
import org.firstinspires.ftc.teamcode.hardware.vision.RightPosFrameGrabber;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "BlueComp1", group = "Tests")
public class BlueInnerComp2 extends LinearOpMode {
    //Vision Variables
    OpenCvCamera webCam;
    private RightPosFrameGrabber RightPosFrameGrabber;
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
        RightPosFrameGrabber = new RightPosFrameGrabber();
        webCam.setPipeline(RightPosFrameGrabber);
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
                RightPosFrameGrabber.leftGuide += 0.001;
            } else if (gamepad1.dpad_left == true) {
                RightPosFrameGrabber.leftGuide -= 0.001;
            }

            // Mask
            if (gamepad1.dpad_down == true) {
                RightPosFrameGrabber.mask += 0.001;
            } else if (gamepad1.dpad_up == true) {
                RightPosFrameGrabber.mask -= 0.001;
            }

            // Threshold
            if (gamepad2.x == true) {
                RightPosFrameGrabber.threshold += 0.001; // was 0.001
            } else if (gamepad2.b == true) {
                RightPosFrameGrabber.threshold -= 0.001;
            }

            robot.wobbleGoal.WobbleGrab(false);

            telemetry.addData("Position", RightPosFrameGrabber.position);
            telemetry.addData("Position", RightPosFrameGrabber.position);
            telemetry.addData("Threshold", RightPosFrameGrabber.threshold);
            telemetry.addData("Rect width", RightPosFrameGrabber.rectWidth);
            telemetry.addData("Rect height", RightPosFrameGrabber.rectHeight);

            telemetry.update();//Stop Breaking
        }

        waitForStart();

        webCam.stopStreaming();
        robot.mecanumDrive.reset();
        elapsedTime.reset();
        delay(1);

        //move left and forward to power shots
        while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() < 9.5) {
            if (robot.mecanumDrive.getCenterPosition() > 5) {

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

        while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 60 && robot.mecanumDrive.getRightPosition() < 60) {
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
        delay(0.25);

        //Start shooter
        robot.launchpad.shoot(0.95);
        delay(0.5);

        //Move to and shoot at First Power Shot
        while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() < 10.5) {
            driveSideways(0.43);

            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }

        driveSideways(0);
        delay(0.5);

        robot.launchpad.setConveyor(1.0);
        delay(1.4);
        robot.launchpad.setConveyor(0.0);

        //Second Power Shot
        while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() < 16.5){
            driveSideways(0.43);

            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        driveSideways(0);
        delay(0.5);

        robot.launchpad.setConveyor(1.0);
        delay(0.5);
        robot.launchpad.setConveyor(0.0);


        //Third Power Shot
        while (opModeIsActive() && robot.mecanumDrive.getCenterPosition() < 20.5) {
            driveSideways(0.43);

            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        driveSideways(0);
        delay(0.5);

        robot.launchpad.setConveyor(1.0);
        delay(1.0);
        robot.launchpad.setConveyor(0.0);
        delay(0.25);
        robot.launchpad.shoot(0);

        //Move Right towards Wobble Goal/Rings
        while(opModeIsActive() && robot.mecanumDrive.getCenterPosition() > -15){
            driveSidewaysControlled(-0.7);

            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        driveSideways(0);
        delay(0.25);

        //Prepare to place wobble goal or score rings(depending on which position)

        //Position A
        if(RightPosFrameGrabber.position == "A") {
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > 55 && robot.mecanumDrive.getRightPosition() < 65) {
                turnLeft(-0.25);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            turnLeft(0);
            delay(0.25);
        }

        //Position B
        if(RightPosFrameGrabber.position == "B"){
            //Drive back and intake ring
            robot.intake.intakeBack(0.95);
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > 40 && robot.mecanumDrive.getRightPosition() > 40){
                driveForward(-0.5);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.5);

            //Drive forward and score wobble goal
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 65 && robot.mecanumDrive.getLeftPosition() < 65){
                driveForward(0.75);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.5);

            //RELEASE WOBBLE GOAL PLACEHOLDER

            //Drive back and score ring
            robot.launchpad.shoot(1.0);
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > 40 && robot.mecanumDrive.getRightPosition() > 40){
                driveForward(-0.5);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.5);

            robot.intake.intakeBack(0);
            delay(0.2);

            robot.launchpad.setConveyor(1.0);
            delay(2.0);

            //Park
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < 60 && robot.mecanumDrive.getLeftPosition() < 60){
                driveForward(0.75);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
            driveForward(0);
            delay(0.5);

        }

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

    public void driveSidewaysControlled(double power){
        //treat left as positive
        if(power > 0){
            robot.mecanumDrive.frontR.setPower(-power - 0.01);
            robot.mecanumDrive.backR.setPower(power);
            robot.mecanumDrive.frontL.setPower(-power);
            robot.mecanumDrive.backL.setPower(power + 0.01);

        }else if (power < 0){
            robot.mecanumDrive.frontR.setPower(-power + 0.01);
            robot.mecanumDrive.backR.setPower(power);
            robot.mecanumDrive.frontL.setPower(-power);
            robot.mecanumDrive.backL.setPower(power - 0.01);
        }
    }

    public void turnLeft(double power){
        robot.mecanumDrive.frontR.setPower(power);
        robot.mecanumDrive.backR.setPower(power);
        robot.mecanumDrive.frontL.setPower(power);
        robot.mecanumDrive.backL.setPower(power);
    }
}
