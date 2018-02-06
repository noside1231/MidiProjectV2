import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class Main extends Application {


    int screenWidth = 1280;
    int screenHeight = 800;
    MainWindow mainWindow;

    JSONObject currentFile;
    File fileOpen;
    Stage w;
    Preferences preferencesWindow;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        currentFile = new JSONObject();
        preferencesWindow = new Preferences();
        preferencesWindow.initializePreferencesObject();
        preferencesWindow.getSaveButtonPressed().addListener(event -> savePreferences(preferencesWindow.getSaveButtonPressed().get()));
        w = window;
        savePreferences(true);

        resetWindow();


        //Animation Timer
        new AnimationTimer() {

            final long[] frameTimes = new long[100];
            int frameTimeIndex = 0;
            boolean arrayFilled = false;
            double frameRate;

            @Override
            public void handle(long now) {


                //Calculate FPS
                long oldFrameTime = frameTimes[frameTimeIndex];
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                    frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
//                    frameRateItem.setText(String.format("Frame Rate: %.3f", frameRate));
                }
                mainWindow.update(now, frameRate);

//                s.sendMatrixData(mainWindow.getMixerMatrix());


            }
        }.start();

    }

    void savePreferences(boolean t) {
        if (!t) {
            return;
        }
        currentFile.put("Preferences", preferencesWindow.saveData());
        resetWindow();

    }

    void resetWindow() {
        mainWindow = new MainWindow(w, currentFile.getJSONObject("Preferences"));
        mainWindow.getPreferenceItemPressed().addListener(event -> preferencesWindow.showPreferences(mainWindow.getPreferenceItemPressed().get()));
        mainWindow.getOpenItemPressed().addListener(event -> openFile(false));
        mainWindow.getSaveFileItemPressed().addListener(event -> saveFile(false));
        mainWindow.getSaveFileAsItemPressed().addListener(event -> saveFileAs(false));

    }

    private void saveFile(boolean t) {
        if (!t) {
            return;
        }
        System.out.println("SAVE FILE backend");
        if (fileOpen == null) {
            saveFileAs(true);
        } else {
            writeFile(fileOpen);
        }
    }


    void saveFileAs(boolean t) {
        if (!t) {
            return;
        }
        System.out.println("SAVE FILE AS backend");
        currentFile.put("WindowData", mainWindow.saveData());
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project File: .mpv2", "*.mpv2"));
        File selectedFile = fc.showSaveDialog(new Stage());
        if (selectedFile != null) {
            fileOpen = selectedFile;
//            fileOpenLabel.setText(fileOpen.getName());
            writeFile(selectedFile);
        }
    }

    void writeFile(File f) {
        try {
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(currentFile.toString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFile(boolean t) {

        System.out.println(t);
        if (!t) {
            return;
        }
        System.out.println("OPEN FILE backend");

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project File: .mpv2", "*.mpv2"));
        File selectedFile = fc.showOpenDialog(new Stage());

        if (selectedFile != null) {
            fileOpen = selectedFile;
//            fileOpenLabel.setText(fileOpen.getName());
            readFile(selectedFile);
            loadData();
        }

    }

    private void readFile(File f) {
        try {

            InputStream is = new FileInputStream(f);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileString = sb.toString();
            currentFile = new JSONObject(fileString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        screenWidth = Integer.parseInt((currentFile.getJSONObject("Preferences").getString("screenX")));
        screenHeight = Integer.parseInt((currentFile.getJSONObject("Preferences").getString("screenY")));
        preferencesWindow.loadData(currentFile.getJSONObject("Preferences"));
        resetWindow();
        mainWindow.loadData(currentFile.getJSONObject("WindowData"));


    }


    }



