package io.xpipe.fxcomps.store;

import io.xpipe.fxcomps.Comp;
import javafx.beans.value.ObservableValue;

public abstract class WrappedValueStoreComp<T> extends Comp {

    private ObservableValue<T> observableValue;

    public WrappedValueStoreComp(ObservableValue<T> observableValue) {
        this.observableValue = observableValue;
    }

    public T getValue() {
        return observableValue.getValue();
    }

    public ObservableValue<T> valueProperty() {
        return observableValue;
    }
}
