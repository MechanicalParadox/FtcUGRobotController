package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FullRobot {
    public MecanumDrive mecanumDrive;
    //public FlywheelTestMotor flywheelTestMotor;

    private OpMode master;
    public Thread subsystemExector;

    public FullRobot(LinearOpMode OpMode, HardwareMap map){
        this.master = OpMode;

        mecanumDrive = new MecanumDrive(this, map, OpMode);
        //flywheelTestMotor = new FlywheelTestMotor(this, map, OpMode);

    }
}
