package org.firstinspires.ftc.teamcode.odometry;

public class Pos2d {
    private double x, y, heading;

    public Pos2d(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    /**
     * @return x value of the vector
     */
    public double getX() {
        return x;
    }

    /**
     * @return y value of the vector
     */
    public double getY() {
        return y;
    }

    /**
     * @return heading value of the vector
     */
    public double getHeading() {
        return heading;
    }
}
