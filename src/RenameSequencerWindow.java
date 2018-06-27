import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by edisongrauman on 6/26/18.
 */
public class RenameSequencerWindow extends TextInputDialog {

    private VBox rootBox;
    private TextField input;

    public RenameSequencerWindow(Stage owner) {

        rootBox = new VBox();
        input = new TextField();

        setTitle("");
        setHeaderText("Rename Sequencer");

        rootBox.getChildren().add(input);
        getDialogPane().setContent(rootBox);
        initStyle(StageStyle.UNDECORATED);
//        initOwner(owner); //fix

        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {
                return input.getText();
            } else {
                return "";
            }
        });

    }

    public Optional<String> showRenameSequencerWindow() {
        return showAndWait();
    }


}
