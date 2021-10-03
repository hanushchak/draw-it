package DrawIt;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * DrawIt v.1.0
 * <p>
 * Main class (view) of the drawing application
 *
 * @author Maksym Hanushchak
 */
public class DrawIt extends Application {

    /**
     * Screen width in pixels
     **/
    private final double SCREEN_WIDTH = 1280;
    /**
     * Screen height in pixels
     **/
    private final double SCREEN_HEIGHT = 800;
    /**
     * Control panel width in pixels
     **/
    private final double CONTROL_WIDTH = 200;
    /**
     * Default canvas color
     **/
    private final Color defaultCanvasColor = Color.WHITE;
    /**
     * Default drawing color
     **/
    private final Color defaultColor = Color.BLACK;
    /**
     * Mouse press/release coordinates variables' declaration
     **/
    private double sx, sy, ex, ey;
    /**
     * Line width variable declaration
     **/
    private double lineWidth;
    /**
     * Declaration of canvases (main and transparent)
     **/
    private GraphicsContext gc, gcTransparent;
    /**
     * Line width text field declaration
     **/
    private TextField lineWidthField;
    /**
     * Color picker declaration
     **/
    private ColorPicker colorPicker;
    /**
     * Undo button declaration
     **/
    private Button undoButton;
    /**
     * Redo button declaration
     **/
    private Button redoButton;
    /**
     * Declaration of variable that represents selected tool (1 - 'line' by default)
     **/
    private int selectedTool = 1;
    /**
     * Declaration of the main array of shapes
     **/
    private ArrayList<Shape> shapes;
    /**
     * Declaration of the array that holds shapes that has been undone
     **/
    private ArrayList<Shape> undoShapes;

