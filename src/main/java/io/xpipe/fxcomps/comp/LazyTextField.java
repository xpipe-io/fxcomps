package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXTextField;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.store.DefaultValueStoreComp;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;


public class LazyTextField extends DefaultValueStoreComp<CompStructure<StackPane>, String> {

    @Value
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Structure extends CompStructure<StackPane> {
        JFXTextField textField;
    }

    public LazyTextField(String content) {
        super(content);
    }

    @Override
    public CompStructure<StackPane> createBase() {
        var sp = new StackPane();
        var r = new JFXTextField();
        sp.focusedProperty().addListener((c,o,n) -> {
            if (n) {
                r.setDisable(false);
                r.requestFocus();
            }
        });
        r.setPrefWidth(0);
        sp.getChildren().add(r);
        sp.prefWidthProperty().bind(r.prefWidthProperty());
        sp.prefHeightProperty().bind(r.prefHeightProperty());
        r.textProperty().bindBidirectional(valueProperty());
        r.setDisable(true);

        Animation delay = new PauseTransition(Duration.millis(800));
        delay.setOnFinished(e -> {
            r.setDisable(false);
            r.requestFocus();
        });
        sp.addEventFilter(MouseEvent.MOUSE_ENTERED,
                e -> {
            delay.playFromStart();
                });
        sp.addEventFilter(MouseEvent.MOUSE_EXITED,
                e -> {
                    delay.stop();
                });
        r.focusedProperty().addListener((c,o,n) -> {
            if (!n) {
                r.setDisable(true);
            }
        });
        r.getStyleClass().add("lazy-text-field-comp");
        return Structure.builder().value(sp).textField(r).build();
    }
}
