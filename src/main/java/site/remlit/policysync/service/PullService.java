package site.remlit.policysync.service;

import org.jetbrains.annotations.NotNull;
import site.remlit.policysync.model.PolicySource;
import site.remlit.policysync.model.PolicySourceType;

public class PullService {

    public static void pull() {
        for (PolicySource source : ConfigService.getConfig().getSources()) {
            switch (source.getType()) {
                case PolicySourceType.Iceshrimp -> pullIceshrimp(source);
                default -> throw new IllegalStateException("Unknown policy source type " + source.getType());
            }
        }
    }

    public static void pullIceshrimp(
            @NotNull PolicySource source
    ) {
        // /api/iceshrimp/admin/instances/blocked
    }

}
