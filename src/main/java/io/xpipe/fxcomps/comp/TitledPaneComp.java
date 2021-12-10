package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class TitledPaneComp extends Comp {

    private Supplier<String> name;
    private Comp content;
    private final int height;

    public TitledPaneComp(Supplier<String> name, Comp content, int height) {
        this.name = name;
        this.content = content;
        this.height = height;
    }

    @Override
    protected Region createBase() {
        var tp = new TitledPane(name.get(), content.create());
        tp.getStyleClass().add("titled-pane-comp");
        tp.setExpanded(false);
        tp.setAnimated(false);
        AtomicInteger minimizedSize = new AtomicInteger();
        tp.expandedProperty().addListener((c,o,n) -> {
            if (n) {
                if (minimizedSize.get() == 0) {
                    minimizedSize.set((int) tp.getHeight());
                }
                tp.setPrefHeight(height);
            } else {
                tp.setPrefHeight(minimizedSize.get());
            }
        });
        return tp;
    }
}
