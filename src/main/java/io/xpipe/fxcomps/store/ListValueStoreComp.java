package io.xpipe.fxcomps.store;

import io.xpipe.fxcomps.Comp;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ListValueStoreComp<T> extends Comp {

    protected ListProperty<T> values = new SimpleListProperty<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>()));

    public ObservableList<T> getValues() {
        return values.get();
    }

    public ListProperty<T> valuesProperty() {
        return values;
    }
}
