package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;

public abstract class ReplacementComp<S extends CompStructure<?>> extends Comp<S> {

    private Comp<S> comp;

    protected abstract Comp<S> createComp();

    @Override
    public final S createBase() {
        if (comp == null) {
            comp = createComp();
        }

        return comp.createStructure();
    }
}
