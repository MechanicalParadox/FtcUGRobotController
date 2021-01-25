package org.firstinspires.ftc.teamcode.hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FlywheelTestMotor {
    public static DcMotorEx flywheel;

    LinearOpMode OpMode;

    public FlywheelTestMotor(FullRobot robot, HardwareMap map, LinearOpMode OpMode){
        this.OpMode = OpMode;
// match with phone names, set power, and set with encoder
        flywheel = map.get(DcMotorEx.class, "FLY");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void shoot(double power){
        flywheel.setPower(power);
    }


}
