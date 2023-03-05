package pw.twpi.whitelistsync2.config;

import com.google.gson.annotations.SerializedName;

public enum DatabaseMode {
    @SerializedName("MYSQL")
    MYSQL,

    @SerializedName("SQLITE")
    SQLITE
}
