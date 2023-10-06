package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalPlayerJoinedCallback {
    Event<GlobalPlayerJoinedCallback> EVENT = EventFactory.createArrayBacked(GlobalPlayerJoinedCallback.class,
            (listeners) -> (server, player, joinedTheNetwork) -> {
                for (GlobalPlayerJoinedCallback listener : listeners) {
                    listener.onJoin(server, player, joinedTheNetwork);
                }
            });

    public void onJoin(GlobalServer server, GlobalPlayer player, boolean joinedTheNetwork);
}
