package site.remlit.policysync.service;

import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.remlit.policysync.model.PolicySource;
import site.remlit.policysync.model.PolicySourceType;
import site.remlit.policysync.model.iceshrimp.IceshrimpHost;
import site.remlit.policysync.util.KtSerialization;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class PullService {

    private static @NotNull Logger getLogger() {
        return LoggerFactory.getLogger(PullService.class);
    }

    public static void pull() {
        for (PolicySource source : ConfigService.getConfig().getSources()) {
            try {
                switch (source.getType()) {
                    case PolicySourceType.Iceshrimp -> pullIceshrimp(source);
                    default -> throw new IllegalStateException("Unknown policy source type " + source.getType());
                }
            } catch (Exception e) {
                getLogger().error("Pull failed.", e);
            }
        }
    }

    public static final Duration TIMEOUT = Duration.ofSeconds(60 * 2);

    public static void pullIceshrimp(
            @NotNull PolicySource source
    ) throws URISyntaxException,
            IOException,
            UncheckedIOException,
            InterruptedException,
            ClassNotFoundException
    {
        // GET /api/iceshrimp/admin/instances/blocked
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest blockedReq = HttpRequest.newBuilder()
                .uri(new URI(source.getUrl() + "api/iceshrimp/admin/instances/blocked"))
                .timeout(TIMEOUT)
                .header("Authorization", "Bearer " + source.getToken())
                .GET()
                .build();

        HttpResponse<String> blockedRes = client.send(blockedReq, HttpResponse.BodyHandlers.ofString());
        List<IceshrimpHost> blockedHosts = KtSerialization.serialize(IceshrimpHost.Companion.listSerializer(), blockedRes.body());

        for (IceshrimpHost host : blockedHosts) {
            getLogger().info("hit host "+host);
        }
    }

}
