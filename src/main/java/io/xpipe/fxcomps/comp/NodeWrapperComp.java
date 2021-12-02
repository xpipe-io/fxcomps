package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class NodeWrapperComp extends Comp {

    private Node node;

    public NodeWrapperComp(Node node) {
        this.node = node;
    }

    @Override
    public Region createBase() {
        var s = new StackPane(node);
        s.setAlignment(Pos.CENTER);
        return s;
    }
}
