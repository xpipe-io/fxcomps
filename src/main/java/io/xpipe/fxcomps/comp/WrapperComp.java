package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class WrapperComp<R extends Region> extends Comp<CompStructure<R>> {

    private final Supplier<R> region;

    public WrapperComp(Supplier<R> region) {
        this.region = region;
    }

    @Override
    public CompStructure<R> createBase() {
        return new CompStructure<>(region.get());
    }
}
