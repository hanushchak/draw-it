package DrawIt;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class allows to draw a circle object. Subclass of Shape class
 *
 * @author Maksym Hanushchak
 */
public class Circle extends Shape {
    /**
     * Circle constructor
     *
     * @param sx        Circle's x coordinate
     * @param sy        Circle's y coordinate
     * @param ex        Circle's height
     * @param ey        Circle's width
     * @param lineColor Circle's line color
     * @param lineWidth Circle's line width
     */
    Circle(double sx, double sy, double ex, double ey, Color lineColor, double lineWidth) {
        super(sx, sy, ex, ey, lineColor, lineWidth); // Passes parameters to the superclass
    }

    /**
     * Draws the circle on the canvas. Inverts the coordinates when the condition is followed - to be able to draw in any direction
     *
     * @param gc Graphics Context
     */
    @Override
    public void draw(GraphicsContext gc) {

        double x, y, w, h; // Local variables for X, Y, Width, Height of the Circle

        if (getSx() >= getEx()) {  // Left corner
            x = getEx();
            w = getSx() - getEx();
        } else {          // Left corner
            x = getSx();
            w = getEx() - (getSx());
        }
        if (getSy() >= getEy()) {  // Top corner
            y = getEy();
            h = getSy() - getEy();
        } else {          // Top corner
            y = getSy();
            h = getEy() - getSy();
        }

        gc.setStroke(getLineColor());
        gc.setLineWidth(getLineWidth());
        gc.strokeOval(x, y, w, h);
    }
}
