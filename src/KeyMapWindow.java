import Utilities.KeyMap;
import javafx.scene.layout.HBox;

/**
 * Created by edisongrauman on 2/25/18.
 */
public class KeyMapWindow extends HBox {

    private String[] keys = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
    private KeyMap[] keyMaps;

    public KeyMapWindow() {

        keyMaps = new KeyMap[keys.length];
        for (int i = 0; i < keys.length; i++) {
            keyMaps[i] = new KeyMap(keys[i]);
        }

        getChildren().addAll(keyMaps);
    }

    public int getKeyMap(String key) {
        for(int i = 0; i < keys.length; i++) {
            if (key == keyMaps[i].getName()) {
                return keyMaps[i].getValue();
            }
        }
        return -1;
    }

    public void setKeyValue(String key, int value) {
        for(int i = 0; i < keys.length; i++) {
            if (key == keyMaps[i].getName()) {
                keyMaps[i].setValue(value);
            }
        }
    }


    public void update(long now) {



       //implement looping feature to  a bpm/keypress beat


    }


}



