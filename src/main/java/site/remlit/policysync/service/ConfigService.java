package site.remlit.policysync.service;

import kotlinx.serialization.json.Json;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.remlit.policysync.model.Configuration;
import site.remlit.policysync.util.KtSerialization;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigService {

    private static @NotNull Logger getLogger() {
        return LoggerFactory.getLogger(ConfigService.class);
    }

    public static Path CONFIG_PATH = Path.of("plugins/policysync/config.json");

    public static void initialize() {
        if (!Files.exists(CONFIG_PATH)) {
            InputStream stream = ConfigService.class.getClassLoader().getResourceAsStream("config.json");

            try {
                Files.createDirectories(CONFIG_PATH.getParent());
            } catch (IOException e) {
                getLogger().debug("Failed to create parent directory", e);
            }

            try {
                Files.write(CONFIG_PATH, stream.readAllBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                getLogger().debug("Failed to create configuration file", e);
            }
        }
    }

    public static @NotNull Configuration getConfig() {
        String configString;

        try {
            configString = Files.readString(CONFIG_PATH);
        } catch (IOException e) {
            getLogger().error("Failed to read configuration file", e);
            return new Configuration();
        }

        return KtSerialization.serialize(Configuration.class, configString);
    }

}
