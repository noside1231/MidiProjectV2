import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 3/6/18.
 */
public class SequencerContainer {

    private ArrayList<Sequencer> sequencers;

    private int currentSequencer = 0;


    public SequencerContainer() {
        sequencers = new ArrayList<>();
        sequencers.add(new Sequencer(0));

    }

    public Sequencer getCurrentSequencer() {
        return sequencers.get(currentSequencer);
    }

    public void setCurrentSequencerNoteAmount(int i) {
        sequencers.get(currentSequencer).setNoteAmount(i);
    }

    public int getCurrentSequencerNoteAmount() {
        return sequencers.get(currentSequencer).getNoteAmount();
    }
    public void setCurrentSequencerNoteMapping(String s) {
        sequencers.get(currentSequencer).setNoteMapping(s);
    }

    public void setCurrentSequencer(String s) {
        currentSequencer = Integer.parseInt(s)-1;
    }

    public void setCurrentSequencerNotePressed(String s) {

        sequencers.get(currentSequencer).setNotePressed(s);
    }

    public void setCurrentSequencerActiveChannel(String s) {
        sequencers.get(currentSequencer).setActiveChannel(s);
    }

    public void addSequencer(String s) {
        sequencers.add(new Sequencer(Integer.parseInt(s)));
        setCurrentSequencer(s);
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();

        int i;
        for (i = 0; i < sequencers.size(); i++) {
            tFile.put(Integer.toString(i), sequencers.get(i).saveData());
        }
        tFile.put("amt", i);

        return tFile;
    }

    public void loadData(JSONObject tFile) {

        sequencers.get(currentSequencer).loadData(tFile.getJSONObject("0"));

        for (int i = 1; i < tFile.getInt("amt"); i++) {
            addSequencer(Integer.toString(i+1));
            sequencers.get(currentSequencer).loadData(tFile.getJSONObject(Integer.toString(i)));
        }


    }
}
