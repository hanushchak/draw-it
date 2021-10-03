package DrawIt;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class allows to draw a line object. Subclass of Shape class
 *
 * @author Maksym Hanushchak
 */
public class Line extends Shape {

    /**
     * Line constructor
     *
     * @param sx        Line's x coordinate
     * @param sy        Line's y coordinate
     * @param ex        Line's height
     * @param ey        Line's width
     * @param lineColor Line's line color
     * @param lineWidth Line's line width
     */
    Line(double sx, double sy, double ex, double ey, Color lineColor, double lineWidth) {
        super(sx, sy, ex, ey, lineColor, lineWidth); // Passes parameters to the superclass
    }

    /**
     * Draws the line on the canvas
     *
     * @param gc Graphics Context
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getLineColor());
        gc.setLineWidth(getLineWidth());
        gc.strokeLine(getSx(), getSy(), getEx(), getEy());
    }
}
