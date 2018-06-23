import org.json.JSONObject;

/**
 * Created by edisongrauman on 3/6/18.
 */
public class Sequencer {

    private int maxNoteAmount = 64;
    private int id;
    private int noteAmount = 16;
    private int channels = 8;

    private String name;

    int noteMapping[];
    boolean activeChannels[];
    private boolean[][] noteSelected;

    public Sequencer(int ind, String n) {
        id = ind;
        name = n;
        noteMapping = new int[channels];
        for (int i =0; i < noteMapping.length; i++) {
            noteMapping[i] = -1;
        }

        activeChannels = new boolean[channels];
        for (int i = 0; i < channels; i++) {
            activeChannels[i] = true;
        }

        noteSelected = new boolean[channels][maxNoteAmount];
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < noteAmount; j++) {
                noteSelected[i][j] = false;
            }
        }

    }

    public void setActiveChannel(String s) {
        String a[] = s.split(";");
        if (a.length == 2) {

            int i = Integer.parseInt(a[0]);
            boolean j = Boolean.parseBoolean(a[1]);

            activeChannels[i] = j;

        }
    }

    public void setName(String s) {
        name = s;
    }

    public String getName() {
        return name;
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
    public boolean[] getActiveChannels() {
        return activeChannels;
    }

    public void setNoteMapping(String s) {
        String a[] = s.split(";");
        if (a.length == 2) {
            int i = Integer.parseInt(a[0]);
            int j = Integer.parseInt(a[1]);
            noteMapping[i] = j;
        }
    }

    public void setNotePressed(String s) {
        String a[] = s.split(";");

        if (a.length == 2) {
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

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        JSONObject rFile;
        JSONObject cFile;

        rFile = new JSONObject();
        for(int i = 0; i < activeChannels.length; i++) {
            rFile.put(Integer.toString(i), activeChannels[i]);
        }
        tFile.put("Active Channels", rFile);

        rFile = new JSONObject();
        for (int i = 0; i < noteMapping.length; i++) {
            rFile.put(Integer.toString(i), noteMapping[i]);
        }
        tFile.put("Note Mapping", rFile);

        rFile = new JSONObject();
        for (int i = 0; i < channels; i++) {
            cFile = new JSONObject();
            for (int j = 0; j < maxNoteAmount; j++) {
                cFile.put(Integer.toString(j), noteSelected[i][j]);
            }
            rFile.put(Integer.toString(i), cFile);
        }
        tFile.put("Note Selection", rFile);
        tFile.put("Note Amount", noteAmount);

        return tFile;
    }

    public void loadData(JSONObject tFile) {
        noteAmount = tFile.getInt("Note Amount");

        for (int i = 0; i < activeChannels.length; i++) {
            activeChannels[i] = tFile.getJSONObject("Active Channels").getBoolean(Integer.toString(i));
        }

        for (int i = 0; i < noteMapping.length; i++) {
            noteMapping[i] = tFile.getJSONObject("Note Mapping").getInt(Integer.toString(i));
        }

        JSONObject rFile = tFile.getJSONObject("Note Selection");

        for (int i = 0; i < channels; i++) {
            JSONObject cFile = rFile.getJSONObject(Integer.toString(i));
            for (int j = 0; j < maxNoteAmount; j++) {
                noteSelected[i][j] = cFile.getBoolean(Integer.toString(j));
            }
        }



    }

}
