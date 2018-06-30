import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by edisongrauman on 6/29/18.
 */
public class EditNoteMapWindow extends TextInputDialog {

    private VBox rootBox;

    private TextField nameField;
    private ComboBox<String> valField;


    public EditNoteMapWindow(Stage owner) {

        nameField = new TextField();
        valField = new ComboBox<>();

        for (int i = 0; i < 128; i++) {
            valField.getItems().add(Integer.toString(i+1));
        }

        rootBox = new VBox(nameField, valField);
        getDialogPane().setContent(rootBox);
        initStyle(StageStyle.UNDECORATED);
//        initOwner(owner);


        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {
                return nameField.getText()+";"+valField.getValue();
            }
            return "";
        });
    }

    public Optional<String> showEditNoteMapWindow(String name, String val) {
        nameField.setText(name);
        valField.setValue(val);


        return showAndWait();
    }


}
