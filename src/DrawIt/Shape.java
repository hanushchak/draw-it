package DrawIt;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Shape abstract class. Superclass that contains common values shared with Line, Rectangle and Circle classes
 *
 * @author Maksym Hanushchak
 */
public abstract class Shape {
    /**
     * Shape's coordinates (pixels)
     **/
    private double sx, sy, ex, ey;
    /**
     * Shape's stroke line color
     */
    private Color lineColor;
    /**
     * Shape's stroke line width
     */
    private double lineWidth;

    /**
     * Shape constructor
     *
     * @param sx        Shape's x coordinate
     * @param sy        Shape's y coordinate
     * @param ex        Shape's height
     * @param ey        Shape's width
     * @param lineColor Shape's line color
     * @param lineWidth Shape's line width
     */
    Shape(Double sx, Double sy, Double ex, Double ey, Color lineColor, Double lineWidth) {
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
        this.lineColor = lineColor;
        this.lineWidth = lineWidth;
    }


    /**
     * Setter method for shape's line color
     *
     * @param lineColor Shape's line color
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }


    /**
     * Getter method for shape's x coordinate
     *
     * @return X coordinate
     */
    public double getSx() {
        return sx;
    }

    /**
     * Getter method for shape's y coordinate
     *
     * @return Y coordinate
     */
    public double getSy() {
        return sy;
    }

    /**
     * Getter method for shape's height
     *
     * @return Height
     */
    public double getEx() {
        return ex;
    }

    /**
     * Getter method for shape's width
     *
     * @return Width
     */
    public double getEy() {
        return ey;
    }

    /**
     * Getter method for shape's line color
     *
     * @return Stroke line color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Getter method for shape's line width
     *
     * @return Stroke line width
     */
    public double getLineWidth() {
        return lineWidth;
    }

    /**
     * abstact draw method
     *
     * @param gc Graphics Context
     */
    public abstract void draw(GraphicsContext gc);
}
