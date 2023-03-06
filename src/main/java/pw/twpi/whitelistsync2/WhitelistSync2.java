package pw.twpi.whitelistsync2;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.rmnad.minecraft.forge.whitelistsynclib.services.BaseService;
import net.rmnad.minecraft.forge.whitelistsynclib.services.MySqlService;
import net.rmnad.minecraft.forge.whitelistsynclib.services.SqLiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.twpi.whitelistsync2.config.ConfigData;
import pw.twpi.whitelistsync2.config.ConfigManager;
import pw.twpi.whitelistsync2.services.WhitelistSyncThread;

public class WhitelistSync2 implements ModInitializer {
	public static final String MODID = "whitelistsync2";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static String SERVER_FILEPATH;

	// Database Service
	public static BaseService whitelistService;
	public static ConfigData Config;

	public WhitelistSync2() {
		LOGGER.info("Hello from Whitelist Sync 2!");
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register((s) -> {
			// Register config
			if (ConfigManager.loadConfig()) {
				Config = ConfigManager.getConfig();
				SetupWhitelistSync(s);
			} else {
				LOGGER.error("Failed to load Whitelist Sync 2 config");
			}
		});
	}

	public static void SetupWhitelistSync(MinecraftServer server) {
		boolean errorOnSetup = false;

		// Server filepath
		SERVER_FILEPATH = server.getRunDirectory().getPath();

		LOGGER.info("----------------------------------------------");
		LOGGER.info("---------------WHITELIST SYNC 2---------------");
		LOGGER.info("----------------------------------------------");

		switch (Config.DATABASE_MODE) {
			case SQLITE:
				whitelistService = new SqLiteService(Config.SQLITE_DATABASE_PATH, Config.SYNC_OP_LIST);
				break;
			case MYSQL:
				whitelistService = new MySqlService(
						Config.MYSQL_DB_NAME,
						Config.MYSQL_IP,
						Config.MYSQL_PORT,
						Config.MYSQL_USERNAME,
						Config.MYSQL_PASSWORD,
						Config.SYNC_OP_LIST
				);
				break;
			default:
				LOGGER.error("Please check what WHITELIST_MODE is set in the config and make sure it is set to a supported mode.");
				errorOnSetup = true;
				break;
		}

		StartWhitelistSyncThread(server, whitelistService, errorOnSetup);

		LOGGER.info("----------------------------------------------");
		LOGGER.info("----------------------------------------------");
		LOGGER.info("----------------------------------------------");
	}

	public static void StartWhitelistSyncThread(MinecraftServer server, BaseService service, boolean errorOnSetup) {
		WhitelistSyncThread syncThread = new WhitelistSyncThread(server, service, Config.SYNC_OP_LIST, errorOnSetup);
		syncThread.start();
		LOGGER.info("WhitelistSync Thread Started!");
	}
}
