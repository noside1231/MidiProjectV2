import javafx.scene.paint.Color;

class Note {

    LEDMatrix matrix;


    float fadeIn;
    float fadeOut;
    float hold;

    Note(int id) {
        matrix = new LEDMatrix();
        fadeIn = 0;
        fadeOut = 0;
        hold = 0;
    }

    void setLED(int x, int y, Color c) {
        matrix.setLED(x, y, c);
    }

    Color getLED(int x, int y) {
        return matrix.getLED(x, y);
    }

    Led[][] getLEDS() {
        return matrix.getLEDS();
    }

    String getLEDString(int x, int y) {
        return matrix.getLEDString(x, y);
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

    void setFadeIn(float f) {
        fadeIn = f;
    }

    void setFadeOut(float f) {
        fadeOut = f;
    }

    void setHold(float f) {
        hold = f;
    }
    String getTimeString() {
        return Float.toString(fadeIn)+";"+Float.toString(hold)+";"+Float.toString(fadeOut);
    }

    void setTimeFromString(String s) {
        String[] a = s.split(";");
        System.out.println(a[0]);
        fadeIn = Float.parseFloat(a[0]);
        hold = Float.parseFloat(a[1]);
        fadeOut = Float.parseFloat(a[2]);
    }

    public float getFadeIn() {
        return fadeIn;
    }

    public float getFadeOut() {
        return fadeOut;
    }

    public float getHold() {
        return hold;
    }

    public void setSelected(int x, int y, boolean s) {
        matrix.setSelected(x, y, true);
    }


}