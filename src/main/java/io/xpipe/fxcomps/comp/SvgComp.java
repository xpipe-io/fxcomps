package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.util.PlatformUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Set;

public class SvgComp extends ReplacementComp<AspectComp.Structure<SvgComp.Structure>> {

    @Value
    @EqualsAndHashCode(callSuper = true)
    public static class Structure extends CompStructure<StackPane> {
        WebView webView;

        public Structure(StackPane value, WebView webView) {
            super(value);
            this.webView = webView;
        }
    }

    private final ObservableValue<Number> width;
    private final ObservableValue<Number> height;
    private final ObservableValue<String> svgContent;

    public SvgComp(ObservableValue<Number> width, ObservableValue<Number> height, ObservableValue<String> svgContent) {
        this.width = PlatformUtil.wrap(width);
        this.height = PlatformUtil.wrap(height);
        this.svgContent = PlatformUtil.wrap(svgContent);
    }

    private String getHtml(String content) {
        return "<html><body style='margin: 0; padding: 0; border: none;' >" +
                content +
                "</body></html>";
    }

    private WebView createWebView() {
        var wv = new WebView();
        wv.setPageFill(Color.TRANSPARENT);
        wv.setDisable(true);

        wv.getEngine().loadContent(getHtml(svgContent.getValue()));
        svgContent.addListener((c, o, n) -> {
            wv.getEngine().loadContent(getHtml(n));
        });

        // Hide scrollbars that popup on every content change. Bug in WebView?
        wv.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
            Set<Node> scrolls = wv.lookupAll(".scroll-bar");
            for (Node scroll : scrolls) {
                scroll.setVisible(false);
            }
        });

        // As the aspect ratio of the WebView is kept constant, we can compute the zoom only using the width
        wv.zoomProperty().bind(Bindings.createDoubleBinding(() -> {
            return wv.getWidth() / width.getValue().doubleValue();
        }, wv.widthProperty(), width));
        return wv;
    }

    @Override
    protected Comp<AspectComp.Structure<Structure>> createComp() {
        var ar = Bindings.createDoubleBinding(() -> {
            return width.getValue().doubleValue() / height.getValue().doubleValue();
        }, width, height);
        return new AspectComp<>(Comp.ofStructure(() -> {
            var wv = createWebView();
            var sp = new StackPane(wv);
            sp.setAlignment(Pos.CENTER);
            return new Structure(sp, wv);
        }), ar).styleClass("svg-comp");
    }
}
