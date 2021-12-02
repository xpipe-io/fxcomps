package io.xpipe.fxcomps.augment;

import javafx.scene.layout.Region;

public class StyleAugment implements Augment {

    public static StyleAugment styleClass(String c) {
        return new StyleAugment(c);
    }

    private final String styleClass;

    public StyleAugment(String styleClass) {
        this.styleClass = styleClass;
    }

    @Override
    public Region augment(Region r) {
        r.getStyleClass().add(styleClass);
        return r;
    }
}
