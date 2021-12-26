package io.xpipe.fxcomps.augment;

import io.xpipe.fxcomps.CompStructure;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class GrowAugment<S extends CompStructure<?>> implements Augment<S> {

    public static <S extends CompStructure<?>> GrowAugment<S> create(boolean width, boolean height) {
        return new GrowAugment<>(width, height);
    }

    private final boolean width;
    private final boolean height;

    private GrowAugment(boolean width, boolean height) {
        this.width = width;
        this.height = height;
    }

    private void bind(Region r, Node parent) {
        if (!(parent instanceof Region p)) {
            return;
        }

        if (width) {
            r.prefWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    p.getWidth() - p.getPadding().getLeft() - p.getPadding().getRight(),
                    p.widthProperty(), p.paddingProperty()));
        }
        if (height) {
            r.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
                    p.getHeight() - p.getPadding().getTop() - p.getPadding().getBottom(),
                    p.heightProperty(), p.paddingProperty()));
        }
    }

    @Override
    public void augment(S struc) {
        struc.get().parentProperty().addListener((c,o,n) -> {
            if (o instanceof Region) {
                if (width) struc.get().prefWidthProperty().unbind();
                if (height) struc.get().prefHeightProperty().unbind();
            }

            bind(struc.get(), n);
        });

        bind(struc.get(), struc.get().getParent());
    }
}
