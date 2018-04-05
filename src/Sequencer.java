/**
 * Created by edisongrauman on 3/6/18.
 */
public class Sequencer {

    private int maxNoteAmount = 64;
    private int id;
    private int noteAmount = 16;
    private int channels = 8;

    int noteMapping[];
    private boolean[][] noteSelected;

    public Sequencer(int d) {
        id = d;
        noteMapping = new int[channels];
        for (int i =0; i < noteMapping.length; i++) {
            noteMapping[i] = -1;
        }

        noteSelected = new boolean[channels][maxNoteAmount];
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < noteAmount; j++) {
                noteSelected[i][j] = false;
            }
        }



    }

    public void setNoteAmount(int s) {
        noteAmount = s;
    }
    public int getNoteAmount() {
        return noteAmount;
    }

    public boolean[][] getNotes() {
        return noteSelected;
    }
    public int[] getNoteMapping() {
        return noteMapping;
    }

    public void setNoteMapping(String s) {
        String a[] = s.split(";");
        if (a.length == 2) {
            System.out.println("SEQ " + id + " POS. " + s);
            int i = Integer.parseInt(a[0]);
            int j = Integer.parseInt(a[1]);
            noteMapping[i] = j;
        }


    }

    public void setNotePressed(String s) {
        String a[] = s.split(";");

        if (a.length == 2) {
            System.out.println("SEQ " + id +" POS. " + s);
            int i = Integer.parseInt(a[0]);
            int j = Integer.parseInt(a[1]);
            noteSelected[i][j] = !noteSelected[i][j];
        }
    }

    public int getChannelAmount() {
        return channels;
    }

    public void setChannels(int i) {
        channels = i;
    }

    public int getID() {
        return id;
    }

}
