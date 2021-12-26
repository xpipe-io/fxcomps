package io.xpipe.fxcomps.util;

import javafx.application.Platform;

public class PlatformUtil {

    public static void runLaterIfNeeded(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }
}
