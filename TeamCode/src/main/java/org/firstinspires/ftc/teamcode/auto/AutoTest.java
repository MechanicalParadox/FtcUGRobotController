package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.FullRobot;

import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "TestGyro", group = "Tests")
public class AutoTest extends LinearOpMode {
    private FullRobot robot;
    public ElapsedTime elapsedTime;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new FullRobot(this, hardwareMap, telemetry);
        elapsedTime = new ElapsedTime();
        robot.mecanumDrive.reset();
        while (!opModeIsActive()){
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }
        //super.runOpMode();
        waitForStart();
        robot.mecanumDrive.reset();
        elapsedTime.reset();
        delay(1);

        //move forward to place wobble grabber
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
        while(opModeIsActive() && robot.mecanumDrive.getCenterPosition() < 20){
            if (robot.mecanumDrive.getCenterPosition() > 10){
                driveSideways(-0.25);
            } else {
                driveSideways(-0.5);

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
        robot.mecanumDrive.frontR.setPower(power);
        robot.mecanumDrive.backR.setPower(power);
        robot.mecanumDrive.frontL.setPower(-power);
        robot.mecanumDrive.backL.setPower(-power);
    }
    public void driveSideways(double power){
        //treat left as positive
        robot.mecanumDrive.frontR.setPower(power);
        robot.mecanumDrive.backR.setPower(-power);
        robot.mecanumDrive.frontL.setPower(power);
        robot.mecanumDrive.backL.setPower(-power);
    }
}
