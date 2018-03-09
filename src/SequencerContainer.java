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
    public void setCurrentSequencerNoteMapping() {
        sequencers.get(currentSequencer).setNoteAmount(1);
    }
    public void setCurrentSequencer(String s) {
        currentSequencer = Integer.parseInt(s)-1;
    }

    public void setCurrentSequencerNotePressed(String s) {
        sequencers.get(currentSequencer).setNotePressed(s);
    }


}
