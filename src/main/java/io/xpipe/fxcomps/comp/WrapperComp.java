package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.scene.layout.Region;

public class WrapperComp extends Comp {

    private Region region;

    public WrapperComp(Region region) {
        this.region = region;
    }

    @Override
    public Region createBase() {
        return region;
    }
}
