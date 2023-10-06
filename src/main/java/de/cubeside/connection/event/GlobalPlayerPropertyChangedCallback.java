package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalPlayerPropertyChangedCallback {
    Event<GlobalPlayerPropertyChangedCallback> EVENT = EventFactory.createArrayBacked(GlobalPlayerPropertyChangedCallback.class,
            (listeners) -> (server, player, property, value) -> {
                for (GlobalPlayerPropertyChangedCallback listener : listeners) {
                    listener.onData(server, player, property, value);
                }
            });

    public void onData(GlobalServer server, GlobalPlayer player, String property, String value);
}
