import Utilities.KeyMap;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

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
        setSpacing(3);
        setAlignment(Pos.CENTER);
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

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();

        for (int i = 0; i < keyMaps.length; i++) {
            tFile.put(Integer.toString(i), keyMaps[i].getValue());
        }
        return tFile;
    }

    public void loadData(JSONObject obj) {
        for (int i = 0; i < keyMaps.length; i++) {
            keyMaps[i].setValue(obj.getInt(Integer.toString(i)));
        }

    }




}



