package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalServerDisconnectedCallback {
    Event<GlobalServerDisconnectedCallback> EVENT = EventFactory.createArrayBacked(GlobalServerDisconnectedCallback.class,
            (listeners) -> (server) -> {
                for (GlobalServerDisconnectedCallback listener : listeners) {
                    listener.onDisconnect(server);
                }
            });

    public void onDisconnect(GlobalServer server);
}
