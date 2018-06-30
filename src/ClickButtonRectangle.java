import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Created by edisongrauman on 6/29/18.
 */
public class ClickButtonRectangle extends StackPane {

    private String name = "Test";
    private SimpleStringProperty val;
    private SimpleStringProperty triggerNote;

    private Rectangle rectangle;
    private Text displayInfo;


    private ContextMenu contextMenu;
    private MenuItem editItem;

    private EditNoteMapWindow editNoteMapWindow;


    public ClickButtonRectangle(Stage owner) {

        editNoteMapWindow = new EditNoteMapWindow(owner);

        val = new SimpleStringProperty("1");
        triggerNote = new SimpleStringProperty("");

        rectangle = new Rectangle(50, 50, Color.LIGHTGREY);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);

        displayInfo = new Text();
        updateValues();
        displayInfo.setTextAlignment(TextAlignment.CENTER);

        editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> showEditNoteMapWindow());
        contextMenu = new ContextMenu(editItem);


        getChildren().addAll(rectangle, displayInfo);

        setOnMousePressed(event -> rectangle.setFill(Color.DARKGRAY));
        setOnMouseReleased(event -> rectangle.setFill(Color.LIGHTGREY));
        displayInfo.setOnMouseClicked(event -> mousePressed(event));

        setOnMouseClicked(event -> contextMenu.hide());


    }

    private void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.SECONDARY) {
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        }
        else {
            triggerNote.set(Integer.toString(Integer.parseInt(val.get())-1));
            triggerNote.set("");
        }
    }

    public SimpleStringProperty getTriggerNote() {
        return triggerNote;
    }

    private void showEditNoteMapWindow() {


        String a = editNoteMapWindow.showEditNoteMapWindow(name, val.get()).get();
        if (!a.isEmpty()) {
            String[] s = a.split(";");
            name = s[0];
            val.set(s[1]);
            updateValues();

        }
    }

    public SimpleStringProperty getVal() {
        return val;
    }

    private void updateValues() {
        displayInfo.setText(name+'\n'+val.get());
    }


}
