import javafx.scene.paint.Color;

class Note {

    LEDMatrix matrix;

    Note(int id) {
        matrix = new LEDMatrix();
    }

    void setLED(int x, int y, Color c) {
        matrix.setLED(x, y, c);
    }

    Color getLED(int x, int y) {
        return matrix.getLED(x, y);
    }
    boolean getLEDSelected(int x, int y) {
        return matrix.getLEDSelected(x, y);
    }

    void toggleSelected(int x, int  y) {
        matrix.toggleSelected(x, y);
    }

    void resetMatrix() {
        matrix.reset();
    }


}