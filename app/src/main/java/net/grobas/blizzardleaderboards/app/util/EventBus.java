package net.grobas.blizzardleaderboards.app.util;

import com.squareup.otto.Bus;

public class EventBus {

    private static final Bus mBus = new Bus();

    private EventBus() {}

    public static Bus getInstance() {
        return mBus;
    }
}
