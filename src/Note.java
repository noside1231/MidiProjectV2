import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Note {

    LEDMatrix matrix;

    ArrayList<String> presetContainer;
    String currentPreset;


    float fadeIn;
    float fadeOut;
    float hold;
    int id;

    public Note(int j) {
        id = j;
        matrix = new LEDMatrix();
        fadeIn = 0;
        fadeOut = 0;
        hold = 0;

        currentPreset = "None";
        presetContainer = new ArrayList<>();


    }

    void setLED(int x, int y, Color c) {
        matrix.setLED(x, y, c);
    }

    Color getLED(int x, int y) {
        return matrix.getLED(x, y);
    }

    Led[][] getLEDS() {
        return matrix.getLEDS();
    }

    String getLEDString(int x, int y) {
        return matrix.getLEDString(x, y);
    }

    boolean getLEDSelected(int x, int y) {
        return matrix.getLEDSelected(x, y);
    }

    void toggleSelected(int x, int y) {
        matrix.toggleSelected(x, y);
    }

    void resetMatrix() {
        matrix.reset();
    }

    void setFadeIn(float f) {
        fadeIn = f;
    }

    void setFadeOut(float f) {
        fadeOut = f;
    }

    void setHold(float f) {
        hold = f;
    }

    String getTimeString() {
        return Float.toString(fadeIn) + ";" + Float.toString(hold) + ";" + Float.toString(fadeOut);
    }

    void setTimeFromString(String s) {
        String[] a = s.split(";");
//        System.out.println(a[0]);
        fadeIn = Float.parseFloat(a[0]);
        hold = Float.parseFloat(a[1]);
        fadeOut = Float.parseFloat(a[2]);
    }

    public float getFadeIn() {
        return fadeIn;
    }

    public float getFadeOut() {
        return fadeOut;
    }

    public float getHold() {
        return hold;
    }

    public void setSelected(int x, int y, boolean s) {
        matrix.setSelected(x, y, s);
    }

    public void setPresetProperty(String p) {

        String[] add = p.split(";");

        if (presetContainer.size() == 0) {
            presetContainer.add(p);
        } else {
            for (int i = 0; i < presetContainer.size(); i++) {
                String temp[] = presetContainer.get(i).split(";");
                //try to replace
                if (add[0].equals(temp[0])) {
                    if (add[1].equals(temp[1])) {
                        temp[2] = add[2];
                        presetContainer.remove(i);
                        presetContainer.add(p);
                        return;
                    }
                }
            }
            presetContainer.add(p);
        }

    }

    public void setCurrentPreset(String p) {
        currentPreset = p;
    }

    public ArrayList<String> getPresetContainer() {
        return presetContainer;
    }

    public String getCurrentPreset() {
        return currentPreset;
    }

    public void printPresetArray() {
        System.out.println("List for " + id);
        for (int i = 0; i < presetContainer.size(); i++) {
            System.out.println("  "+presetContainer.get(i));
        }
        System.out.println();
    }
}