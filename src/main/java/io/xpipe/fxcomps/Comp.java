package io.xpipe.fxcomps;

import io.xpipe.fxcomps.augment.Augment;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public abstract class Comp {

    private List<Augment> augments;

    public Comp augment(Augment a) {
        if (augments == null) {
            augments = new ArrayList<>();
        }
        augments.add(a);
        return this;
    }

    public Region create() {
        var r = createBase();
        if (augments != null) {
            for (var a : augments) {
                var newR = a.augment(r);
                if (newR != null) {
                    r = newR;
                }
            }
        }
        return r;
    }

    protected abstract Region createBase();
}

