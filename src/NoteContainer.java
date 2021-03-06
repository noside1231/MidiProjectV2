import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 2/28/18.
 */
public class NoteContainer {

    private Note[] notes;
    private int noteAmount;

    private int strips;
    private int ledsPerStrip;
    private int dmxChannels;

    private int currentNote;

    public NoteContainer(int noteAmt, int s, int lps, int ch) {
        noteAmount = noteAmt;
        strips = s;
        ledsPerStrip = lps;
        dmxChannels = ch;
        currentNote = 0;

        notes = new Note[noteAmount];
        for (int i = 0; i < noteAmount; i++) {
            notes[i] = new Note(i, strips, ledsPerStrip, dmxChannels);
        }
    }

    public void setCurrentNoteIndex(int c) {
        if (c < 0 | c > noteAmount ) {
            return;
        }
        currentNote = c;
    }
    public int getCurrentNoteIndex() {
        return currentNote;
    }

    public Note getCurrentNote() {
        return notes[currentNote];
    }

    public Note getNote(int i) {
        return notes[i];
    }

    public void currentNoteRectangleToggled(Integer[] pair) {
        notes[currentNote].toggleSelected(pair[0], pair[1]);
    }

    public void currentNoteResetMatrix() {
        notes[currentNote].resetMatrix();
    }

    public Led[][] getCurrentNoteLEDS() {
        return notes[currentNote].getLEDS();
    }

    public DMXChannel[] getCurrentNoteDMXChannels() {
        return notes[currentNote].getDmxValues();
    }

    public DMXChannelContainer getCurrentNoteDMXContainer() {
        return notes[currentNote].getDMXContainer();
    }

    public ArrayList<String> getCurrentNotePresetContainer() {
        return notes[currentNote].getPresetContainer();
    }

    public void setCurrentNotePresetProperty(String s) {
        notes[currentNote].setPresetProperty(s);
    }

    public void setCurrentNoteCurrentPreset(String s) {
        notes[currentNote].setCurrentPreset(s);
    }

    public String[] getCurrentNoteCurrentPreset() {
        return notes[currentNote].getCurrentPreset();
    }

    public void setCurrentNoteSelectedColor(Color c) {
        notes[currentNote].updateSelectedColor(c);
    }

    public void setCurrentNoteDMXValue(String s) {
        notes[currentNote].setDMXValue(s);
    }

    public void setCurrentNoteChannelTitle(String s) {
        notes[currentNote].setDMXChannelTitle(s);
    }

    public void setCurrentNoteDMXTimes(String s) {
        notes[currentNote].setDMXTimes(s);
    }

    public void setCurrentNoteDMXSelectedChannel(int s) {
        notes[currentNote].setDMXSelectedChannel(s);
    }

    public void setCurrentNoteFadeInTime(float v) {
        notes[currentNote].setFadeIn(v);
    }
    public void setCurrentNoteHoldTime(float v) {
        notes[currentNote].setHold(v);
    }
    public void setCurrentNoteFadeOutTime(float v) {
        notes[currentNote].setFadeOut(v);
    }

    public void setMultiTriggerVal(String s) {
        notes[currentNote].setMultiTrigger(s);
    }

    public void setCurrentNoteSelectAll(boolean b) {
//        System.out.println("A"+b);
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                notes[currentNote].setSelected(x, y, b);
            }
        }
    }

    public void setCurrentNoteSelectRow(int r) {
        for (int j = 0; j < ledsPerStrip; j++) {
            notes[currentNote].setSelected(j, r, true);
        }
    }

    public void setCurrentNoteSelectColumn(int c) {
        for (int j = 0; j < strips; j++) {
            notes[currentNote].setSelected(c, j, true);
        }
    }

    public void setCurrentNoteEndTrigger(boolean b) {
        notes[currentNote].setEndTrigger(b);
    }

    public void incrementNoteIndex() {
        if (currentNote < noteAmount) {
            currentNote++;
        }
    }

    public void decrementNoteIndex() {
        if (currentNote > 0) {
            currentNote--;
        }
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        for (int i = 0; i < noteAmount; i++) {
            tFile.put(Integer.toString(i), notes[i].saveData());
        }
        return tFile;
    }

    public void loadData(JSONObject currentFile) {
        for (int i = 0; i < noteAmount; i++) {
            notes[i].loadData(currentFile.getJSONObject(Integer.toString(i)));
        }
    }


}
