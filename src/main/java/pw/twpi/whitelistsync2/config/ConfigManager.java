package pw.twpi.whitelistsync2.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import pw.twpi.whitelistsync2.WhitelistSync2;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    private static ConfigData CONFIG;
    private static boolean ENABLED = false;

    public static ConfigData getConfig() {
        return CONFIG;
    }

    public static boolean isEnabled() {
        return ENABLED;
    }

    public static boolean loadConfig() {
        ENABLED = false;

        CONFIG = null;
        try {
            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("whitelistsync2");
            File configFile = Paths.get(configPath., "whitelistsync2.json");

            ConfigData config;

            if (!configFile.exists()) {
                WhitelistSync2.LOGGER.info("Creating new whitelist sync file");
                config = CreateNewConfig(configFile);
            } else {
                config = GSON.fromJson(new InputStreamReader(new FileInputStream(configFile), "UTF-8"), ConfigData.class);
            }

            CONFIG = config;
            ENABLED = true;
        } catch(Throwable exception) {
            ENABLED = false;
            WhitelistSync2.LOGGER.error("Failed to read whitelistsync2 config.");
            exception.printStackTrace();
        }

        return ENABLED;
    }

    private static ConfigData CreateNewConfig(File configFile) throws IOException {
        configFile.mkdir();

        ConfigData newConfig = new ConfigData();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
        writer.write(GSON.toJson(newConfig));
        writer.close();

        return newConfig;
    }
}
