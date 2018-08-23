package com.merabreak.models;

/**
 * Created by Saya Godshala on 1/13/2016.
 */
public class APIResponse<T> {
    private Meta meta;
    private T values;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public T getValues() {
        return values;
    }

    public void setValues(T values) {
        this.values = values;
    }
}
