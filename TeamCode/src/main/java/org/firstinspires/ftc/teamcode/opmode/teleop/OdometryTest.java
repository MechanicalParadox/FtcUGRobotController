package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.FullRobot;
import org.firstinspires.ftc.teamcode.odometry.InitialPosCalcs;

@TeleOp(name = "Odometry Positioning Test")
public class OdometryTest extends LinearOpMode {
    private FullRobot robot;
    public ElapsedTime time;

    InitialPosCalcs initialPosCalcs;
    final double INCHES_PER_TICK = (Math.PI * 2.83)/1440; //May be unneccessary

    InitialPosCalcs PosCalcUpdate;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new FullRobot(this, hardwareMap, telemetry);
        time = new ElapsedTime();

        robot.mecanumDrive.reset();

        telemetry.addData("Status", "Init Complete");
        telemetry.update();
        waitForStart();

        //Start the Basic Position Calculations
        PosCalcUpdate = new InitialPosCalcs(robot.mecanumDrive.frontL, robot.mecanumDrive.frontR, robot.mecanumDrive.backL);

        //PLACEHOLDER IF WE NEED TO UPDATE ENCODER DIRECTIONS

        while (opModeIsActive()){
            PosCalcUpdate.InitialPosCalcsUpdate();
            //Display the global position coordinates
            telemetry.addData("X Position", PosCalcUpdate.returnXCoordinate()/INCHES_PER_TICK);
            telemetry.addData("Y Position", PosCalcUpdate.returnYCoordinate());
            telemetry.addData("Theta(Degrees)", PosCalcUpdate.returnTheta());

            //Check standard encoder positions
            telemetry.addData("Left Encoder", robot.mecanumDrive.getLeftPosition());
            telemetry.addData("Right Encoder", robot.mecanumDrive.getRightPosition());
            telemetry.addData("Center Encoder", robot.mecanumDrive.getCenterPosition());

            telemetry.update();
        }

    }
}