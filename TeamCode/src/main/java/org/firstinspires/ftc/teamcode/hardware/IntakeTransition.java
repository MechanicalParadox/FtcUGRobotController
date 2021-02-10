package org.firstinspires.ftc.teamcode.hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeTransition {
    public static DcMotorEx backIntake;
    public static DcMotorEx frontIntake;

    LinearOpMode OpMode;

    public IntakeTransition(FullRobot robot, HardwareMap map, LinearOpMode OpMode){
        this.OpMode = OpMode;
// match with phone names, set power, and set with encoder
        backIntake = map.get(DcMotorEx.class, "BI");
        backIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontIntake = map.get(DcMotorEx.class, "FI");
        frontIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //Back Intake Functions
    public void intakeBack(double power){
        backIntake.setPower(power);
    }

    //Front Intake Functions
    public void intakeFront(double power){
        frontIntake.setPower(power);
    }

}
