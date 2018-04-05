import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;

public class Main extends Application {


    //    int screenWidth = 1280;
//    int screenHeight = 800;
    private FileManager fileManager;

    private MainWindow mainWindow;

    JSONObject currentFile;
    //    File fileOpen;
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
        mainWindow = new MainWindow(w, currentFile.getJSONObject("Preferences"));
        currentFile.put("WindowData", mainWindow.saveData());
        mainWindow.getPreferenceItemPressed().addListener(event -> preferencesWindow.showPreferences(mainWindow.getPreferenceItemPressed().get()));
        mainWindow.getOpenItemPressed().addListener(event -> openFile(mainWindow.getOpenItemPressed().get()));
        mainWindow.getSaveFileItemPressed().addListener(event -> saveFile(mainWindow.getSaveFileItemPressed().get()));
        mainWindow.getSaveFileAsItemPressed().addListener(event -> saveFileAs(mainWindow.getSaveFileAsItemPressed().get()));

    }

    private void saveFile(boolean t) {
        if (!t) {
            return;
        }

        currentFile.put("WindowData", mainWindow.saveData());
        currentFile.put("Preferences", preferencesWindow.saveData());
        String fName = fileManager.save(currentFile);
        System.out.println(fName);

    }

    void saveFileAs(boolean t) {
        if (!t) {
            return;
        }

        currentFile.put("WindowData", mainWindow.saveData());
        currentFile.put("Preferences", preferencesWindow.saveData());

        String fName = fileManager.saveAs(currentFile);
        System.out.println(fName);
    }

    private void openFile(boolean t) {

        if (!t) {
            return;
        }

        currentFile = fileManager.open();
        preferencesWindow.loadData(currentFile.getJSONObject("Preferences"));
        resetWindow();
        mainWindow.loadData(currentFile.getJSONObject("WindowData"));

    }


}



