package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.store.ValueStoreComp;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.function.Supplier;

public abstract class MultiStepComp extends ValueStoreComp<CompStructure<VBox>, MultiStepComp.Step<?>> {

    @Value
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Structure extends CompStructure<StackPane> {
        JFXTextField textField;
    }

    public static abstract class Step<S extends CompStructure<?>> extends Comp<S> {

        private Runnable help;

        public Step(Runnable help) {
            this.help = help;
        }

        public void onFailedContinue() {}

        public void onBack() {}

        public boolean onContinue() {
            return true;
        }

        public boolean canContinue() {
            return true;
        }
    }

    private static final PseudoClass COMPLETED = PseudoClass.getPseudoClass("completed");
    private static final PseudoClass CURRENT = PseudoClass.getPseudoClass("current");
    private static final PseudoClass NEXT = PseudoClass.getPseudoClass("next");

    private List<Entry> entries;
    private int currentIndex = 0;

    public void next() {
        if (!getValue().canContinue()) {
            return;
        }

        int index = Math.min(getCompIndex() + 1, entries.size() - 1);
        if (currentIndex == index) {
            return;
        }

        getValue().onContinue();
        currentIndex = index;
        set(entries.get(index).comp().get());
    }

    public void previous() {
        int index = Math.max(currentIndex - 1, 0);
        if (currentIndex == index) {
            return;
        }

        getValue().onBack();
        currentIndex = index;
        set(entries.get(index).comp().get());
    }

    public boolean isCompleted(Entry e) {
        return entries.indexOf(e) < currentIndex;
    }

    public boolean isNext(Entry e) {
        return entries.indexOf(e) > currentIndex;
    }

    public boolean isCurrent(Entry e) {
        return entries.indexOf(e) == currentIndex;
    }

    public int getCompIndex() {
        return currentIndex;
    }

    public boolean isFirstPage() {
        return currentIndex == 0;
    }

    public boolean isLastPage() {
        return currentIndex == entries.size() - 1;
    }

    protected Region createStepOverview(Region content) {
        HBox box = new HBox();
        box.setFillHeight(true);
        box.getStyleClass().add("top");
        box.setAlignment(Pos.CENTER);

        var comp = this;
        int number = 1;
        for (var entry : comp.getEntries()) {
            VBox element = new VBox();
            element.setFillWidth(true);
            element.setAlignment(Pos.CENTER);
            var label = new Label(entry.name().get());
            label.getStyleClass().add("label");
            element.getChildren().add(label);
            element.getStyleClass().add("entry");

            var line = new Region();
            boolean first = number == 1;
            boolean last = number == comp.getEntries().size();
            line.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> element.getWidth() / ((first || last) ? 2 : 1), element.widthProperty()));
            line.setMinWidth(0);
            line.getStyleClass().add("line");
            var lineBox = new HBox(line);
            lineBox.setFillHeight(true);
            if (first) {
                lineBox.setAlignment(Pos.CENTER_RIGHT);
            } else if (last) {
                lineBox.setAlignment(Pos.CENTER_LEFT);
            } else {
                lineBox.setAlignment(Pos.CENTER);
            }

            var circle = new Region();
            circle.getStyleClass().add("circle");
            var numberLabel = new Label("" + number);
            numberLabel.getStyleClass().add("number");
            var stack = new StackPane();
            stack.getChildren().add(lineBox);
            stack.getChildren().add(circle);
            stack.getChildren().add(numberLabel);
            stack.setAlignment(Pos.CENTER);
            element.getChildren().add(stack);

            comp.valueProperty().addListener((c, o, n) -> {
                element.pseudoClassStateChanged(CURRENT, comp.isCurrent(entry));
                element.pseudoClassStateChanged(NEXT, comp.isNext(entry));
                element.pseudoClassStateChanged(COMPLETED, comp.isCompleted(entry));
            });
            if (comp.getCompIndex() == number - 1) {
                element.pseudoClassStateChanged(CURRENT, true);
            } else {
                element.pseudoClassStateChanged(NEXT, true);
            }

            box.getChildren().add(element);

            element.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> content.getWidth() / comp.getEntries().size(), content.widthProperty()));

            number++;
        }

        return box;
    }

    protected Region createStepNavigation() {
        MultiStepComp comp = this;

        HBox buttons = new HBox();
        buttons.getStyleClass().add("buttons");
        buttons.setSpacing(5);

        var help = new JFXButton("Help");
        help.getStyleClass().add("help");
        help.disableProperty().bind(Bindings.createBooleanBinding(() -> value.getValue().help == null, valueProperty()));
        help.setOnAction(e -> value.getValue().help.run());
        buttons.getChildren().add(help);

        var spacer = new Region();
        buttons.getChildren().add(spacer);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttons.setAlignment(Pos.CENTER_RIGHT);
        var nextText = Bindings.createStringBinding(() -> isLastPage() ? "Finish" : "Next >", valueProperty());
        var next = new JFXButton();
        next.getStyleClass().add("next");
        next.textProperty().bind(nextText);
        next.setOnAction(e -> {
            if (comp.isLastPage()) {
                comp.finish();
            } else {
                comp.next();
            }
        });
        var previous = new JFXButton("< Previous");
        previous.getStyleClass().add("previous");
        previous.disableProperty().bind(Bindings.createBooleanBinding(this::isFirstPage, valueProperty()));
        previous.setOnAction(e -> {
            comp.previous();
        });
        buttons.getChildren().addAll(previous, next);
        return buttons;
    }

    @Override
    public CompStructure<VBox> createBase() {
        this.entries = setup();
        this.set(entries.get(0).comp.get());

        VBox content = new VBox();
        var comp = this;
        Region box = createStepOverview(content);

        var compContent = new JFXTabPane();
        compContent.getStyleClass().add("content");
        for (var entry : comp.getEntries()) {
            compContent.getTabs().add(new Tab(null, null));
        }
        compContent.getTabs().set(0, new Tab(null, comp.getValue().createRegion()));

        content.getChildren().addAll(box, compContent, createStepNavigation());
        content.getStyleClass().add("multi-step-comp");
        content.setFillWidth(true);
        VBox.setVgrow(compContent, Priority.ALWAYS);
        comp.valueProperty().addListener((c, o, n) -> {
            compContent.getTabs().get(comp.getCompIndex()).setContent(n.createRegion());
            compContent.getSelectionModel().select(comp.getCompIndex());
        });
        return new CompStructure<>(content);
    }

    protected abstract List<Entry> setup();

    protected abstract void finish();

    public List<Entry> getEntries() {
        return entries;
    }

    public static record Entry(Supplier<String> name, Supplier<Step> comp) {
    }
}
