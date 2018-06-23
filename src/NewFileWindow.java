import Utilities.SliderTextField;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by edisongrauman on 4/6/18.
 */
public class NewFileWindow extends TextInputDialog {

    private VBox rootBox;
    private HBox stripContainer;
    private SliderTextField ledsPerStripField;
    private SliderTextField stripsField;

    private int defaultLedsPerStrip = 30;
    private int defaultStrips = 5;


    public NewFileWindow(Stage owner) {

        rootBox = new VBox();

        setTitle("New File");
        setHeaderText("Enter Strip Sizes");

        ledsPerStripField = new SliderTextField(defaultLedsPerStrip, 1, 200, "Leds Per Strip");
        stripsField = new SliderTextField(defaultStrips, 1, 100, "Strips");
        stripContainer = new HBox(stripsField, ledsPerStripField);

        rootBox.getChildren().add(stripContainer);
        getDialogPane().setContent(rootBox);
        initStyle(StageStyle.UNDECORATED);
        initOwner(owner);

        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {
                return Integer.toString(ledsPerStripField.getValue().get())+";"+Integer.toString(stripsField.getValue().get());
            }
            return "";
        });

    }

    public Optional<String> showNewFileWindow() {
        ledsPerStripField.setValue(defaultLedsPerStrip);
        stripsField.setValue(defaultStrips);
        return showAndWait();
    }

}
