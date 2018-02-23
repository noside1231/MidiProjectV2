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
    public int getstartVal() {
        return beginVal;
    }
    public  int getEndVal() {
        return endVal;
    }
    public boolean getChecked() {
        return checked;
    }
}
