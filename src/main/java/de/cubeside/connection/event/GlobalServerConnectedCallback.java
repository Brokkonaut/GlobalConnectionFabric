package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalServerConnectedCallback {
    Event<GlobalServerConnectedCallback> EVENT = EventFactory.createArrayBacked(GlobalServerConnectedCallback.class,
            (listeners) -> (server) -> {
                for (GlobalServerConnectedCallback listener : listeners) {
                    listener.onConnect(server);
                }
            });

    public void onConnect(GlobalServer server);
}
