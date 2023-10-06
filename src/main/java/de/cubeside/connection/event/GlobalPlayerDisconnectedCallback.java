package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalPlayerDisconnectedCallback {
    Event<GlobalPlayerDisconnectedCallback> EVENT = EventFactory.createArrayBacked(GlobalPlayerDisconnectedCallback.class,
            (listeners) -> (server, player, leftTheNetwork) -> {
                for (GlobalPlayerDisconnectedCallback listener : listeners) {
                    listener.onDisconnect(server, player, leftTheNetwork);
                }
            });

    public void onDisconnect(GlobalServer server, GlobalPlayer player, boolean leftTheNetwork);
}
