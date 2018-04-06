import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by edisongrauman on 3/22/18.
 */
public class FileManager {

    private File currentFile;

    public FileManager() {
        currentFile = null;
    }


    public String save(JSONObject data) {

    if (currentFile == null) {
        return saveAs(data);
    }

    if (!writeFile(currentFile, data)) {
        return "";
    }

        return currentFile.getName();
    }

    public String saveAs(JSONObject data) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project File: .mpv2", "*.mpv2"));
        File file = fc.showSaveDialog(new Stage());

        if (file == null) {
            return "";
        }


        if (!writeFile(file, data)) {
            return "";
        }

        currentFile = file;
        return currentFile.getName();
    }

    private boolean writeFile(File file, JSONObject data) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data.toString());
            fileWriter.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject open() {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project File: .mpv2", "*.mpv2"));
        File file = fc.showOpenDialog(new Stage());

        if (file == null) {
            System.out.println("EMPTY");
            return null;
        }

        try {
            InputStream is = new FileInputStream(file);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileString = sb.toString();

            currentFile = file;
            System.out.println("FINE");
            return new JSONObject(fileString);

        } catch (Exception e) {
            System.out.println("EXCEPT");
            e.printStackTrace();
            return null;
        }
    }


}
