package org.firstinspires.ftc.teamcode.opmode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

    InitialPosCalcs initialPosCalcs;
    final double INCHES_PER_TICK = (Math.PI * 2.83)/1440; //May be unneccessary

    InitialPosCalcs PosCalcUpdate;
    @Override
    public void runOpMode() throws InterruptedException{
        robot = new FullRobot(this, hardwareMap, telemetry);
        time = new ElapsedTime();

        robot.mecanumDrive.reset();
        robot.wobbleGoal.runWithoutEncoders();
        waitForStart();
        //Start the Basic Position Calculations
        PosCalcUpdate = new InitialPosCalcs(robot.mecanumDrive.frontL, robot.mecanumDrive.frontR, robot.mecanumDrive.backL);

        while(!isStopRequested()) {
            PosCalcUpdate.InitialPosCalcsUpdate();
            //Display the global position coordinates
            telemetry.addData("X Position", PosCalcUpdate.returnXCoordinate()*INCHES_PER_TICK);
            telemetry.addData("Y Position", PosCalcUpdate.returnYCoordinate()*INCHES_PER_TICK);
            telemetry.addData("Theta(Degrees", PosCalcUpdate.returnTheta());

            //Check dX and dY
            telemetry.addData("DX", PosCalcUpdate.getDeltaX());

            //Check standard encoder positions
            telemetry.addData("Left Encoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("Right Encoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("Center Encoder", robot.mecanumDrive.getCenterPosition());
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
            } else if (gamepad2.b) {              //Test Powers!!!!
                robot.launchpad.shoot(0.5);
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

            /*if(gamepad2.a){
                robot.wobbleGoal.WobbleLift(3);
            } else if(gamepad2.y){
                robot.wobbleGoal.WobbleLift(2);
            } else {
                robot.wobbleGoal.WobbleLift(0);
            }*/

            if(gamepad2.a){
                robot.wobbleGoal.runWithoutEncoders();
                robot.wobbleGoal.WobbleHold(true);
            } else if(gamepad2.dpad_down == true){
                robot.wobbleGoal.runWithoutEncoders();
                robot.wobbleGoal.WobbleScore(-1);
                telemetry.addData("Wobble Score", "Down");
                telemetry.update();
            } else if (gamepad2.dpad_up == true){
                robot.wobbleGoal.runWithoutEncoders();
                robot.wobbleGoal.WobbleScore(1);
                telemetry.addData("Wobble Score", "Up");
                telemetry.update();
            } else {
                robot.wobbleGoal.runWithoutEncoders();
                robot.wobbleGoal.WobbleScore(0);
                telemetry.addData("Wobble Score", "Zero");
                telemetry.update();
            }
            telemetry.addData("Wobble Position", robot.wobbleGoal.wobbleScorer.getCurrentPosition());
            //telemetry.addData("Left Encoder", robot.mecanumDrive.getLeftPosition());
            //telemetry.addData("Right Encoder", robot.mecanumDrive.getRightPosition());
            //telemetry.addData("Center Encoder", robot.mecanumDrive.getCenterPosition());
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
