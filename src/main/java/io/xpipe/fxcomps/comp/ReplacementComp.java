package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.scene.layout.Region;

public abstract class ReplacementComp extends Comp {

    private Comp comp;

    protected abstract Comp createComp();

    @Override
    protected Region createBase() {
        if (comp == null) {
            comp = createComp();
        }

        return comp.create();
    }
}
