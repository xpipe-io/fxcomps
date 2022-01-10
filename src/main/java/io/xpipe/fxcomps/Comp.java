package io.xpipe.fxcomps;

import io.xpipe.fxcomps.augment.Augment;
import io.xpipe.fxcomps.comp.WrapperComp;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Comp<S extends CompStructure<?>> {

    public static <R extends Region> Comp<CompStructure<R>> of(Supplier<R> r) {
        return new WrapperComp<>(() -> new CompStructure<>(r.get()));
    }

    public static <S extends CompStructure<?>> Comp<S> ofStructure(Supplier<S> r) {
        return new WrapperComp<>(r);
    }

    @SuppressWarnings("unchecked")
    public static <IR extends Region, SIN extends CompStructure<IR>, OR extends Region> Comp<CompStructure<OR>> derive(
            Comp<SIN> comp, Function<IR, OR> r) {
        return of(() -> r.apply((IR) comp.createRegion()));
    }

    private List<Augment<S>> augments;

    public Comp<S> apply(Augment<S> augment) {
        if (augments == null) {
            augments = new ArrayList<>();
        }
        augments.add(augment);
        return this;
    }

    public Comp<S> styleClass(String styleClass) {
        return apply(struc -> struc.get().getStyleClass().add(styleClass));
    }

    public Comp<S> tooltip(Supplier<String> text) {
        return apply(r -> Tooltip.install(r.get(), new Tooltip(text.get())));
    }

    public Region createRegion() {
        return createStructure().get();
    }

    public S createStructure() {
        S struc = createBase();
        if (augments != null) {
            for (var a : augments) {
                a.augment(struc);
            }
        }
        return struc;
    }

    public abstract S createBase();
}

