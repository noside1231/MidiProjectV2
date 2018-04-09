import Utilities.NumberTextField;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

/**
 * Created by edisongrauman on 4/7/18.
 */
public class MediaPlayerWindow extends VBox {

    private Media media;
    private MediaPlayer mediaPlayer;

    private Button startButton;
    private Button stopButton;
    private Button loadButton;
    private Label currentSongLabel;

    private NumberTextField startTimeField;

    private String currentSongPath;

    private SimpleFloatProperty startTime;

    public MediaPlayerWindow() {

        startTime = new SimpleFloatProperty(0);

        startButton = new Button("Play");
        startButton.setOnAction(event -> {
            try {
                mediaPlayer.stop();
                System.out.println(startTime.get());
                mediaPlayer.play();
//                System.out.println(mediaPlayer.getStopTime().divide(2));
                mediaPlayer.seek(mediaPlayer.getStopTime().divide(2));

                System.out.println(mediaPlayer.getCurrentTime());
            } catch (Exception e){};
        });

        stopButton = new Button("Stop");
        stopButton.setOnAction(event -> {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {};
        });

        loadButton = new Button("Load Song");
        loadButton.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select mp3","*.mp3"));
            File file = fc.showOpenDialog(null);
            if (file == null) return;
            currentSongPath = file.getAbsolutePath();
            currentSongPath = currentSongPath.replace("\\", "/");
            initializeMediaPlayer();
        });

        startTimeField = new NumberTextField(0);
        startTimeField.getValue().addListener(event -> startTime.set(startTimeField.getValue().get()));

        currentSongLabel = new Label("No File Loaded");

        getChildren().addAll(startButton, stopButton, loadButton, currentSongLabel, startTimeField);

    }

    private void initializeMediaPlayer() {
        media = new Media(new File(currentSongPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        currentSongLabel.setText("File Loaded: " + currentSongPath.substring(currentSongPath.lastIndexOf("/")+1, currentSongPath.length()-4));

    }


}
