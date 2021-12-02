package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.store.ValueStoreComp;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import java.util.Set;

public class SvgComp extends ValueStoreComp<String> {

    private int width;
    private int height;

    public SvgComp(String content, int width, int height) {
        set(content);
        this.width = width;
        this.height = height;
    }

    private String getHtml(String content) {
        return "<html><body style='margin: 0; padding: 0; border: none;' >" +
                content +
                "</body></html>";
    }

    @Override
    public Region createBase() {
        var wv = new WebView();
        wv.setPageFill(Color.TRANSPARENT);
        wv.setDisable(true);

        wv.getEngine().loadContent(getHtml(value.getValue()));
        valueProperty().addListener((c, o, n) -> {
            wv.getEngine().loadContent(getHtml(n));
        });

        wv.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
            Set<Node> scrolls = wv.lookupAll(".scroll-bar");
            for (Node scroll : scrolls) {
                scroll.setVisible(false);
            }
        });

        double ar = (double) width / height;
        wv.zoomProperty().bind(Bindings.createDoubleBinding(() -> {
            return wv.getWidth() / width;
        }, wv.widthProperty()));

        var sp = new StackPane(wv);
        sp.setAlignment(Pos.CENTER);
        var r = new AspectComp(WrapperComp.of(sp), ar).createBase();
        r.getStyleClass().add("svg-comp");
        return r;
    }
}