package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class WobbleGoal{
    public static Servo wobbleGrabber;
    public static Servo wobbleScorer; //Will be written in later
    LinearOpMode OpMode;

    public WobbleGoal(FullRobot robot, HardwareMap map, LinearOpMode OpMode){
        this.OpMode = OpMode;

// match with phone names, set power
        wobbleGrabber = map.get(Servo.class, "WG");
        wobbleGrabber.getDirection();
    }

    public void WobbleGrab(boolean grab){
        if(grab){
            wobbleGrabber.setPosition(1);
        } else{
            wobbleGrabber.setPosition(0.2);
        }
    }
}
