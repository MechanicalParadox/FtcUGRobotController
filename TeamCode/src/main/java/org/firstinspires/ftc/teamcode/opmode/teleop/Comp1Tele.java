package org.firstinspires.ftc.teamcode.opmode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.FullRobot;
import org.firstinspires.ftc.teamcode.odometry.InitialPosCalcs;

import java.lang.annotation.ElementType;

@TeleOp(name = "FullRobotTele")
public class Comp1Tele extends LinearOpMode {
    private FullRobot robot;
    public ElapsedTime time;

    @Override
    public void runOpMode() throws InterruptedException{
        robot = new FullRobot(this, hardwareMap, telemetry);
        time = new ElapsedTime();

        robot.mecanumDrive.reset();
        waitForStart();

        while(!isStopRequested()) {
            /////////////////////////////////////////////////////////////////////

            //************************ GAMEPAD 1 DRIVE ************************//

            /////////////////////////////////////////////////////////////////////
            // Basic Driving Controls
            double LeftY = -gamepad1.left_stick_y;
            double LeftX = gamepad1.left_stick_x;
            double RightX = gamepad1.right_stick_x;
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
            double slowBy = 1;
            if (gamepad1.left_trigger >= 0.25) {
                slowBy = 1/gamepad1.left_trigger;
                frontLeft /= slowBy;
                frontRight /= slowBy;
                backLeft /= slowBy;
                backRight /= slowBy;
            }

            // Sets motor powers
            robot.mecanumDrive.frontL.setPower(-frontLeft);
            robot.mecanumDrive.frontR.setPower(frontRight);
            robot.mecanumDrive.backL.setPower(-backLeft);
            robot.mecanumDrive.backR.setPower(backRight);

            // Intakes
            if (gamepad1.right_bumper == true) {
                robot.intake.intakeFront(0.90);
            } else if (gamepad1.right_trigger >= 0.25) {
                robot.intake.intakeFront(-0.90);
            } else {
                robot.intake.intakeFront(0);
            }

            if (gamepad1.left_bumper == true) {
                robot.intake.intakeBack(0.90);
            } else if (gamepad1.right_trigger >= 0.25) {
                robot.intake.intakeBack(-0.90);
            } else {
                robot.intake.intakeBack(0);
            }

            /////////////////////////////////////////////////////////////////////

            //************************ GAMEPAD 2 DRIVE ************************//

            /////////////////////////////////////////////////////////////////////

            //Shooter
            if (gamepad2.left_trigger >= 0.25) {
                robot.launchpad.shoot(1);//need to confirm
            } else if (gamepad2.a) {              //Test Powers!!!!
                robot.launchpad.shoot(0.75);
            } else if (gamepad2.b) {              //Test Powers!!!!
                robot.launchpad.shoot(0.5);
            } else if (gamepad2.x) {              //Test Powers!!!!
                robot.launchpad.shoot(0.60);
            } else if (gamepad2.y) {              //Test Powers!!!!
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

            telemetry.addData("FL Speed", frontLeft);
            telemetry.addData("FR Speed", frontRight);
            telemetry.addData("BL Speed", backLeft);
            telemetry.addData("BR Speed", backRight);
            telemetry.update();
        }

    }
}
