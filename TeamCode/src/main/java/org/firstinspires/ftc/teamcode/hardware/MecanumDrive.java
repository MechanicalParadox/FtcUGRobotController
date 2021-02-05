package org.firstinspires.ftc.teamcode.hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.odometry.InitialPosCalcs;
import org.firstinspires.ftc.teamcode.odometry.Pos2d;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MecanumDrive extends InitialPosCalcs {
    public static DcMotorEx frontR;
    public static DcMotorEx frontL;
    public static DcMotorEx backR;
    public static DcMotorEx backL;
    //public ElapsedTime eTime;
    LinearOpMode opMode;
    Telemetry telemetry;
    LinearOpMode OpMode;

    public MecanumDrive(FullRobot robot, HardwareMap map, Telemetry telemetry, LinearOpMode OpMode){
        super(telemetry);
        this.OpMode = OpMode;
// match with phone names
        frontR = map.get(DcMotorEx.class, "FR");
        frontL = map.get(DcMotorEx.class, "FL");
        backR = map.get(DcMotorEx.class, "BR");
        backL = map.get(DcMotorEx.class, "BL");

//add brake power
        frontR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//set drivetrain motors to run without encoder
        frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

//reset drivetrain encoders
    public void reset() {
        frontL.setPower(0);
        frontR.setPower(0);
        backL.setPower(0);
        backR.setPower(0);

        frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

//determine encoder changes
    public double getRawLeftPosition() {
        return frontL.getCurrentPosition();
    }

    public double getRawCenterPosition() {
        return backL.getCurrentPosition();
    }

    public double getRawRightPosition() {
        return frontR.getCurrentPosition();
    }

//determine position in cm
    //Distances in inches currently to check distance.  will change to cm when implementing
    public double getLeftPosition() {
    return ((Math.PI * 2.83) / 1440 * frontL.getCurrentPosition());
    }

    public double getCenterPosition() {
        return (((Math.PI * 2.83) / 1440) * backL.getCurrentPosition());
    }

    public double getRightPosition() {
        return (-(Math.PI * 2.83) / 1440 * frontR.getCurrentPosition());
    }

    public Pos2d getPosition() {
        //update();
        return getPosEstimate();
    }

    /**
     * creates a unique filename for logging
     *
     * @param fileName  orig filename
     * @param extension file extension
     * @return a unique filename
     */
    private String createUniqueFileName(String fileName, String extension) {
        return fileName + DateFormat.getDateTimeInstance().format(new Date()) + extension;
    }

    /**
     * Overides the method in TrigThreeWheelLocalizer so that the localizer can have access to the
     * encoder values of each tracking wheel.
     *
     * @return
     */
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                getLeftPosition(),
                getRightPosition(),
                getCenterPosition()
        );
    }
}
