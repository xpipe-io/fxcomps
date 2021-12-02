package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class CountComp<T> extends Comp {

    private ObservableList<T> sub;
    private ObservableList<T> all;

    public CountComp(ObservableList<T> sub, ObservableList<T> all) {
        this.sub = sub;
        this.all = all;
    }

    @Override
    protected Region createBase() {
        var label = new Label();
        label.setAlignment(Pos.CENTER);
        label.textProperty().bind(Bindings.createStringBinding(() -> {
            if (sub.size() == all.size()) {
                return all.size() + "";
            } else {
                return "" + sub.size() + "/" + all.size();
            }
        }, sub, all));
        label.getStyleClass().add("count-comp");
        return label;
    }
}
