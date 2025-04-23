package com.bm.travelcore.populator;

public interface Populator<S, T> {
    void populate(S source, T target);
}