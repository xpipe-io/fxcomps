package io.xpipe.fxcomps.augment;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class GrowAugment implements Augment {

    public static GrowAugment grow(boolean width, boolean height) {
        return new GrowAugment(width, height);
    }

    private boolean width;
    private boolean height;

    public GrowAugment(boolean width, boolean height) {
        this.width = width;
        this.height = height;
    }

    private void bind(Region r, Node parent) {
        if (!(parent instanceof Region p)) {
            return;
        }

        if (width) {
            r.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                return p.getWidth() - p.getPadding().getLeft() - p.getPadding().getRight();
            }, p.widthProperty(), p.paddingProperty()));
        }
        if (height) {
            r.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> {
                return p.getHeight() - p.getPadding().getTop() - p.getPadding().getBottom();
            }, p.heightProperty(), p.paddingProperty()));
        }
    }


    @Override
    public Region augment(Region r) {
        r.parentProperty().addListener((c,o,n) -> {
            if (o instanceof Region) {
                if (width) r.prefWidthProperty().unbind();
                if (height) r.prefHeightProperty().unbind();
            }

            bind(r, n);
        });

        bind(r, r.getParent());
        return r;
    }
}
