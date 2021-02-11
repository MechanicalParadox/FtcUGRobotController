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

        while(!isStopRequested()){
            telemetry.addData("leftEncoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getCenterPosition());
            telemetry.update();


            /////////////////////////////////////////////////////////////////////

            //************************ GAMEPAD 1 DRIVE ************************//

            /////////////////////////////////////////////////////////////////////
            // Basic Driving Controls
            double LeftY = -gamepad1.left_stick_y;
            double LeftX = gamepad1.left_stick_x;
            double RightX = gamepad1.right_stick_x;

            // Calculates direction each word should turn(range -3 to 3)
            double frontLeft = LeftY + LeftX + RightX;
            double frontRight = LeftY - LeftX - RightX;
            double backLeft = LeftY - LeftX + RightX;
            double backRight = LeftY + LeftX - RightX;

            // Calculates the motor which requires the most power
            double largestVariable = Math.abs(frontLeft);

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

            if(gamepad1.right_trigger > 0){
                frontLeft /= 2;
                frontRight /= 2;
                backLeft /= 2;
                backRight /= 2;
            } else if (gamepad1.left_trigger > 0){
                frontLeft /= 4;
                frontRight /= 4;
                backLeft /= 4;
                backRight /= 4;
            }

            // Sets motor powers
            robot.mecanumDrive.frontL.setPower(-frontLeft);
            robot.mecanumDrive.frontR.setPower(frontRight);
            robot.mecanumDrive.backL.setPower(-backLeft);
            robot.mecanumDrive.backR.setPower(backRight);

            // Intakes
            if (gamepad1.left_bumper == true){
                robot.intake.intakeFront(0.75);
            } else if (gamepad1.left_trigger >= 0.25){
                robot.intake.intakeFront(-0.75);
            } else {
                robot.intake.intakeFront(0);
            }

            if (gamepad1.right_bumper == true){
                robot.intake.intakeBack(0.75);
            } else if (gamepad1.right_trigger >= 0.25) {
                robot.intake.intakeBack(-0.75);
            } else  {
                robot.intake.intakeBack(0);
            }

            /////////////////////////////////////////////////////////////////////

            //************************ GAMEPAD 2 DRIVE ************************//

            /////////////////////////////////////////////////////////////////////

            //Shooter
            if (gamepad2.left_trigger >= 0.25){
                robot.launchpad.shoot(1);//need to confirm
            } else if(gamepad2.a){              //Test Powers!!!!
                robot.launchpad.shoot(0.75);
            }  else if(gamepad2.b){              //Test Powers!!!!
                robot.launchpad.shoot(0.5);
            }  else if(gamepad2.x){              //Test Powers!!!!
                robot.launchpad.shoot(0.25);
            }  else if(gamepad2.y){              //Test Powers!!!!
                robot.launchpad.shoot(0.9);
            } else{
                robot.launchpad.shoot(0);
            }

            //Conveyor
            if (gamepad2.right_trigger >= 0.25){
                robot.launchpad.setConveyor(1.0);
            } else if (gamepad2.right_bumper){
                robot.launchpad.setConveyor(-1.0);
            } else{
                robot.launchpad.setConveyor(0);
            }
        }

    }
}
