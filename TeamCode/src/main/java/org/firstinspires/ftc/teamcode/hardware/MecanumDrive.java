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

// Teleop drive
    public void moveBy(float LeftX, float LeftY, float RightX){
        //Deadzones
        if(Math.abs(LeftX) <= 0.05){
            LeftX = 0;
        }
        if(Math.abs(LeftY) <= 0.05){
            LeftY = 0;
        }
        if(Math.abs(RightX) <= 0.05){
            RightX = 0;
        }

        // Calculates direction each word should turn(range -3 to 3)
        double frontLeft = LeftY + LeftX + RightX;
        double frontRight = LeftY - LeftX - RightX;
        double backLeft = LeftY - LeftX + RightX;
        double backRight = LeftY + LeftX - RightX;

        // Calculates the motor which requires the most power
        double largestVariable = Math.abs(frontLeft);

        if (largestVariable < Math.abs(frontRight))
            largestVariable = Math.abs(frontRight);

        if (largestVariable < Math.abs(backLeft))
            largestVariable = Math.abs(backLeft);

        if (largestVariable < Math.abs(backRight))
            largestVariable = Math.abs(backRight);

        // Sets range from -1 to 1
        frontLeft /= largestVariable;
        frontRight /= largestVariable;
        backLeft /= largestVariable;
        backRight /= largestVariable;

      /*  if (gamepad1.left_trigger > 0){
            frontLeft /= 4;
            frontRight /= 4;
            backLeft /= 4;
            backRight /= 4;
        }*/

        // Sets motor powers
        frontL.setPower(-frontLeft);
        frontR.setPower(frontRight);
        backL.setPower(-backLeft);
        backR.setPower(backRight);
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
    return (-(Math.PI * 2.83) / 1440 * frontL.getCurrentPosition());
    }

    public double getCenterPosition() {
        return (-((Math.PI * 2.83) / 1440) * backL.getCurrentPosition());
    }

    public double getRightPosition() {
        return ((Math.PI * 2.83) / 1440 * frontR.getCurrentPosition());
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
