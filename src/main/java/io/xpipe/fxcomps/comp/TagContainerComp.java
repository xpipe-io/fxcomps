package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXMasonryPane;
import io.xpipe.fxcomps.store.ListValueStoreComp;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;

public class TagContainerComp extends ListValueStoreComp<TagComp> {

    private ObservableList<TagComp> shownTags = FXCollections.observableArrayList();

    public void add(TagComp t) {
        values.add(t);
        t.setContainer(this);

        if (t.isShown()) {
            shownTags.add(t);
        }
        t.shownProperty().addListener((c,o,n) -> {
            if (n) {
                shownTags.add(t);
            } else {
                shownTags.remove(t);
            }
        });
    }

    @Override
    protected Region createBase() {
        var c = new JFXMasonryPane();
        c.setCellWidth(10);
        c.setCellHeight(10);
        shownTags.forEach(t -> {
            var r = t.create();
            c.getChildren().add(r);
        });
        shownTags.addListener((ListChangeListener<? super TagComp>) change -> {
            shownTags.forEach(t -> {
                var r = t.create();
                c.getChildren().add(r);
            });
        });
        c.getStyleClass().add("tag-container-comp");
        return c;
    }
}
