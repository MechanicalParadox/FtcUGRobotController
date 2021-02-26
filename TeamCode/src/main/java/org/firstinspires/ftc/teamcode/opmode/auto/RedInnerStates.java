package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.FullRobot;
import org.firstinspires.ftc.teamcode.hardware.vision.LeftPosFrameGrabber;
import org.firstinspires.ftc.teamcode.odometry.InitialPosCalcs;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "RedStates", group = "Tests")
public class RedInnerStates extends LinearOpMode {
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

    //Positioning calculations
    double xInitial = 0.0, yInitial = 0.0, thetaInitial = 0.0;

    InitialPosCalcs initialPosCalcs;
    final double INCHES_PER_TICK = (Math.PI * 2.83)/1440; //May be unneccessary

    InitialPosCalcs PosCalcUpdate;

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

        while (!isStarted() && !isStopRequested()) { // 1 stack W: 96. H: 42.  4 stack: W: 105.  H: 84
            telemetry.addData("Position", leftPosFrameGrabber.position);
            telemetry.addData("Threshold", leftPosFrameGrabber.threshold);
            telemetry.addData("Rect width", leftPosFrameGrabber.rectWidth);
            telemetry.addData("Rect height", leftPosFrameGrabber.rectHeight);

            /*// Left Guide
            if (gamepad1.dpad_right) {
                leftPosFrameGrabber.leftGuide += 0.001;
            } else if (gamepad1.dpad_left) {
                leftPosFrameGrabber.leftGuide -= 0.001;
            }*/

            // Mask
            if (gamepad1.dpad_down == true) {
                leftPosFrameGrabber.mask += 0.001;
            } else if (gamepad1.dpad_up == true) {
                leftPosFrameGrabber.mask -= 0.001;
            }

            // Threshold
            if (gamepad2.x == true) {
                leftPosFrameGrabber.threshold += 0.001; // was 0.001
            } else if (gamepad2.b == true) {
                leftPosFrameGrabber.threshold -= 0.001;
            }

            robot.wobbleGoal.WobbleGrab(false);

            telemetry.update();
        }

        waitForStart();

        robot.wobbleGoal.WobbleGrab(true);
        delay(0.5);
        robot.wobbleGoal.WobbleLift(2);

        //Start the Basic Position Calculations
        PosCalcUpdate = new InitialPosCalcs(robot.mecanumDrive.frontL, robot.mecanumDrive.frontR, robot.mecanumDrive.backL);

        webCam.stopStreaming();
        robot.mecanumDrive.reset();
        elapsedTime.reset();
        //delay(1);

//Drive Forward, Turn, go to Power Shot 1, turn back
        PosCalcUpdate.InitialPosCalcsUpdate();
        robot.launchpad.shoot(0.9);
        moveTo(0, 18, 0, 0.5);
        //delay(2);

        moveTo(-10, 40, 0, 0.75); //was -1
        //delay(0.25);

        //shoot First power shot
        robot.launchpad.setConveyor(1.0);
        delay(1.4);
        robot.launchpad.setConveyor(0.0);

        //Turn and shoot second power shot
        while(opModeIsActive() && PosCalcUpdate.returnTheta() > -5){
            turnLeft(0.25);
            PosCalcUpdate.InitialPosCalcsUpdate();
            telemetry.update();
        }
        turnLeft(0);
        delay(0.1);

        robot.launchpad.setConveyor(1.0);
        delay(0.5);//May have to change
        robot.launchpad.setConveyor(0.0);
        delay(0.25);

        //Turn and shoot third power shot
        while(opModeIsActive() && PosCalcUpdate.returnTheta() > -8){
            turnLeft(0.25);
            PosCalcUpdate.InitialPosCalcsUpdate();
        }
        turnLeft(0);
        delay(0.1);

        robot.launchpad.setConveyor(1.0);
        delay(1);//May have to change
        robot.launchpad.setConveyor(0.0);
        robot.launchpad.shoot(0);

