package org.firstinspires.ftc.teamcode.hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class Launchpad {
    public static DcMotorEx flywheel;
    public static VoltageSensor voltageSensor;
    private CRServo conveyor;
    LinearOpMode OpMode;

    private int flyEncoder = 0;
    private int flyLastEncover = 0;
    private long flyVelocityTime, flyLastVelocityTime = 0;
    private double flyVelocity;
    private double tolerance = 0.5e-7;

    public Launchpad(FullRobot robot, HardwareMap map, LinearOpMode OpMode){
        this.OpMode = OpMode;
// match with phone names, set power, and set with encoder
        flywheel = map.get(DcMotorEx.class, "S");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        conveyor = map.get(CRServo.class, "C");
        conveyor.getDirection();

    }

    public void shoot(double power){
        flywheel.setPower(power);
    }

    public void setConveyor(double power){
        conveyor.setPower(power);
    }

    public void encoderRegulator(double flyTarget){
        flyVelocityTime = System.nanoTime();
        flyEncoder = flywheel.getCurrentPosition();
        flyVelocity = (double)(flyEncoder - flyLastEncover) / (flyLastVelocityTime - flyVelocityTime);

        if(flyVelocity >= (flyTarget + tolerance)){
            shoot(flyTarget);
        }
    }


}
