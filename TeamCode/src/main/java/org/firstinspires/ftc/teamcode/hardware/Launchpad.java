package org.firstinspires.ftc.teamcode.hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launchpad {
    public static DcMotorEx flywheel;
    private CRServo conveyor;
    LinearOpMode OpMode;

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

    /*public double fLastEncoder = 0;
    public void velocityShot()
    {
        public double fVelocityTime = System.nanoTime();
        public double fEncoder = flywheel.getCurrentPosition();
        public double fVelocity = (double)(fEncoder - fLastEncoder) / (fVelocityTime - fLastVelocityTime);

        if(fVelocity >= (fTarget + tolerance))
        {
            setFPower(0.8);
        }

        else if(fVelocity < (fTarget - tolerance))
        {
            setFPower(0.8);
        }

        fLastEncoder = fEncoder;
        fLastVelocityTime = fVelocityTime;
    }*/


}
