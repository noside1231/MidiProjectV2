package Utilities;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

/**
 * Created by Edison on 1/15/18.
 */
public class ColorProperty {

    ObjectProperty<Color> t = new ObjectProperty<Color>() {
        @Override
        public void bind(ObservableValue<? extends Color> observable) {

        }

        @Override
        public void unbind() {

        }

        @Override
        public boolean isBound() {
            return false;
        }

        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Color get() {
            return null;
        }

        @Override
        public void addListener(ChangeListener<? super Color> listener) {

        }

        @Override
        public void removeListener(ChangeListener<? super Color> listener) {

        }

        @Override
        public void addListener(InvalidationListener listener) {

        }

        @Override
        public void removeListener(InvalidationListener listener) {

        }

        @Override
        public void set(Color value) {

        }
    };

    public ColorProperty() {

    }

}
