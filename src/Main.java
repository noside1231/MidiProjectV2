import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;

public class Main extends Application {


    private FileManager fileManager;

    private MainWindow mainWindow;

    private NewFileWindow newFileWindow;

    JSONObject currentFile;

    private Stage w;
    private Preferences preferencesWindow;

    private Serial serialPort;
    private ObservableList<String> serialPortList;
    boolean serialEnabled;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        currentFile = new JSONObject();

        fileManager = new FileManager();

        //serial port
        serialPort = new Serial();
        serialPortList = serialPort.getPortNames();

        newFileWindow = new NewFileWindow();

        preferencesWindow = new Preferences();
        preferencesWindow.initializePreferencesObject();
        preferencesWindow.setSerialPortList(serialPortList);
        preferencesWindow.setSerialBaudRates(serialPort.getBaudRates());
        preferencesWindow.getSaveButtonPressed().addListener(event -> savePreferences(preferencesWindow.getSaveButtonPressed().get()));
        preferencesWindow.getBaudRateValue().addListener(event -> serialPort.setBaudRate(preferencesWindow.getBaudRateValue().get()));
        preferencesWindow.getSerialPortValue().addListener(event -> serialPort.connectToPort(preferencesWindow.getSerialPortValue().get()));
        serialPort.getStatus().addListener(event -> preferencesWindow.setSerialStatusLabel(serialPort.getStatus().get()));


        w = window;
        savePreferences(true);

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
                }
                mainWindow.update(now, frameRate);

                if (serialEnabled) {
                    serialPort.updateMatrixData(mainWindow.getMixerMatrix(), now);
                }


            }
        }.start();

    }

    void savePreferences(boolean t) {
        if (!t) {
            return;
        }
        currentFile.put("Preferences", preferencesWindow.saveData());
        serialEnabled = currentFile.getJSONObject("Preferences").getBoolean("SerialEnabled");
        resetWindow();

    }

    void resetWindow() {
        System.out.println("RESET");
        mainWindow = new MainWindow(w, currentFile.getJSONObject("Preferences"));
        mainWindow.setTitle(fileManager.getCurrentFileTitle());
        mainWindow.getPreferenceItemPressed().addListener(event -> preferencesWindow.showPreferences(mainWindow.getPreferenceItemPressed().get()));
        mainWindow.getOpenItemPressed().addListener(event -> openFile(mainWindow.getOpenItemPressed().get()));
        mainWindow.getSaveFileItemPressed().addListener(event -> saveFile(mainWindow.getSaveFileItemPressed().get()));
        mainWindow.getSaveFileAsItemPressed().addListener(event -> saveFileAs(mainWindow.getSaveFileAsItemPressed().get()));
        mainWindow.getNewItemPressed().addListener(event -> newFile(mainWindow.getNewItemPressed().get()));

    }

    private void saveFile(boolean t) {
        if (!t) {
            return;
        }

        currentFile.put("WindowData", mainWindow.saveData());
        currentFile.put("Preferences", preferencesWindow.saveData());
        String fName = fileManager.save(currentFile);
        mainWindow.setTitle(fileManager.getCurrentFileTitle());
        System.out.println(fName);

    }

    void saveFileAs(boolean t) {
        if (!t) {
            return;
        }

        currentFile.put("WindowData", mainWindow.saveData());
        currentFile.put("Preferences", preferencesWindow.saveData());

        String fName = fileManager.saveAs(currentFile);
        mainWindow.setTitle(fileManager.getCurrentFileTitle());
        System.out.println(fName);
    }

    private void openFile(boolean t) {
        System.out.println("OPENNNN" + t);
        if (!t) {
            return;
        }

        JSONObject tFile = fileManager.open();

        if (tFile == null) {
            System.out.println("NULL");
            return;
        }

        currentFile = tFile;
        try {
            preferencesWindow.loadData(currentFile.getJSONObject("Preferences"));
        } catch (Exception e) {
            System.out.println("COULD NOT LOAD DATA");
            return;
        }

        resetWindow();

        try {
            mainWindow.loadData(currentFile.getJSONObject("WindowData"));
        } catch (Exception e) {
            System.out.println("COULD NOT LOAD DATA");
            return;
        }
    }

    private void newFile(boolean b) {
        if (!b) {
            return;
        }

        String a = newFileWindow.showNewFileWindow().get();
        if (!a.isEmpty()) {
            String[] s = a.split(";");
            preferencesWindow.initializePreferencesObject();
            preferencesWindow.setLedsPerStrip(Integer.parseInt(s[0]));
            preferencesWindow.setStrips(Integer.parseInt(s[1]));
            savePreferences(true);
            fileManager.resetCurrentFile();
            resetWindow();

        }


    }


}



