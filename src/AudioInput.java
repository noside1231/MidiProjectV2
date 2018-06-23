import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;


/**
 * Created by edisongrauman on 5/7/18.
 */
public class AudioInput extends HBox {


    private Label label;
    private Button startButton;
    private Button stopButton;

    public AudioInput() {

      label = new Label("AUDIO INPUT TEST");

      startButton = new Button("Start");
      stopButton = new Button("Stop");

      getChildren().addAll(label, startButton, stopButton);



        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 2, 4, 44100, false);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                return;
            }

            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();

            System.out.println("Starting Recording");

            targetLine.start();

            Thread thread = new Thread(() -> {
//                AudioInputStream audioStream = new AudioInputStream(targetLine);
////                File audioFile = new File("record.wav");
//                OutputStream audioFile = new OutputStream() {
//                    @Override
//                    public void write(int b) throws IOException {
//                        System.out.println(b);
//                    }
//                };
//                try {
////                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


//                while (true) {
//                    try {
//                        byte[] bh = new byte[8];
//                        byte[] bl = new byte[8];
//                        targetLine.read(bh, 0, bh.length);
//                        targetLine.read(bl, 0, bh.length);
//                        int sample = getSixteenBitSample(bh, (bl);
//
//
////                        System.out.println(b[0] + " " + b[1] + " " + b[2] + " " + b[3] + " " + b[4] + " " + b[5] + " " + b[6] + " " + b[7]);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

            });


            thread.start();
//            targetLine.stop();
//            targetLine.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSixteenBitSample(byte[] high, byte low) {
        int h = high[0];
        return (h << 8) + (low & 0x00ff);
    }


}
