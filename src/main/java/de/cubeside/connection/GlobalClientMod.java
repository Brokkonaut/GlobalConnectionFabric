package de.cubeside.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GlobalClientMod implements ModInitializer {
    public static final String MODID = "globalconnectionfabric";

    public static final Logger LOGGER = LogManager.getLogger();
    private GlobalClientFabric globalClient;
    private GlobalClientConfig config;

    private PlayerMessageImplementation messageAPI;

    private PlayerPropertiesImplementation propertiesAPI;

    private static GlobalClientMod instance;

    public GlobalClientMod() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getConfigDir().toFile().mkdirs();
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "globalclient.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8)) {
                config = gson.fromJson(reader, GlobalClientConfig.class);
            } catch (Exception e) {
                LOGGER.error("Could not load GlobalClient config", e);
            }
        }
        boolean saveConfig = false;
        if (config == null) {
            config = new GlobalClientConfig();
            saveConfig = true;
        }
        if (config.initDefaultValues()) {
            saveConfig = true;
        }
        if (saveConfig) {
            try (FileWriter writer = new FileWriter(configFile, StandardCharsets.UTF_8)) {
                gson.toJson(config, writer);
            } catch (Exception e) {
                LOGGER.error("Could not save GlobalClient config", e);
            }
        }

        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopping);
    }

    public void onServerStarting(MinecraftServer server) {
        globalClient = new GlobalClientFabric(this, server);
        globalClient.setServer(config.getHostname(), config.getPort(), config.getUser(), config.getPassword());

        messageAPI = new PlayerMessageImplementation(this, server);
        propertiesAPI = new PlayerPropertiesImplementation(this, server);
    }

    public void onServerStopping(MinecraftServer server) {
        if (globalClient != null) {
            globalClient.shutdown();
            globalClient = null;
        }
    }

    public GlobalClientFabric getConnectionAPI() {
        return globalClient;
    }

    public PlayerMessageImplementation getMessageAPI() {
        return messageAPI;
    }

    public PlayerPropertiesImplementation getPropertiesAPI() {
        return propertiesAPI;
    }

    public static GlobalClientMod getInstance() {
        return instance;
    }
}
