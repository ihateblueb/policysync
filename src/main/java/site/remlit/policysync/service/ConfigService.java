package site.remlit.policysync.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.remlit.policysync.model.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigService {

    public static @NotNull Logger getLogger() {
        return LoggerFactory.getLogger(ConfigService.class);
    }

    public static void copyIfNeeded() {
        Path path = Path.of("plugins/policysync/policysync.yaml");
        if (!Files.exists(path)) {
            InputStream stream = ConfigService.class.getClassLoader().getResourceAsStream("policysync.yaml");

            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                getLogger().debug("Failed to create parent directory", e);
            }

            try {
                Files.write(path, stream.readAllBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                getLogger().debug("Failed to create configuration file", e);
            }
        }
    }

    public static @NotNull Configuration getConfig() {
        copyIfNeeded();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File("plugins/policysync.yaml"), Configuration.class);
    }

}
