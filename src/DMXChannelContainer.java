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

    public int getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(int s) {
        currentChannel = s;
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

    public void setDMXChannelTitle(String s) {
        channels[currentChannel].setDMXChannelTitle(s);
    }


    public void setDMXTimes(String s) {
        if (s.equals("")) {
            return;
        }
        String[] a = s.split(";");
        if (a[0].equals("0")) {
            channels[currentChannel].setStartVal(Integer.parseInt(a[1]));
        } else if (a[0].equals("1")) {
            channels[currentChannel].setEndVal(Integer.parseInt(a[1]));

        }
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
