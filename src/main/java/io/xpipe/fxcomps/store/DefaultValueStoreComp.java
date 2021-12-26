package io.xpipe.fxcomps.store;

import io.xpipe.fxcomps.CompStructure;

public abstract class DefaultValueStoreComp<S extends CompStructure<?>, T> extends ValueStoreComp<S, T> {

    private final T defaultVal;

    public DefaultValueStoreComp(T defaultVal) {
        this.defaultVal = defaultVal;
        this.value.setValue(defaultVal);
    }

    @Override
    protected void setDefault() {
        this.value.setValue(defaultVal);
    }
}
