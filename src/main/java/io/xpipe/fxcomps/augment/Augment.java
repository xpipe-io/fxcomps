package io.xpipe.fxcomps.augment;

import javafx.scene.layout.Region;

import java.util.function.Consumer;

public interface Augment {

    public static Augment simple(Consumer<Region> c) {
        return (r) -> {
            c.accept(r);
            return r;
        };
    }

    Region augment(Region r);
}
