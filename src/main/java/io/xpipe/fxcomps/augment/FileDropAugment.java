package io.xpipe.fxcomps.augment;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileDropAugment implements Augment {

    private final Consumer<List<Path>> fileConsumer;

    public FileDropAugment(Consumer<List<Path>> fileConsumer) {
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
        });

        stack.setOnDragExited(event -> {
            overlay.setVisible(false);
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

    @Override
    public Region augment(Region r) {
        var fileDropOverlay = new StackPane(new FontIcon("mdi2f-file-import"));
        fileDropOverlay.setOpacity(1.0);
        fileDropOverlay.setAlignment(Pos.CENTER);
        fileDropOverlay.getStyleClass().add("file-drop-comp");
        fileDropOverlay.setVisible(false);

        var contentStack = new StackPane(r, fileDropOverlay);
        setupDragAndDrop(contentStack, fileDropOverlay);
        return contentStack;
    }
}
