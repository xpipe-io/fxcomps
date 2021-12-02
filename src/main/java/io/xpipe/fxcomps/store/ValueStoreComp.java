package io.xpipe.fxcomps.store;

import io.xpipe.fxcomps.Comp;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public abstract class ValueStoreComp<T> extends Comp {

    protected Property<T> value = new SimpleObjectProperty<>();

    public boolean set(T newValue) {
        if (!isValid(newValue)) {
            return false;
        }

        value.setValue(newValue);
        return true;
    }

    protected void setDefault() {

    }

    protected boolean isValid(T newValue) {
        return true;
    }

    public T getValue() {
        return value.getValue();
    }

    public Property<T> valueProperty() {
        return value;
    }
}
