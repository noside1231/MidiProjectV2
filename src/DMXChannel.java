import org.json.JSONObject;

/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXChannel {

    int index;
    int val = 0;
    int beginVal = 0;
    int endVal = 0;
    boolean checked;

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

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();

        tFile.put("value", val);
        tFile.put("beginVal", beginVal);
        tFile.put("endVal", endVal);
        tFile.put("checked", checked);

        return tFile;
    }

    public void loadData(JSONObject curFile) {

        val = curFile.getInt("value");
        beginVal = curFile.getInt("beginVal");
        endVal = curFile.getInt("endVal");
        checked = curFile.getBoolean("checked");

    }
}
