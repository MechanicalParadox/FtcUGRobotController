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
    public static DcMotorEx wobbleScorer; //Will be written in later
    LinearOpMode OpMode;

    public WobbleGoal(FullRobot robot, HardwareMap map, LinearOpMode OpMode){
        this.OpMode = OpMode;

// match with phone names, set power
        wobbleGrabber = map.get(Servo.class, "WG");
        wobbleGrabber.getDirection();
        wobbleScorer = map.get(DcMotorEx.class, "WS");
        wobbleScorer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleScorer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void WobbleGrab(boolean grab){
        if(grab){
            wobbleGrabber.setPosition(0.3);
        } else{
            wobbleGrabber.setPosition(1);
        }
    }

    public void WobbleScore(int position){
        if(position == 0){
            wobbleScorer.setPower(0);
        } else if(position <= 0){
            wobbleScorer.setPower(0.1);
        } else {
            wobbleScorer.setPower(-0.5);
        }
    }

    public void WobbleHold(boolean hold){
        if(hold){
            wobbleScorer.setPower(-0.2);
        }
    }

    public void reset() {
        wobbleScorer.setPower(0);

        wobbleScorer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        wobbleScorer.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void WobbleLift(int position){
        if(position == 0){
            wobbleScorer.setTargetPosition(0);
            wobbleScorer.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            wobbleScorer.setPower(0.1);
        } else if(position == 2) {
            wobbleScorer.setTargetPosition(-385);
            wobbleScorer.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            if (wobbleScorer.getCurrentPosition() > -385){
                wobbleScorer.setPower(-0.5);
            } else{
                wobbleScorer.setPower(0.1);
            }
        } else if(position == 3){
            wobbleScorer.setTargetPosition(-490);
            wobbleScorer.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            if (wobbleScorer.getCurrentPosition() > -490){
                wobbleScorer.setPower(-0.5);
            } else{
                wobbleScorer.setPower(0.1);
            }
        } else if(position == 1){
            wobbleScorer.setTargetPosition(-50);
            wobbleScorer.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if(wobbleScorer.getCurrentPosition() > -50){
                wobbleScorer.setPower(-0.5);
            } else{
                wobbleScorer.setPower(0.1);
            }
        }
    }
    public void runWithoutEncoders(){
        wobbleScorer.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void runWithEncoders(){
        wobbleScorer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
