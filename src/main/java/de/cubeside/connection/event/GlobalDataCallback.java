package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GlobalDataCallback {
    Event<GlobalDataCallback> EVENT = EventFactory.createArrayBacked(GlobalDataCallback.class,
            (listeners) -> (source, targetPlayer, channel, data) -> {
                for (GlobalDataCallback listener : listeners) {
                    listener.onGlobalData(source, targetPlayer, channel, data);
                }
            });

    public void onGlobalData(GlobalServer source, GlobalPlayer targetPlayer, String channel, byte[] data);
}
