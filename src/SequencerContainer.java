import org.json.JSONObject;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by edisongrauman on 3/6/18.
 */
public class SequencerContainer {


    private LinkedList<Sequencer> sequencers;

    private int currentSequencer = 0;

    private int lastAssignedID = 1;

    private String defaultName = "UNNAMED ";

    public SequencerContainer() {
        sequencers = new LinkedList<>();
//        sequencers.add(new Sequencer(lastAssignedID, defaultName+lastAssignedID));
//        setCurrentSequencer(defaultName+lastAssignedID);
//        lastAssignedID++;


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
    System.out.println("SETCURRENTSEQUENCER");
        ListIterator<Sequencer> iterator = sequencers.listIterator();

        int ind = 0;
        while (iterator.hasNext()) {
            Sequencer tSequencer = iterator.next();
//            System.out.println(ind + " " + tSequencer.getName() + "   " + s);
            if (tSequencer.getName().equals(s)) {
                currentSequencer = ind;
                System.out.println("!!!"+currentSequencer + " " + tSequencer.getName());
                return;
            }
            ind++;
        }
    }

    public void setCurrentSequencerNotePressed(String s) {
        sequencers.get(currentSequencer).setNotePressed(s);
    }

    public void setCurrentSequencerActiveChannel(String s) {
        sequencers.get(currentSequencer).setActiveChannel(s);
    }

    public void addSequencer() {
        System.out.println("ADDSEQUENCER");
        sequencers.add(new Sequencer(lastAssignedID, "UNNAMED"+Integer.toString(lastAssignedID)));
        setCurrentSequencer("UNNAMED"+Integer.toString(lastAssignedID));
        lastAssignedID++;
    }

    public void removeSequencer() {
        sequencers.remove(currentSequencer);
    }

    public LinkedList<Sequencer> getSequencers() {
        return sequencers;
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
            addSequencer();
            sequencers.get(currentSequencer).loadData(tFile.getJSONObject(Integer.toString(i)));
        }


    }
}
