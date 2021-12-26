package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXListView;
import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.util.PlatformUtil;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListComp<T> extends Comp<CompStructure<JFXListView<Node>>> {

    private final ObservableList<T> shown;
    private final ObservableList<T> all;
    private final Property<T> selected;
    private final Function<T, Comp<?>> compFunction;

    public ListComp(ObservableList<T> shown, ObservableList<T> all, Property<T> selected, Function<T, Comp<?>> compFunction) {
        this.shown = shown;
        this.all = all;
        this.selected = selected;
        this.compFunction = compFunction;
    }

    @Override
    public CompStructure<JFXListView<Node>> createBase() {
        Map<T, Region> cache = new HashMap<>();

        JFXListView<Node> listView = new JFXListView<>();
        var newItems = shown.stream()
                .map(v -> {
                    var r = compFunction.apply(v).createRegion();
                    cache.put(v, r);
                    return r;
                })
                .collect(Collectors.toList());
        listView.getItems().setAll(newItems);

        if (selected != null) {
            listView.getSelectionModel().selectedItemProperty().addListener((c, o, n) -> {
                var item = new DualHashBidiMap<>(cache).inverseBidiMap().get(n);
                selected.setValue(item);
            });

            selected.addListener((c, o, n) -> {
                var selectedNode = cache.get(n);
                PlatformUtil.runLaterIfNeeded(() -> {
                    listView.getSelectionModel().select(selectedNode);
                });
            });
        }

        shown.addListener((ListChangeListener<? super T>) (c) -> {
            var newShown = c.getList().stream()
                    .map(v -> {
                        if (!cache.containsKey(v)) {
                            cache.put(v, compFunction.apply(v).createRegion());
                        }

                        return cache.get(v);
                    }).toList();
            PlatformUtil.runLaterIfNeeded(() -> {
                listView.getItems().setAll(newShown);
                listView.layout();
            });
        });

        all.addListener((ListChangeListener<? super T>) c -> {
            cache.keySet().retainAll(c.getList());
        });

        return new CompStructure<>(listView);
    }
}
