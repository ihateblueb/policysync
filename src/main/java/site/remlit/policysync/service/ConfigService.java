package site.remlit.policysync.service;

import site.remlit.policysync.model.Configuration;
import site.remlit.policysync.model.PolicySource;

import java.util.Collections;
import java.util.List;

public class ConfigService {

    public static Configuration getConfig() {
        return new Configuration(
                Collections.emptyList(),
                120 // 2 hours
        );
    }

}
