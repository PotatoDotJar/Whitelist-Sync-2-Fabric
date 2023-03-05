package pw.twpi.whitelistsync2.config;

import com.google.gson.annotations.SerializedName;

public class ConfigData {
    // General Settings
    @SerializedName("DATABASE_MODE")
    public DatabaseMode DATABASE_MODE = DatabaseMode.SQLITE;
    public boolean SYNC_OP_LIST = false;
    public int SYNC_TIMER = 60;

    // MYSQL Settings
    public String MYSQL_DB_NAME = "WhitelistSync";
    public String MYSQL_IP = "localhost";
    public int MYSQL_PORT = 3306;
    public String MYSQL_USERNAME = "root";
    public String MYSQL_PASSWORD = "password";

    // SQLITE Settings
    public String SQLITE_DATABASE_PATH = "./whitelistSync.db";
}
