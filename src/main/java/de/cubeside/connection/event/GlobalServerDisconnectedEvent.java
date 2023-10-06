package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalServerDisconnectedEvent {
    Event<GlobalServerDisconnectedEvent> EVENT = EventFactory.createArrayBacked(GlobalServerDisconnectedEvent.class,
            (listeners) -> (server) -> {
                for (GlobalServerDisconnectedEvent listener : listeners) {
                    listener.onDisconnect(server);
                }
            });

    public void onDisconnect(GlobalServer server);
}