    /**
     * This method clears main canvas when called
     */
    private void clearMainCanvas() {
        gc.setFill(defaultCanvasColor);
        gc.fillRect(0, 0, SCREEN_WIDTH - CONTROL_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * This method clears transparent canvas when called
     */
    private void clearTransparentCanvas() {
        gcTransparent.clearRect(0, 0, SCREEN_WIDTH - CONTROL_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Checks conditions and Disables/Enables undo and redo buttons when called
     */
    private void undoRedoCheck() {
        if (shapes.isEmpty()) {
            undoButton.setDisable(true);
        } else {
            undoButton.setDisable(false);
        }
        if (undoShapes.isEmpty()) {
            redoButton.setDisable(true);
        } else {
            redoButton.setDisable(false);
        }
    }


    /**
     * Undo method. Allows to remove last shape from the canvas
     *
     * @param actionEvent Unused
     */
    private void undoButtonHandler(ActionEvent actionEvent) {
        undoShapes.add(shapes.get(shapes.size() - 1)); // Adds the shape that is about to be removed to the array of undone objects (to be able to redo them later)
        shapes.remove(shapes.size() - 1); // Removes the shape from the main array
        clearMainCanvas();
        for (Shape s : shapes) {
            s.draw(gc);
        }
        undoRedoCheck();
    }

    /**
     * Resets the application to default settings
     *
     * @param actionEvent Unused
     */
    private void resetAll(ActionEvent actionEvent) {
        clearMainCanvas();
        undoShapes.clear();
        shapes.clear();
        undoRedoCheck();
        lineWidthField.setText("2");
    }

    /**
     * Redo method. Allows to restore undone objects and draw them on the canvas
     *
     * @param actionEvent Unused
     */
    private void redoButtonHandler(ActionEvent actionEvent) {
        shapes.add(undoShapes.get(undoShapes.size() - 1)); // Adds the shape from the array of undone objects to the main array
        undoShapes.remove(undoShapes.size() - 1); // Deletes the shape from the array of undone objects
        clearMainCanvas();
        for (Shape s : shapes) {
            s.draw(gc);
        }
        undoRedoCheck();
    }


    /**
     * Allows to reset color of all the shapes on the canvas to selected color
     *
     * @param actionEvent Unused
     */
    private void resetColorButtonHandler(ActionEvent actionEvent) {
        // Update the array
        for (Shape s : shapes) {
            s.setLineColor(colorPicker.getValue());
        }
        // Update the canvas
        clearMainCanvas();
        for (Shape s : shapes) {
            s.draw(gc);
        }
    }

    /**
     * Called when mouse button is pressed
     *
     * @param mouseEvent Gets coordinates of the spot where the button was pressed
     */
    private void pressHandler(MouseEvent mouseEvent) {

        // Error handling
        try { // Try to convert the value from the textfield to double
            lineWidth = Double.parseDouble(lineWidthField.getText());
        } catch (NumberFormatException e) { // Catch format exception
            new Alert(Alert.AlertType.ERROR, "Stroke Width field only accepts numbers").showAndWait(); // Show the error alert
            lineWidth = 2; // Set line width to default
        }
        if ((lineWidth < 1) || (lineWidth > 200)) { // If out of range
            new Alert(Alert.AlertType.ERROR, "Number in Stroke Width field is out or range (1 - 200)").showAndWait(); // Show 'out of range alert'
        }

        sx = mouseEvent.getX();
        sy = mouseEvent.getY();
    }

    /**
     * Called when mouse is dragged with pressed down button
     *
     * @param mouseEvent Gets coordinates of current location of the cursor
     */
    private void dragHandler(MouseEvent mouseEvent) {

        clearTransparentCanvas();
        ex = mouseEvent.getX();
        ey = mouseEvent.getY();

        switch (selectedTool) { // Draws on transparent canvas
            case 1:
                Shape l = new Line(sx, sy, ex, ey, colorPicker.getValue(), lineWidth);
                l.draw(gcTransparent);
                break;
            case 2:
                Shape c = new Circle(sx, sy, ex, ey, colorPicker.getValue(), lineWidth);
                c.draw(gcTransparent);
                break;
            case 3:
                Shape r = new Rectangle(sx, sy, ex, ey, colorPicker.getValue(), lineWidth);
                r.draw(gcTransparent);
                break;
        }
    }


    /**
     * Called when mouse button is released
     *
     * @param mouseEvent Gets coordinates of the spot where the button was released
     */
    private void releaseHandler(MouseEvent mouseEvent) { // draws on background
        clearTransparentCanvas();
        ex = mouseEvent.getX();
        ey = mouseEvent.getY();

        if ((ex > SCREEN_WIDTH - CONTROL_WIDTH) || (ey > SCREEN_HEIGHT) || (ex < 0) || (ey < 0)) { // Checks if the shape that is being drawn is out of the canvas area
            new Alert(Alert.AlertType.WARNING, "Cannot draw outside of the workspace").showAndWait();
        } else { // If range condition is followed, creates a shape and adds it to the main array
            switch (selectedTool) {
                case 1:
                    Shape s = new Line(sx, sy, ex, ey, colorPicker.getValue(), lineWidth);
                    shapes.add(s);
                    break;
                case 2:
                    Shape c = new Circle(sx, sy, ex, ey, colorPicker.getValue(), lineWidth);
                    shapes.add(c);
                    break;
                case 3:
                    Shape r = new Rectangle(sx, sy, ex, ey, colorPicker.getValue(), lineWidth);
                    shapes.add(r);
                    break;
            }
        }

        // Draws all the objects from the main array on the screen
        gc.setFill(defaultCanvasColor);
        for (Shape s : shapes) {
            s.draw(gc);
        }

        undoRedoCheck();
    }


    //private void pencilButtonHandler(ActionEvent actionEvent) {}

    /**
     * Selects drawing tool "line"
     *
     * @param actionEvent Unused
     */
    private void lineButtonHandler(ActionEvent actionEvent) {
        selectedTool = 1;
    }

    /**
     * Selects drawing tool "Circle"
     *
     * @param actionEvent Unused
     */
    private void circleButtonHandler(ActionEvent actionEvent) {
        selectedTool = 2;
    }

    /**
     * Selects drawing tool "Rectangle"
     *
     * @param actionEvent Unused
     */
    private void rectangleButtonHandler(ActionEvent actionEvent) {
        selectedTool = 3;
    }

    //private void eraserButtonHandler(ActionEvent actionEvent) {}


    /**
     * Start method contains view components and event handlers
     *
     * @param stage The main stage
     * @throws Exception Unused
     */
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, Color.DARKGRAY); // Creates and sets main scene parameters
        stage.setTitle("DrawIt!"); // Window title
        stage.setScene(scene);
        stage.setResizable(false); // Denies to resize the window
        stage.sizeToScene(); // Sets stage size to scene size
        stage.getIcons().add(new Image("DrawIt/icon.png")); // Sets application icon

        // Created main and undo arrays
        shapes = new ArrayList<>();
        undoShapes = new ArrayList<>();

        // Created GUI components and set their parameters
        Canvas mainCanvas = new Canvas(SCREEN_WIDTH - CONTROL_WIDTH, SCREEN_HEIGHT); // Main canvas
        Canvas transparentCanvas = new Canvas(SCREEN_WIDTH - CONTROL_WIDTH, SCREEN_HEIGHT); // Transparent canvas
        transparentCanvas.setCursor(Cursor.CROSSHAIR); // Sets custom cursor on transparent canvas

        // JavaFX controls on left-sided control panel
        Label titleLabel = new Label("DrawIt!");
        titleLabel.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 20);
        titleLabel.setTextFill(Color.DARKGRAY);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        Label instrumentsLabel = new Label("Drawing Instruments");
        instrumentsLabel.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 100);

        Button pencilButton = new Button("Ԇ Pencil");
        pencilButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 130);
        pencilButton.setPrefWidth(CONTROL_WIDTH - 40);
        pencilButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pencilButton.setDisable(true);

