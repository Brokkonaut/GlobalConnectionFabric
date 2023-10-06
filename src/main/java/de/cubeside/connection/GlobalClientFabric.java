package de.cubeside.connection;

import com.mojang.authlib.GameProfile;
import de.cubeside.connection.event.GlobalDataCallback;
import de.cubeside.connection.event.GlobalPlayerDisconnectedCallback;
import de.cubeside.connection.event.GlobalPlayerJoinedCallback;
import de.cubeside.connection.event.GlobalServerConnectedCallback;
import de.cubeside.connection.event.GlobalServerDisconnectedCallback;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class GlobalClientFabric extends GlobalClient {
    // private final GlobalClientMod plugin;
    private boolean stoppingServer;

    protected final ArrayDeque<Runnable> tasks = new ArrayDeque<>();
    protected final Object sync = new Object();
    protected boolean running = true;
    private MinecraftServer minecraftServer;

    private static GlobalClientFabric instance;

    public GlobalClientFabric(GlobalClientMod connectionPlugin, MinecraftServer minecraftServer) {
        super(null);

        // plugin = connectionPlugin;
        this.minecraftServer = minecraftServer;

        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::onPlayerQuit);

        instance = this;
    }

    @Override
    public void setServer(String host, int port, String account, String password) {
        GlobalClientFabric.super.setServer(host, port, account, password);
        if (minecraftServer.getPlayerList() != null) {
            for (ServerPlayer p : minecraftServer.getPlayerList().getPlayers()) {
                onPlayerOnline(p.getUUID(), p.getName().getString(), System.currentTimeMillis());
            }
        }
    }

    @Override
    protected void runInMainThread(Runnable r) {
        if (!stoppingServer) {
            minecraftServer.execute(r);
        }
    }

    @Override
    protected void processData(GlobalServer source, String channel, GlobalPlayer targetPlayer, GlobalServer targetServer, byte[] data) {
        // GlobalClientMod.LOGGER.debug("processData: " + channel);
        GlobalDataCallback.EVENT.invoker().onGlobalData(source, targetPlayer, channel, data);
    }

    public void onPlayerJoin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        GameProfile p = handler.getOwner();
        // GlobalClientMod.LOGGER.debug("Player join: " + p.getName().getString());
        GlobalPlayer existing = getPlayer(p.getId());
        if (existing == null || !existing.isOnServer(getThisServer())) {
            onPlayerOnline(p.getId(), p.getName(), System.currentTimeMillis());
        }
    }

    public void onPlayerQuit(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        // GlobalClientMod.LOGGER.debug("Player quit: " + p.getName().getString());
        GlobalPlayer existing = getPlayer(handler.getOwner().getId());
        if (existing != null && existing.isOnServer(getThisServer())) {
            onPlayerOffline(existing.getUniqueId());
        }
    }

    @Override
    protected void onPlayerJoined(GlobalServer server, GlobalPlayer player, boolean joinedTheNetwork) {
        // GlobalClientMod.LOGGER.debug("onPlayerJoined: " + player.getName());
        GlobalPlayerJoinedCallback.EVENT.invoker().onJoin(server, player, joinedTheNetwork);
    }

    @Override
    protected void onPlayerDisconnected(GlobalServer server, GlobalPlayer player, boolean leftTheNetwork) {
        // GlobalClientMod.LOGGER.debug("onPlayerDisconnected: " + player.getName());
        GlobalPlayerDisconnectedCallback.EVENT.invoker().onDisconnect(server, player, leftTheNetwork);
    }

    @Override
    protected void onServerConnected(GlobalServer server) {
        // GlobalClientMod.LOGGER.debug("onServerConnected: " + server.getName());
        GlobalServerConnectedCallback.EVENT.invoker().onConnect(server);
    }

    @Override
    protected void onServerDisconnected(GlobalServer server) {
        // GlobalClientMod.LOGGER.debug("onServerDisconnected: " + server.getName());
        GlobalServerDisconnectedCallback.EVENT.invoker().onDisconnect(server);
    }

    @Override
    public void shutdown() {
        this.stoppingServer = true;
        super.shutdown();
        synchronized (sync) {
            running = false;
            sync.notifyAll();
        }
    }

    public static GlobalClientFabric getInstance() {
        return instance;
    }
}