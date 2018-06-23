import org.json.JSONObject;

/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXChannel {

    private int index;
    private int val = 0;
    private int beginVal = 0;
    private int endVal = 0;
    private boolean checked;
    private String title = "";

    public DMXChannel(int ind) {
        index = ind;
        checked = false;
    }

    public void setValue(int v) {
        val = v;
    }

    public void setStartVal(int v) {
        beginVal = v;
    }

    public void setEndVal(int v) {
        endVal = v;
    }

    public void setChecked(boolean b) {
        checked = b;
    }

    public void setDMXChannelTitle(String s) {
        title = s;
    }

    public int getValue() {
        return val;
    }

    public int getStartVal() {
        return beginVal;
    }

    public int getEndVal() {
        return endVal;
    }

    public boolean getChecked() {
        return checked;
    }

    public String getTitle() {
        return title;
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();

        tFile.put("value", val);
        tFile.put("beginVal", beginVal);
        tFile.put("endVal", endVal);
        tFile.put("checked", checked);
        tFile.put("title", title);

        return tFile;
    }

    public void loadData(JSONObject curFile) {

        val = curFile.getInt("value");
        beginVal = curFile.getInt("beginVal");
        endVal = curFile.getInt("endVal");
        checked = curFile.getBoolean("checked");
        title = curFile.getString("title");
        System.out.println(title);

    }
}
