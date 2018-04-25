import Utilities.SliderTextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by edisongrauman on 4/25/18.
 */
public class HelpWindow extends DialogPane{
    private VBox rootBox;

    private Label tLbl;

    public HelpWindow() {
        rootBox = new VBox();

        setContent(rootBox);
//        initStyle(StageStyle.UNDECORATED);

        tLbl = new Label("TEST");
        rootBox.getChildren().add(tLbl);
    }

    public void showHelpWindow() {
//        showAndWait();
    }


}
