package io.xpipe.fxcomps;

import javafx.scene.layout.Region;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode
@AllArgsConstructor
@SuperBuilder
@ToString
public class CompStructure<R extends Region> {

    private final R value;

    public R get() {
        return value;
    }
}
