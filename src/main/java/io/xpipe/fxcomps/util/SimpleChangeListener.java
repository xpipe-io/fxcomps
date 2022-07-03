package io.xpipe.fxcomps.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@FunctionalInterface
public interface SimpleChangeListener<T> {

    static <T> void apply(ObservableValue<T> obs, SimpleChangeListener<T> cl) {
        cl.onChange(obs.getValue());
        obs.addListener(cl.wrapped());
    }

    void onChange(T val);

    default ChangeListener<T> wrapped() {
        return (observable, oldValue, newValue) -> this.onChange(newValue);
    }
}
