package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileDropOverlayComp<T extends CompStructure<?>> extends Comp<FileDropOverlayComp.Structure<T>> {

    @Override
    public Structure<T> createBase() {
        var fileDropOverlay = new StackPane(new FontIcon("mdi2f-file-import"));
        fileDropOverlay.setOpacity(1.0);
        fileDropOverlay.setAlignment(Pos.CENTER);
        fileDropOverlay.getStyleClass().add("file-drop-comp");
        fileDropOverlay.setVisible(false);

        var compBase = comp.createStructure();
        var contentStack = new StackPane(compBase.get(), fileDropOverlay);
        setupDragAndDrop(contentStack, fileDropOverlay);
        return new Structure<>(contentStack, compBase);
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    public static class Structure<T extends CompStructure<?>> extends CompStructure<StackPane> {
        T compStructure;

        public Structure(StackPane value, T compStructure) {
            super(value);
            this.compStructure = compStructure;
        }
    }

    private final Comp<T> comp;
    private final Consumer<List<Path>> fileConsumer;

    public FileDropOverlayComp(Comp<T> comp, Consumer<List<Path>> fileConsumer) {
        this.comp = comp;
        this.fileConsumer = fileConsumer;
    }

    private void setupDragAndDrop(StackPane stack, Node overlay) {
        stack.setOnDragOver(event -> {
            if (event.getGestureSource() == null && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        stack.setOnDragEntered(event -> {
            if (event.getGestureSource() == null && event.getDragboard().hasFiles()) {
                overlay.setVisible(true);
            }
            event.consume();
        });

        stack.setOnDragExited(event -> {
            overlay.setVisible(false);
            event.consume();
        });

        stack.setOnDragDropped(event -> {
            // Only accept drops from outside the app window
            if (event.getGestureSource() == null && event.getDragboard().hasFiles()) {
                event.setDropCompleted(true);
                Dragboard db = event.getDragboard();
                var list = db.getFiles().stream().map(File::toPath).toList();
                fileConsumer.accept(list);
            }
            event.consume();
        });
    }
}
