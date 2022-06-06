package dev.cubxity.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public final class MicroQueueConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Properties properties = new Properties();

    private String brandName = "microqueue";
    private int serverPort = 25565;
    private int compressionThreshold = 0; // 0: Disable
    private int viewDistance = 2;
    private int maxPlayers = 0; // 0: Disable
    private boolean disableGravity = false;

    public void saveDefaults() {
        try {
            File file = new File("server.properties");
            if (!file.exists()) {
                try (FileOutputStream out = new FileOutputStream("server.properties")) {
                    try (InputStream in = getClass().getResourceAsStream("/server.properties")) {
                        byte[] buf = new byte[8192];
                        int length;
                        while ((length = in.read(buf)) > 0) {
                            out.write(buf, 0, length);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to save default configuration", ex);
        }
    }

    public void load() {
        try (FileInputStream stream = new FileInputStream("server.properties")) {
            properties.load(stream);
        } catch (Exception ex) {
            logger.error("Unable to load configuration", ex);
        }

        try {
            brandName = properties.getProperty("brand-name", "microqueue");
            serverPort = getInt("server-port", 25565);
            compressionThreshold = Math.max(getInt("compression-threshold", 0), 0);
            viewDistance = Math.max(getInt("view-distance", 2), 2);
            maxPlayers = Math.max(getInt("max-players", 0), 0);
            disableGravity = Boolean.parseBoolean(properties.getProperty("disable-gravity", "false"));
        } catch (Exception ex) {
            logger.error("Unable to parse configuration", ex);
        }
    }

    public String getBrandName() {
        return brandName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getCompressionThreshold() {
        return compressionThreshold;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isDisableGravity() {
        return disableGravity;
    }

    private int getInt(String key, int def) {
        try {
            String value = properties.getProperty(key);
            if (value != null) {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException ex) {
            logger.warn("Invalid configuration value for key '" + key + "'", ex);
        }

        return def;
    }

}
