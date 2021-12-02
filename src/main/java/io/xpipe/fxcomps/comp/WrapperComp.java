package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class WrapperComp extends Comp {

    public static WrapperComp of(Supplier<? extends Region> r) {
        return new WrapperComp(r);
    }

    public static WrapperComp of(Region r) {
        return new WrapperComp(() -> r);
    }

    private final Supplier<? extends Region> region;

    private WrapperComp(Supplier<? extends Region> region) {
        this.region = region;
    }

    @Override
    public Region createBase() {
        return region.get();
    }
}
