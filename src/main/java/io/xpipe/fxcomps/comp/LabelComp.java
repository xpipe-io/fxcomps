package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class LabelComp extends Comp {

    private Supplier<String> name;
    private Comp graphic;

    public LabelComp(Supplier<String> name, Comp graphic) {
        this.name = name;
        this.graphic = graphic;
    }

    @Override
    protected Region createBase() {
        var l = new Label(name.get(), graphic.create());
        l.getStyleClass().add("label-comp");
        return l;
    }
}
