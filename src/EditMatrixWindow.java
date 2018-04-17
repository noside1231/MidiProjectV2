import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 4/8/18.
 */
public class EditMatrixWindow extends VBox {

    private DisplayMatrixWindow displayMatrixWindow;
    private ColorPickerWindow colorPickerWindow;
    private MatrixPresetContainer matrixPresetContainer;
    private MultiTriggerWindow multiTriggerWindow;

    private HBox hContainer;

    private SimpleObjectProperty<Integer[]> lastMatrixRectSelected;
    private SimpleBooleanProperty editModeVal;
    private SimpleStringProperty lastContextMenuVal;
    private SimpleObjectProperty<Color> selectedColor;
    private SimpleStringProperty lastChangedPresetProperty;
    private SimpleStringProperty lastSelectedPresetValue;
    private SimpleStringProperty multiTriggerChangedVal;

    public EditMatrixWindow(int lps, int s) {

        lastMatrixRectSelected = new SimpleObjectProperty<>();
        lastContextMenuVal = new SimpleStringProperty("");
        editModeVal = new SimpleBooleanProperty(false);
        selectedColor = new SimpleObjectProperty<>();
        lastChangedPresetProperty = new SimpleStringProperty("");
        lastSelectedPresetValue = new SimpleStringProperty("");
        multiTriggerChangedVal = new SimpleStringProperty("");

        displayMatrixWindow = new DisplayMatrixWindow(lps, s);
        displayMatrixWindow.setEditMode(true);
        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));
        displayMatrixWindow.getEditModeVal().addListener(event -> setEditMode(displayMatrixWindow.getEditModeVal().get()));
        displayMatrixWindow.getLastContextMenuVal().addListener(event -> lastContextMenuVal.set(displayMatrixWindow.getLastContextMenuVal().get()));

        colorPickerWindow = new ColorPickerWindow();
        colorPickerWindow.getColor().addListener(event -> selectedColor.set(colorPickerWindow.getColor().get()));
        colorPickerWindow.getPaletteChanged().addListener(event -> {
            if (colorPickerWindow.getPaletteChanged().get()) {
                displayMatrixWindow.updateContextMenuPalette(colorPickerWindow.getTopPalette());
            }
        } );
        displayMatrixWindow.updateContextMenuPalette(colorPickerWindow.getTopPalette());


        matrixPresetContainer = new MatrixPresetContainer(lps, s);
        matrixPresetContainer.getLastChangedPresetProperty().addListener(event -> lastChangedPresetProperty.set(matrixPresetContainer.getLastChangedPresetProperty().get()));
        matrixPresetContainer.getLastSelectedPresetValue().addListener(event -> lastSelectedPresetValue.set(matrixPresetContainer.getLastSelectedPresetValue().get()));

        multiTriggerWindow = new MultiTriggerWindow();
        multiTriggerWindow.getLastChanged().addListener(event -> multiTriggerChangedVal.set(multiTriggerWindow.getLastChanged().get()));

        hContainer = new HBox(matrixPresetContainer, multiTriggerWindow);

        getChildren().addAll(displayMatrixWindow, hContainer, colorPickerWindow);


    }

    public void setScale(int w, int h) {
        displayMatrixWindow.setScale(w, h);
        colorPickerWindow.setScale();
        hContainer.setMinHeight(200);
    }

    public void setLEDDisplay(Led[][] leds) {
        displayMatrixWindow.setLEDS(leds);
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
        displayMatrixWindow.setEditMode(t);
    }

    public SimpleStringProperty getLastChangedPresetProperty() {
        return lastChangedPresetProperty;
    }

    public SimpleStringProperty getLastSelectedPresetValue() {
        return lastSelectedPresetValue;
    }

    public SimpleObjectProperty<Integer[]> getLastRectPressed() {
        return lastMatrixRectSelected;
    }

    private void setLastPressed(Integer[] p) {
        lastMatrixRectSelected.set(p);
    }

    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }

    public SimpleStringProperty getLastContextMenuVal() {
        return lastContextMenuVal;
    }

    public SimpleObjectProperty<Color> getSelectedColor() {
        return selectedColor;
    }

    public void setColorFromPalette() {
        colorPickerWindow.setColor();
    }

    public void setColorFromPalette(int i) {
        colorPickerWindow.setColor(i);
    }

    public void setMatrixPresets(ArrayList<String> pText, String[] curPreset, int curNote) {
        matrixPresetContainer.setPresetDisplay(pText, curPreset, curNote);
    }

    public int getCurrentDisplayedPresetInd() {
        return matrixPresetContainer.getCurrentlyDisplayingNote();
    }

    public SimpleStringProperty getMultiTriggerChangedVal() {
        return multiTriggerChangedVal;
    }

    public void resetMultiValues(int id, boolean[] v) {
        multiTriggerWindow.resetFields(id, v);
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        tFile.put("ColorPicker", colorPickerWindow.saveData());
        return tFile;
    }

    public void loadData(JSONObject tFile) {
        colorPickerWindow.loadData(tFile.getJSONObject("ColorPicker"));
        displayMatrixWindow.updateContextMenuPalette(colorPickerWindow.getTopPalette());

    }


    }
