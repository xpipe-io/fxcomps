package io.xpipe.fxcomps.augment;

import com.jfoenix.controls.JFXSpinner;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class LoadingAugment implements Augment {

    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    public boolean isLoading() {
        return loading.get();
    }

    public BooleanProperty loadingProperty() {
        return loading;
    }

    @Override
    public Region augment(Region r) {
        JFXSpinner loading = new JFXSpinner();
        var loadingBg = new StackPane(loading);
        loadingBg.getStyleClass().add("loading-comp");

        // Reduce flickering for consecutive loads
        loadingProperty().addListener((c, o, busy) -> {
            if (!busy) {
                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {
                    }

                    if (!isLoading()) {
                        Platform.runLater(() -> loadingBg.setVisible(false));
                    }
                });
                t.setDaemon(true);
                t.setName("loading delay");
                t.start();
            } else {
                Platform.runLater(() -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(500), loading);
                    ft.setFromValue(0.0);
                    ft.setToValue(1.0);
                    loadingBg.setVisible(true);
                    ft.play();
                });
            }
        });

        loadingBg.setMinWidth(Pane.USE_COMPUTED_SIZE);
        loadingBg.setPrefHeight(Pane.USE_COMPUTED_SIZE);

        var stack = new StackPane(r, loadingBg);
        return stack;
    }
}