        //Move to place wobble goal
        if(leftPosFrameGrabber.position == "A"){
            //Drive to position A and place first wobble goal
            moveTo(20, 60, 80, 0.5);
            robot.wobbleGoal.WobbleLift(0);
            delay(0.4);
            robot.wobbleGoal.WobbleGrab(false);
            delay(0.1);

            double leftEncoderChange = robot.mecanumDrive.getLeftPosition() - 3;
            double rightEncoderChange = robot.mecanumDrive.getRightPosition() - 3;
            //drive back and turn away from first wobble goal
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-0.5);
            }
            driveForward(0);
            moveTo(10, 60, 165, 0.75);//may go back way too far

            //drive to pick up second wobble goal
            leftEncoderChange = robot.mecanumDrive.getLeftPosition() + 49;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() + 49;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < leftEncoderChange && robot.mecanumDrive.getRightPosition() < rightEncoderChange) {
                driveForward(0.25);
            }
            driveForward(0);
            robot.wobbleGoal.WobbleGrab(true);
            delay(0.5);
            robot.wobbleGoal.WobbleLift(2);

            //Drive back and place wobble goal
            leftEncoderChange = robot.mecanumDrive.getLeftPosition() - 40;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() - 40;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-0.75);
            }

            //Turn to place
            double dThetaTravel = 110;
            while(opModeIsActive() && PosCalcUpdate.returnTheta() > dThetaTravel){
                turnLeft(0.25);
                PosCalcUpdate.InitialPosCalcsUpdate();
                telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
                telemetry.addData("Turning to", "Left");
                telemetry.update();
            }
            turnLeft(0);
            //Drive forward to place
            leftEncoderChange = robot.mecanumDrive.getLeftPosition() + 5;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() + 5;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < leftEncoderChange && robot.mecanumDrive.getRightPosition() < rightEncoderChange) {
                driveForward(0.25);
            }
            driveForward(0);

            robot.wobbleGoal.WobbleLift(0);
            delay(0.4);
            robot.wobbleGoal.WobbleGrab(false);
            delay(0.1);
        }
        if(leftPosFrameGrabber.position == "B"){
            //Drive to ring and collect to score
            double dThetaTravel = -55;
            while(opModeIsActive() && PosCalcUpdate.returnTheta() > dThetaTravel){
                turnLeft(0.25);
                PosCalcUpdate.InitialPosCalcsUpdate();
                telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
                telemetry.addData("Turning to", "Left");
                telemetry.update();
            }
            turnLeft(0);
            delay(0.2);
            robot.intake.intakeBack(-0.95);

            robot.launchpad.shoot(0.98);
            //Drive back to get ring
            double leftEncoderChange = robot.mecanumDrive.getLeftPosition() - 20;
            double rightEncoderChange = robot.mecanumDrive.getRightPosition() - 20;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-0.5);
            }
            driveForward(0);
            delay(0.1);

            //Turn to score wobbler
            dThetaTravel = -7; //maybe even more left?
            while(opModeIsActive() && PosCalcUpdate.returnTheta() < dThetaTravel){
                turnLeft(-0.25);
                PosCalcUpdate.InitialPosCalcsUpdate();
                telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
                telemetry.addData("Turning to", "Left");
                telemetry.update();
            }
            turnLeft(0);
            delay(0.5);

            //Shoot ring
            robot.launchpad.setConveyor(1.0);
            delay(1);//May have to change
            robot.launchpad.setConveyor(0.0);
            robot.launchpad.shoot(0);
            robot.intake.intakeBack(0);

            //Drive forward to score wobbler
            leftEncoderChange = robot.mecanumDrive.getLeftPosition() + 38;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() + 38;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < leftEncoderChange && robot.mecanumDrive.getRightPosition() < rightEncoderChange) {
                driveForward(0.75);
            }
            driveForward(0);
            delay(0.1);

            //Place Wobble Goal
            robot.wobbleGoal.WobbleLift(0);
            delay(0.4);
            robot.wobbleGoal.WobbleGrab(false);
            delay(0.3);

            //Turn to go to second wobble goal
            dThetaTravel = -15; //maybe even more left?
            while(opModeIsActive() && PosCalcUpdate.returnTheta() > dThetaTravel){
                turnLeft(0.35);
                PosCalcUpdate.InitialPosCalcsUpdate();
                telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
                telemetry.addData("Turning to", "Left");
                telemetry.update();
            }
            turnLeft(0);

            leftEncoderChange = robot.mecanumDrive.getLeftPosition() - 42;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() - 42;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-0.75);
            }
            driveForward(0);
            delay(0.1);

            //Turn and grab wobble goal
            dThetaTravel = 165; //maybe even more left?
            while(opModeIsActive() && PosCalcUpdate.returnTheta() < dThetaTravel){
                turnLeft(-0.35);
                PosCalcUpdate.InitialPosCalcsUpdate();
                telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
                telemetry.addData("Turning to", "Left");
                telemetry.update();
            }
            turnLeft(0);

            leftEncoderChange = robot.mecanumDrive.getLeftPosition() + 8;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() + 8;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < leftEncoderChange && robot.mecanumDrive.getRightPosition() < rightEncoderChange) {
                driveForward(0.25);
            }
            driveForward(0);
            delay(0.1);

            driveForward(0);
            robot.wobbleGoal.WobbleGrab(true);
            delay(0.5);
            robot.wobbleGoal.WobbleLift(2);

            //Drive back and score wobble goal(PLEASE HAVE ENOUGH TIME)
            leftEncoderChange = robot.mecanumDrive.getLeftPosition() - 50;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() - 50;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-0.75);
            }
            driveForward(0);

            dThetaTravel = 0; //maybe even more left?
            while(opModeIsActive() && PosCalcUpdate.returnTheta() > dThetaTravel){
                turnLeft(0.35);
                PosCalcUpdate.InitialPosCalcsUpdate();
                telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
                telemetry.addData("Turning to", "Left");
                telemetry.update();
            }
            turnLeft(0);
            //Place Wobble Goal
            robot.wobbleGoal.WobbleLift(0);
            delay(0.4);
            robot.wobbleGoal.WobbleGrab(false);
            delay(0.3);

            //Park
            leftEncoderChange = robot.mecanumDrive.getLeftPosition() - 10;
            rightEncoderChange = robot.mecanumDrive.getRightPosition() - 10;
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-0.75);
            }
            driveForward(0);
        }

        while(opModeIsActive()){
            telemetry.addData("angle", PosCalcUpdate.returnTheta());
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

    // moveTo(-15, 60, 178, 0.5);//may go back way too far
    public void moveTo(double xFinal, double yFinal, double thetaFinal, double power){
        //Calculate angle to drive straight and distance to travel
        double dX = xFinal - xInitial;
        double dY = yFinal - yInitial;
        double dThetaTravel, dThetaFinal, distanceStraight;

        distanceStraight = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)); //always positive

        //dThetaTravel = Math.atan(dX/dY);
        if(dX > 0 && dY > 0){
            dThetaTravel = Math.toDegrees(Math.atan(dX/dY));
        } else if(dX < 0 && dY > 0){
            dThetaTravel = Math.toDegrees(Math.atan(dX/dY)); //TEST.  IF RIGHT NEED TO SWITCH ALL OTHERS
        } else if(dX > 0 && dY < 0){
            dThetaTravel = 180 - Math.toDegrees(Math.atan(dX/dY));
        } else if(dX < 0 && dY < 0){
            dThetaTravel = Math.toDegrees(Math.atan(dX/dY)) - 180;
        } else if (dX == 0){ // May want to rework these so it turns to whichever is closer, will play around some more
            dThetaTravel = 0;
        } else{
            dThetaTravel = 90;
            if(dX < 0){
                distanceStraight *= -1;
            }
        }

        telemetry.addData("Turning to", dThetaTravel);
        telemetry.update();

        //Turn to travel angle
        while(opModeIsActive() && PosCalcUpdate.returnTheta() > dThetaTravel){
            turnLeft(0.25);
            PosCalcUpdate.InitialPosCalcsUpdate();
            telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
            telemetry.addData("Turning to", "Left");
            telemetry.update();
        }
        while(opModeIsActive() && PosCalcUpdate.returnTheta() < dThetaTravel){
            turnLeft(-0.25);
            PosCalcUpdate.InitialPosCalcsUpdate();
            telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
            telemetry.addData("Turning to", "Right");
            telemetry.update();
        }
        turnLeft(0);
        delay(0.15);
        telemetry.addData("Turning", "Complete");
        telemetry.update();

        //Travel distance to target position
        double leftEncoderChange = distanceStraight + robot.mecanumDrive.getLeftPosition();
        double rightEncoderChange = distanceStraight + robot.mecanumDrive.getRightPosition();
        if(leftEncoderChange > 0 && rightEncoderChange > 0) {
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() < leftEncoderChange && robot.mecanumDrive.getRightPosition() < rightEncoderChange) {
                driveForward(power);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
        } else{
            while (opModeIsActive() && robot.mecanumDrive.getLeftPosition() > leftEncoderChange && robot.mecanumDrive.getRightPosition() > rightEncoderChange) {
                driveForward(-power);

                telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
                telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
                telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
                telemetry.update();
            }
        }
        driveForward(0);
        delay(0.15);

        //Turn to final angle
        while(opModeIsActive() && PosCalcUpdate.returnTheta() > thetaFinal){
            turnLeft(0.25);
            PosCalcUpdate.InitialPosCalcsUpdate();
            telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
            telemetry.addData("Turning to", "Left");
            telemetry.update();
        }
        while(opModeIsActive() && PosCalcUpdate.returnTheta() < thetaFinal){
            turnLeft(-0.25);
            PosCalcUpdate.InitialPosCalcsUpdate();
            telemetry.addData("Turn to", PosCalcUpdate.returnTheta());
            telemetry.addData("Turning to", "Right");
            telemetry.update();
        }
        turnLeft(0);
        delay(0.15);
        telemetry.addData("Turning", "Complete");
        telemetry.update();

        xInitial = xFinal;
        yInitial = yFinal;
    }
}
