import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

public class Main extends Application {


    private FileManager fileManager;

    private MainWindow mainWindow;

    private NewFileWindow newFileWindow;

    private HelpWindow helpWindow;

    private JSONObject currentFile;

    private Stage w;

    private PreferencesWindowNew preferencesWindowNew;

    private SerialNew serialPort;

    private int defaultStrips = 5;
    private int defaultLedsPerstrip = 30;
    private int defaultScreenX = 1200;
    private int defaultScreenY = 800;

    boolean fullScreen = false;

    private int screenX = defaultScreenX;
    private int screenY = defaultScreenY;
    private int tempScreenX = defaultScreenX;
    private int tempScreenY = defaultScreenY;

    private int strips = defaultStrips;
    private int ledsPerStrip = defaultLedsPerstrip;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        window.fullScreenProperty().addListener(event -> {
            if (window.isFullScreen()) {
                screenX = (int)Screen.getPrimary().getBounds().getWidth();
                screenY = (int)Screen.getPrimary().getBounds().getHeight();
                window.initStyle(StageStyle.UNDECORATED);
                resetWindow();
            } else {
                window.initStyle(StageStyle.DECORATED);
            }
        });

        currentFile = new JSONObject();

        fileManager = new FileManager();

        serialPort = new SerialNew();

        w = window;
        resetWindow();

        newFileWindow = new NewFileWindow(window);

        preferencesWindowNew = new PreferencesWindowNew(window);


        preferencesWindowNew.getScreenXChanged().addListener(event -> tempScreenX = preferencesWindowNew.getScreenXChanged().get());
        preferencesWindowNew.getScreenYChanged().addListener(event -> tempScreenY = preferencesWindowNew.getScreenYChanged().get());
        preferencesWindowNew.getSetScreen().addListener(event -> {
            screenX = tempScreenX;
            screenY = tempScreenY;
            resetWindow();
        });
        preferencesWindowNew.getRestoreScreen().addListener(event -> {
            screenX = defaultScreenX;
            screenY = defaultScreenY;
            fullScreen = false;
            resetWindow();
        });
        preferencesWindowNew.getFullscreenSelected().addListener(event -> {
            fullScreen = preferencesWindowNew.getFullscreenSelected().get();
            resetWindow();
        } );

        preferencesWindowNew.getSerialConnectDisconnectPressed().addListener(event -> serialPort.connectDisconnectSerial(preferencesWindowNew.getSerialConnectDisconnectPressed().get()));
        preferencesWindowNew.getSerialRefreshPressed().addListener(event -> preferencesWindowNew.setSerialPortList(serialPort.getPorts(), serialPort.getConnectedPort(), preferencesWindowNew.getSerialRefreshPressed().get()));
        preferencesWindowNew.getSerialPortSelected().addListener(event -> serialPort.setPort(preferencesWindowNew.getSerialPortSelected().get()));
        preferencesWindowNew.getSerialBaudSelected().addListener(event -> serialPort.setBaudRate(preferencesWindowNew.getSerialBaudSelected().get()));
        preferencesWindowNew.getSerialEnabled().addListener(event -> serialPort.setSerialEnabled(preferencesWindowNew.getSerialEnabled().get()));

        serialPort.getStatus().addListener(event -> preferencesWindowNew.setSerialStatus(serialPort.getStatus().get()));
        serialPort.isConnected().addListener(event -> preferencesWindowNew.setConnected(serialPort.isConnected().get()));

        helpWindow = new HelpWindow();



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

                //update
                mainWindow.update(now, frameRate);
                serialPort.updateMatrixData(mainWindow.getMixerMatrix());



            }
        }.start();

    }

    private void resetWindow() {
        System.out.println("RESET");
        mainWindow = new MainWindow(w, strips, ledsPerStrip, screenX, screenY, fullScreen);
        mainWindow.setTitle(fileManager.getCurrentFileTitle());
        mainWindow.getPreferenceItemPressed().addListener(event -> openPreferencesWindow(mainWindow.getPreferenceItemPressed().get()));
        mainWindow.getOpenItemPressed().addListener(event -> openFile(mainWindow.getOpenItemPressed().get()));
        mainWindow.getSaveFileItemPressed().addListener(event -> saveFile(mainWindow.getSaveFileItemPressed().get()));
        mainWindow.getSaveFileAsItemPressed().addListener(event -> saveFileAs(mainWindow.getSaveFileAsItemPressed().get()));
        mainWindow.getNewItemPressed().addListener(event -> newFile(mainWindow.getNewItemPressed().get()));
        mainWindow.getHelpWindowPressed().addListener(event -> showHelpWindow(mainWindow.getHelpWindowPressed().get()));
        System.out.println("DONE RESET");
    }

    private void openPreferencesWindow(boolean b) {
        System.out.println(b + " OPENNNN");
        if (!b) {
            return;
        }

        System.out.println("OPEN PREFERENCE");

        preferencesWindowNew.setSerialPortList(serialPort.getPorts(), serialPort.getConnectedPort(), b);
        preferencesWindowNew.setBaudRates(serialPort.getBaudRates(), serialPort.getConnectedBaud());
        preferencesWindowNew.setConnected(serialPort.isConnected().get());

        preferencesWindowNew.setScreenX(screenX);
        preferencesWindowNew.setScreenY(screenY);
        preferencesWindowNew.setFullScreen(fullScreen);

        preferencesWindowNew.showPreferencesWindow();
    }

    private void saveFile(boolean t) {
        if (!t) {
            return;
        }

        currentFile.put("WindowData", mainWindow.saveData());
        currentFile.put("Strips", strips);
        currentFile.put("LedsPerStrip", ledsPerStrip);
        String fName = fileManager.save(currentFile);
        mainWindow.setTitle(fileManager.getCurrentFileTitle());
        System.out.println(fName);

    }

    private void saveFileAs(boolean t) {
        if (!t) {
            return;
        }

        currentFile.put("WindowData", mainWindow.saveData());
        currentFile.put("Strips", strips);
        currentFile.put("LedsPerStrip", ledsPerStrip);
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

        strips = currentFile.getInt("Strips");
        ledsPerStrip = currentFile.getInt("LedsPerStrip");

        resetWindow();

        try {
            mainWindow.loadData(currentFile.getJSONObject("WindowData"));
        } catch (Exception e) {
            System.out.println("COULD NOT LOAD WINDOW DATA");
            return;
        }
    }

    private void newFile(boolean b) {
        if (!b) {
            return;
        }

        String a = newFileWindow.showNewFileWindow().get();
        if (!a.isEmpty()) {
            String[] s = a.split(";"); //ledsperstrip, strip
            strips = Integer.parseInt(s[1]);
            ledsPerStrip = Integer.parseInt(s[0]);
            fileManager.resetCurrentFile();
            resetWindow();

        }

    }

    private void showHelpWindow(boolean b) {
        if (b) {
            helpWindow.showHelpWindow();
        }
    }

}



