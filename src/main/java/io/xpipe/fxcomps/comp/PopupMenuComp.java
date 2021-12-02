package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.augment.Augment;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.Region;

public abstract class PopupMenuComp implements Augment {

    protected abstract ContextMenu createContextMenu();

    @Override
    public Region augment(Region r) {
        var cm = createContextMenu();
        r.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                cm.show(r, event.getScreenX(), event.getScreenY());
            }
        });
        return r;
    }
}
