package app.mathhelper.screen.render;

import java.awt.geom.Line2D;

public class Clipper {
	private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    
    public Clipper(double xMin, double yMin, double xMax, double yMax) {
    	this.xMin = xMin;
    	this.yMin = yMin;
    	this.xMax = xMax;
    	this.yMax = yMax;
    }
	
    public Line2D.Double clip(Line2D line) {
        double u1 = 0, u2 = 1;
        double x0 = line.getX1(), y0 = line.getY1(), x1 = line.getX2(), y1 = line.getY1();
        double dx = x1 - x0, dy = y1 - y0;
        double p[] = {-dx, dx, -dy, dy};
        double q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};
        for (int i = 0; i < 4; i++) {
            if (p[i] == 0) {
                if (q[i] < 0) {
                    return null;
                }
            } else {
                double u = (double) q[i] / p[i];
                if (p[i] < 0) {
                    u1 = Math.max(u, u1);
                } else {
                    u2 = Math.min(u, u2);
                }
            }
        }
        
        if (u1 > u2) {
            return null;
        }
        double nx0, ny0, nx1, ny1;
        nx0 = (x0 + u1 * dx);
        ny0 = (y0 + u1 * dy);
        nx1 = (x0 + u2 * dx);
        ny1 = (y0 + u2 * dy);
        return new Line2D.Double(nx0, ny0, nx1, ny1);
    }
}
