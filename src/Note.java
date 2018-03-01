import javafx.beans.property.IntegerProperty;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.ArrayList;

public class Note {

    private LEDMatrix matrix;
    private DMXChannel[] dmxChannels;

    private ArrayList<String> presetContainer;
    private String[] currentPreset;


    private float fadeIn;
    private float fadeOut;
    private float hold;
    private int id;

    private boolean endTrigger;

    public Note(int j, int strips, int ledsPerStrip, int channels) {
        id = j;
        matrix = new LEDMatrix(strips, ledsPerStrip);
        dmxChannels = new DMXChannel[channels];
        fadeIn = 0;
        fadeOut = 0;
        hold = 0;
        endTrigger = false;

        currentPreset = new String[4];
        for (int i = 0; i < currentPreset.length; i++) {
            currentPreset[i] = "None";
        }
        presetContainer = new ArrayList<>();

        for (int i = 0; i < dmxChannels.length; i++) {
            dmxChannels[i] = new DMXChannel(i);
        }


    }

    public void setEndTrigger(boolean b) {
        endTrigger = b;
    }

    public boolean getEndTrigger() {
        return endTrigger;
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

        printPresetArray();

        String[] add = p.split(";");

        if (presetContainer.size() == 0) {
            presetContainer.add(p);
        } else {
            for (int i = 0; i < presetContainer.size(); i++) {
                String temp[] = presetContainer.get(i).split(";");
                //try to replace
                if (add[0].equals(temp[0])) {
                    if (add[1].equals(temp[1])) {
                        if (add[2].equals(temp[2])) {
                            temp[3] = add[3];
                            presetContainer.remove(i);
                            presetContainer.add(p);
                            return;
                        }
                    }
                }
            }
            presetContainer.add(p);
        }

    }

    public void setCurrentPreset(String p) {
        String temp[] = p.split(";");
        int ind = Integer.parseInt(temp[0]);
        currentPreset[ind] = temp[1];
    }

    public ArrayList<String> getPresetContainer() {
        return presetContainer;
    }

    public String[] getCurrentPreset() {
        return currentPreset;
    }

    public void printPresetArray() {
        System.out.println("List for " + id);
        for (int i = 0; i < presetContainer.size(); i++) {
            System.out.println("  " + presetContainer.get(i));
        }
        System.out.println();
    }

    public void loadData(JSONObject obj) {
        matrix.loadData(obj.getJSONObject("Matrix"));
        loadPresetData(obj.getJSONObject("Presets"));
        setTimeFromString(obj.getString("Times"));
    }

    public JSONObject saveData() {
        JSONObject obj = new JSONObject();

        obj.put("Matrix", matrix.saveData());
        obj.put("Presets", savePresetData());
        obj.put("Times", getTimeString());

        return obj;
    }

    void updateSelectedColor(Color c) {
        for (int y = 0; y < matrix.getStrips(); y++) {
            for (int x = 0; x < matrix.getLedsPerStrip(); x++) {
                if (getLEDSelected(x, y)) {
                    setLED(x, y, c);
                }
            }
        }
    }

    public int getID() {
        return id;
    }

    public LEDMatrix getMatrix() {
        return matrix;
    }

    public void setDMXValFromString(String s) {
        String[] a = s.split(";");
        dmxChannels[Integer.parseInt(a[0])].setValue(Integer.parseInt(a[1]));
        boolean b = false;
        if (Integer.parseInt(a[2]) == 1) {
            b = true;
        }
        dmxChannels[Integer.parseInt(a[0])].setChecked(b);
    }

    public void setDMXTimesFromString(String s) {
        String[] a = s.split(";");
        dmxChannels[Integer.parseInt(a[0])].setStartVal(Integer.parseInt(a[1]));
        dmxChannels[Integer.parseInt(a[0])].setEndVal(Integer.parseInt(a[2]));

    }

    public DMXChannel[] getDmxValues() {
        return dmxChannels;
    }

    public int getPresetParameter(String preset, String parameter, int presetInd) {
        for (int i = 0; i < presetContainer.size(); i++) {
            String[] temp = presetContainer.get(i).split(";");

            if (Integer.parseInt(temp[0]) == presetInd && temp[1].equals(preset) && temp[2].equals(parameter)) {
                return Integer.parseInt(temp[3]);
            }
        }
        return 0;
    }

    public ArrayList<Integer> getMultiPreset() {
        ArrayList<Integer> tList = new ArrayList<>();

        for (int i = 0; i < presetContainer.size(); i++) {

            String[] temp = presetContainer.get(i).split(";");
            if (temp[0].equals("Multi") && temp[2].equals("1")) {
                tList.add(Integer.parseInt(temp[1]));
            }
        }
        return tList;
    }

    public String getPianoNoteString() {
        String noteLetters[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        return (noteLetters[id % 12]) + " " + ((id / 12) - 2);
    }

    private JSONObject savePresetData() {
        JSONObject tFile = new JSONObject();
        JSONObject tCurPresets = new JSONObject();
        JSONObject tPresetData = new JSONObject();

        for (int i = 0; i < currentPreset.length; i++) {
            tCurPresets.put(Integer.toString(i), currentPreset[i]);
        }
        for (int i = 0; i < presetContainer.size(); i++) {
            tPresetData.put(Integer.toString(i), getPresetContainer().get(i));

        }
        tFile.put("CurrentPresets", tCurPresets);
        tFile.put("PresetData", tPresetData);
        return tFile;
    }

    private void loadPresetData(JSONObject curFile) {

        JSONObject tCurPresets = curFile.getJSONObject("CurrentPresets");
        JSONObject tPresetData = curFile.getJSONObject("PresetData");

        for (int i = 0; i < currentPreset.length; i++) {
            currentPreset[i] = tCurPresets.getString(Integer.toString(i));
        }
        for (int tInc = 0; !tPresetData.isNull(Integer.toString(tInc)); tInc++) {
            setPresetProperty(tPresetData.getString(Integer.toString(tInc)));
        }
    }
}
