package org.firstinspires.ftc.teamcode.odometry;
//import static java.lang.Math;
import java.util.List;
import java.util.Collections;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class InitialPosCalcs {
//All values in cm
//Declare values(W is constant)
    public double chassisWidth = 10;
    public static double dTheta = 0;
    double rCenter = 0;
    double xSides = 0;
    double ySides = 0;
    private Pos2d posEstimate;
    private List<Double> lastWheelPositions;

    Telemetry telemetry;

//function to find positions
    public InitialPosCalcs(Telemetry telemetry) {
        this.telemetry = telemetry;

        lastWheelPositions = Collections.emptyList();
        posEstimate = new Pos2d(0, 0, Math.toRadians(0));
    }

    public void update() {
        List<Double> wheelPosiitons = getWheelPositions();
        if(!lastWheelPositions.isEmpty()){
            double dL = wheelPosiitons.get(0) - lastWheelPositions.get(0);
            double dR = wheelPosiitons.get(1) - lastWheelPositions.get(1);
            double dC = wheelPosiitons.get(2) - lastWheelPositions.get(2);
        }
    }

    public abstract List<Double> getWheelPositions(

    );
}
