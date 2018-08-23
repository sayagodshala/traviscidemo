package com.merabreak;

public class BusProvider {
    private static final MainThreadBus BUS = new MainThreadBus();

    public static MainThreadBus bus() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
