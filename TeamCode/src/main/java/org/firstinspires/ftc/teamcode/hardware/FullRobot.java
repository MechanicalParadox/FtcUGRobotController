package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FullRobot {
    public MecanumDrive mecanumDrive;
    public IntakeTransition intake;
    public Launchpad launchpad;

    private OpMode master;
    public Thread subsystemExector;

    public FullRobot(LinearOpMode OpMode, HardwareMap map, Telemetry telemetry){
        this.master = OpMode;

        mecanumDrive = new MecanumDrive(this, map, telemetry, OpMode);
        intake = new IntakeTransition(this, map, OpMode);
        launchpad = new Launchpad(this, map, OpMode);
    }
}
