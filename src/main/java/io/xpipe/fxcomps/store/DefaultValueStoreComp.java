package io.xpipe.fxcomps.store;

public abstract class DefaultValueStoreComp<T> extends ValueStoreComp<T> {

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
