package org.firstinspires.ftc.teamcode.odometry;
//import static java.lang.Math;
import java.util.List;
import java.util.Collections;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class InitialPosCalcs {
//All values in cm
//Declare values(W is constant in cm)
    public double chassisWidth = 33.476;
    public static double dTheta = 0;
    private Pos2d posEstimate;
    private List<Double> lastWheelPositions;
    double x, y, theta;

    public Telemetry telemetry;

//function to find positions
    public InitialPosCalcs(Telemetry telemetry) {
        this.telemetry = telemetry;

        lastWheelPositions = Collections.emptyList();
        posEstimate = new Pos2d(4, 4, Math.toRadians(90));
    }

    public void update() {
        List<Double> wheelPositons = getWheelPositions();
        if(!lastWheelPositions.isEmpty()){
            double dL = wheelPositons.get(0) - lastWheelPositions.get(0);
            double dR = wheelPositons.get(1) - lastWheelPositions.get(1);
            double dC = wheelPositons.get(2) - lastWheelPositions.get(2);

            dTheta = (dL - dR)/chassisWidth;

            //find x by parts
            double xSides = (1-Math.cos(dTheta))*(dR+dL)/(2*dTheta);
            double xCenter = Math.sin(dTheta)*dC/dTheta;
            double dX = xSides + xCenter;

            //find y by parts
            double ySides = Math.sin(dTheta)*(dR+dL)/(2*dTheta);
            double yCenter = (1-Math.cos(dTheta))*dC/dTheta;
            double dY = ySides + yCenter;

            x += dX;
            y += dY;
            theta += dTheta;

            posEstimate = new Pos2d(x, y, theta);
            lastWheelPositions = getWheelPositions();
        }
        //lastWheelPositions = wheelPosiitons;
    }

    public double getDeltaX(){return x;}
    public abstract List<Double> getWheelPositions();

    public Pos2d getPosEstimate(){
        update();
        double degrees = /*3.1415 / 2 +*/ posEstimate.getHeading() * (180 / Math.PI);
        Pos2d posEstimateCorrected = new Pos2d(
                posEstimate.getX(),
                posEstimate.getY(),
                degrees
        );


        return posEstimateCorrected;
    }



    public Pos2d plus(Pos2d other) {
        return new Pos2d(x + other.getX(), y + other.getY(), theta + other.getHeading());
    }
}
