package org.firstinspires.ftc.teamcode.opmode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.FullRobot;
import org.firstinspires.ftc.teamcode.odometry.InitialPosCalcs;

import java.lang.annotation.ElementType;

@TeleOp(name = "FullRobotTele")
public class Comp1Tele extends LinearOpMode {
    private FullRobot robot;
    public ElapsedTime time;
    double TARGET_VOLTAGE = 12.3;
    double voltage = 0;
    double kP = 0.18;

    @Override
    public void runOpMode() throws InterruptedException{
        robot = new FullRobot(this, hardwareMap, telemetry);
        time = new ElapsedTime();

        robot.mecanumDrive.reset();
        waitForStart();

        while(!isStopRequested()) {
            telemetry.addData("XPos", robot.mecanumDrive.getPosition().getX());
            telemetry.update();
            /////////////////////////////////////////////////////////////////////

            //************************ GAMEPAD 1 DRIVE ************************//

            /////////////////////////////////////////////////////////////////////
            // Basic Driving Controls
            double LeftY = -gamepad1.left_stick_y;
            double LeftX = gamepad1.left_stick_x;
            double RightX = -gamepad1.right_stick_x;
            //Deadzones
            if (Math.abs(LeftX) <= 0.05) {
                LeftX = 0;
            }
            if (Math.abs(LeftY) <= 0.05) {
                LeftY = 0;
            }
            if (Math.abs(RightX) <= 0.05) {
                RightX = 0;
            }

            // Calculates direction each word should turn(range -3 to 3)
            double frontLeft = LeftY + LeftX + RightX;
            double frontRight = LeftY - LeftX - RightX;
            double backLeft = LeftY - LeftX + RightX;
            double backRight = LeftY + LeftX - RightX;

            // Calculates the motor which requires the most power
            double largestVariable = 1.0; //Math.abs(frontLeft);

            if (largestVariable < Math.abs(frontLeft))
                largestVariable = Math.abs(frontLeft);

            if (largestVariable < Math.abs(frontRight))
                largestVariable = Math.abs(frontRight);

            if (largestVariable < Math.abs(backLeft))
                largestVariable = Math.abs(backLeft);

            if (largestVariable < Math.abs(backRight))
                largestVariable = Math.abs(backRight);

            // Sets range from -1 to 1
            frontLeft /= largestVariable;
            frontRight /= largestVariable;
            backLeft /= largestVariable;
            backRight /= largestVariable;

            //Scaling Slowspeed
            double slowBy = gamepad1.left_trigger;
            if (gamepad1.left_trigger >= 0.25) {
                frontLeft *= (1.25 - slowBy);
                frontRight *= (1.25 - slowBy);
                backLeft *= (1.25 - slowBy);
                backRight *= (1.25 - slowBy);
            }

            // Sets motor powers
            robot.mecanumDrive.frontL.setPower(frontLeft);
            robot.mecanumDrive.backR.setPower(-backRight);
            robot.mecanumDrive.frontR.setPower(-frontRight);
            robot.mecanumDrive.backL.setPower(backLeft);

            // Intakes
            if (gamepad1.left_bumper == true) {
                robot.intake.intakeFront(0.90);
            } else if (gamepad1.right_trigger >= 0.25) {
                robot.intake.intakeFront(-0.90);
            } else {
                robot.intake.intakeFront(0);
            }

            if (gamepad1.right_bumper == true) {
                robot.intake.intakeBack(-0.90);
                robot.intake.intakeFront(-0.90);
            } else if (gamepad1.right_trigger >= 0.25) {
                robot.intake.intakeBack(0.90);
            } else {
                robot.intake.intakeBack(0);
            }

            /////////////////////////////////////////////////////////////////////

            //************************ GAMEPAD 2 DRIVE ************************//

            /////////////////////////////////////////////////////////////////////

            //Shooter
            if (gamepad2.left_trigger >= 0.25) {
                robot.launchpad.shoot(1);
            } else if (gamepad2.a) {              //Test Powers!!!!
                robot.launchpad.shoot(0.75);
            } else if (gamepad2.b) {              //Test Powers!!!!
                robot.launchpad.shoot(0.5);
            } else if (gamepad2.x) {              //Test Powers!!!!
                robot.launchpad.shoot(0.60);
            } else if (gamepad2.left_bumper) {              //Test Powers!!!!
                robot.launchpad.shoot(0.9);
            } else {
                robot.launchpad.shoot(0);
            }

            //Conveyor
            if (gamepad2.right_trigger >= 0.25) {
                robot.launchpad.setConveyor(1.0);
            } else if (gamepad2.right_bumper) {
                robot.launchpad.setConveyor(-1.0);
            } else {
                robot.launchpad.setConveyor(0);
            }

            //Wobble Grabber
            if (gamepad2.x){
                robot.wobbleGoal.WobbleGrab(true);
            } else{
                robot.wobbleGoal.WobbleGrab(false);
            }

            telemetry.addData("Left Encoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("Right Encoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("Center Encoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();
        }

    }
    public double batteryVoltage(){
        return this.hardwareMap.voltageSensor.iterator().next().getVoltage();
    }
    public void voltageShoot(double power) {
        voltage = batteryVoltage();
        double error = TARGET_VOLTAGE - voltage;
        double motorOut = (error * kP) + power;
        motorOut = Range.clip(motorOut, 0, 1);
        robot.launchpad.shoot(motorOut);
    }
}