        Button lineButton = new Button("¦ Line");
        lineButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 165);
        lineButton.setPrefWidth(CONTROL_WIDTH - 40);
        lineButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button circleButton = new Button("○ Circle");
        circleButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 200);
        circleButton.setPrefWidth(CONTROL_WIDTH - 40);
        circleButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button rectangleButton = new Button("□ Rectangle");
        rectangleButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 235);
        rectangleButton.setPrefWidth(CONTROL_WIDTH - 40);
        rectangleButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button eraserButton = new Button("⌫ Eraser");
        eraserButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 300);
        eraserButton.setPrefWidth(CONTROL_WIDTH - 40);
        eraserButton.setDisable(true);

        undoButton = new Button("< Undo");
        undoButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 335);
        undoButton.setPrefWidth(CONTROL_WIDTH / 2 - 25);
        undoButton.setDisable(true);

        redoButton = new Button("Redo >");
        redoButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + (CONTROL_WIDTH - (CONTROL_WIDTH / 2 - 25) - 20), 335);
        redoButton.setPrefWidth(CONTROL_WIDTH / 2 - 25);
        redoButton.setDisable(true);

        colorPicker = new ColorPicker(defaultColor);
        colorPicker.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 400);
        colorPicker.setPrefWidth(CONTROL_WIDTH - 40);

        Button resetColorButton = new Button("Reset Color");
        resetColorButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 435);
        resetColorButton.setPrefWidth(CONTROL_WIDTH - 40);

        Button resetCanvasButton = new Button("Reset All");
        resetCanvasButton.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 470);
        resetCanvasButton.setPrefWidth(CONTROL_WIDTH - 40);


        Label lineWidthLabel = new Label("Stroke Width:");
        lineWidthLabel.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 530);
        lineWidthLabel.setPrefWidth(CONTROL_WIDTH / 2 - 25);

        Label widthRangeLabel = new Label("        (1 - 200)");
        widthRangeLabel.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, 545);
        widthRangeLabel.setPrefWidth(CONTROL_WIDTH / 2 - 25);
        widthRangeLabel.setTextFill(Color.DARKGRAY);

        lineWidthField = new TextField("2");
        lineWidthField.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + (CONTROL_WIDTH - (CONTROL_WIDTH / 2 - 25) - 20), 532);
        lineWidthField.setPrefWidth(CONTROL_WIDTH / 2 - 25);
        lineWidthField.setPrefHeight(30);

        Label infoLabel = new Label("* Pencil and Eraser tools\n   will be implemented in the\n   next version");
        infoLabel.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, SCREEN_HEIGHT - 100);
        infoLabel.setFont(Font.font("Arial", 12));
        infoLabel.setTextFill(Color.DARKGRAY);


        Label copyrightLabel = new Label("© Maksym Hanushchak");
        copyrightLabel.relocate((SCREEN_WIDTH - CONTROL_WIDTH) + 20, SCREEN_HEIGHT - 30);
        copyrightLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        copyrightLabel.setTextFill(Color.DARKGRAY);


        // Add components to the root
        root.getChildren().addAll(mainCanvas, transparentCanvas, titleLabel, instrumentsLabel, pencilButton, lineButton, circleButton, rectangleButton, colorPicker, resetColorButton, undoButton, redoButton, eraserButton, resetCanvasButton, lineWidthLabel, lineWidthField, widthRangeLabel, copyrightLabel, infoLabel);

        // Create the two graphics contexts
        gc = mainCanvas.getGraphicsContext2D();
        gcTransparent = transparentCanvas.getGraphicsContext2D();

        // Clear main canvas
        clearMainCanvas();

        // Add Event Handlers
        transparentCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::pressHandler);
        transparentCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::releaseHandler);
        transparentCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::dragHandler);
        //pencilButton.setOnAction(this::pencilButtonHandler);
        lineButton.setOnAction(this::lineButtonHandler);
        circleButton.setOnAction(this::circleButtonHandler);
        rectangleButton.setOnAction(this::rectangleButtonHandler);
        //eraserButton.setOnAction(this::eraserButtonHandler);
        undoButton.setOnAction(this::undoButtonHandler);
        redoButton.setOnAction(this::redoButtonHandler);
        resetColorButton.setOnAction(this::resetColorButtonHandler);
        resetCanvasButton.setOnAction(this::resetAll);

        // Show the stage
        stage.show();
    }

    /**
     * Main method that launches the application
     *
     * @param args Unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
