package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXListView;
import io.xpipe.fxcomps.Comp;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListComp<T> extends Comp {

    private ObservableList<T> shown;
    private ObservableList<T> all;
    private final Function<T, Comp> compFunction;

    public ListComp(ObservableList<T> shown, ObservableList<T> all, Function<T, Comp> compFunction) {
        this.shown = shown;
        this.all = all;
        this.compFunction = compFunction;
    }

    @Override
    public Region createBase() {
        Map<T, Region> cache = new HashMap<>();

        JFXListView<Node> listView = new JFXListView<>();
        var newItems = shown.stream()
                .map(v -> {
                    var r = compFunction.apply(v).create();
                    cache.put(v, r);
                    return r;
                })
                .collect(Collectors.toList());
        listView.getItems().setAll(newItems);

        shown.addListener((ListChangeListener<? super T>) (c) -> {
            Platform.runLater(() -> {
                listView.getItems().setAll(c.getList().stream()
                        .map(v -> {
                            if (!cache.containsKey(v)) {
                                cache.put(v, compFunction.apply(v).create());
                            }

                            return cache.get(v);
                        })
                        .collect(Collectors.toList()));
            });
        });

        all.addListener((ListChangeListener<? super T>) c -> {
            cache.keySet().retainAll(c.getList());
        });

        return listView;
    }
}
