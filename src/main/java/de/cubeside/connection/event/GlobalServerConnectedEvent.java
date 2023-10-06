package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalServerConnectedEvent {
    Event<GlobalServerConnectedEvent> EVENT = EventFactory.createArrayBacked(GlobalServerConnectedEvent.class,
            (listeners) -> (server) -> {
                for (GlobalServerConnectedEvent listener : listeners) {
                    listener.onConnect(server);
                }
            });

    public void onConnect(GlobalServer server);
}
