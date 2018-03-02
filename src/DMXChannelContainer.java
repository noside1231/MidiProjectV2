import org.json.JSONObject;

import javax.xml.bind.util.JAXBSource;

/**
 * Created by edisongrauman on 3/1/18.
 */
public class DMXChannelContainer {

    DMXChannel[] channels;
    int currentChannel;

    public DMXChannelContainer(int amt) {
        currentChannel = 0;

        channels = new DMXChannel[amt];

        for (int i = 0; i < amt; i++) {
        channels[i] = new DMXChannel(i);
        }
    }

    public DMXChannel[] getDMXChannels() {
        return channels;
    }

    public void setDMXValue(String s) {
        String[] a = s.split(";");
        channels[Integer.parseInt(a[0])].setValue(Integer.parseInt(a[1]));
        boolean b = false;
        if (Integer.parseInt(a[2]) == 1) {
            b = true;
        }
        channels[Integer.parseInt(a[0])].setChecked(b);
    }

    public void setDMXTimes(String s) {
        String[] a = s.split(";");
        channels[Integer.parseInt(a[0])].setStartVal(Integer.parseInt(a[1]));
        channels[Integer.parseInt(a[0])].setEndVal(Integer.parseInt(a[2]));
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();

        for (int i = 0; i < channels.length; i++) {
            tFile.put(Integer.toString(i), channels[i].saveData());
        }
        return  tFile;
    }

    public void loadData(JSONObject curFile) {
        for (int i = 0; i < channels.length; i++) {
            channels[i].loadData(curFile.getJSONObject(Integer.toString(i)));
        }
    }
}
