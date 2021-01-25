package org.firstinspires.ftc.teamcode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.FullRobot;

import java.lang.annotation.ElementType;

@TeleOp(name = "drivetrain")
public class straferChassis extends LinearOpMode {
    private FullRobot robot;
    public ElapsedTime time;

    @Override
    public void runOpMode() throws InterruptedException{
        robot = new FullRobot(this, hardwareMap);
        time = new ElapsedTime();

        robot.mecanumDrive.reset();
        waitForStart();

        while(!isStopRequested()){
            telemetry.addData("leftEncoder", robot.mecanumDrive.getRawLeftPosition());
            telemetry.addData("rightEncoder", robot.mecanumDrive.getRawRightPosition());
            telemetry.addData("centerEncoder", robot.mecanumDrive.getRawCenterPosition());
            telemetry.update();

            //Basic Driving Controls
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
        }
    }
}
