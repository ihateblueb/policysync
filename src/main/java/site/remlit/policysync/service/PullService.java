package site.remlit.policysync.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.remlit.aster.common.model.type.PolicyType;
import site.remlit.aster.service.PolicyService;
import site.remlit.policysync.model.PolicySource;
import site.remlit.policysync.model.PolicySourceType;
import site.remlit.policysync.model.iceshrimp.IceshrimpHost;
import site.remlit.policysync.model.misskey.MisskeyAdminMeta;
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

public class PullService {

    private static @NotNull Logger getLogger() {
        return LoggerFactory.getLogger(PullService.class);
    }

    public static List<String> getExistingPolicyHosts(PolicyType policyType) {
        return PolicyService.reducePoliciesToHost(PolicyService.getAllByType(PolicyType.Block));
    }

    public static void createBlock(String host) {
        getLogger().info("Sync added block policy for {}", host);
        PolicyService.create(
                PolicyType.Block,
                host,
                null
        );
    }

    public static void pull() {
        for (PolicySource source : ConfigService.getConfig().getSources()) {
            try {
                switch (source.getType()) {
                    case PolicySourceType.Iceshrimp -> pullIceshrimp(source);
                    case PolicySourceType.Misskey -> pullMisskey(source);
                    default -> throw new IllegalStateException("Unknown policy source type " + source.getType());
                }
            } catch (Exception e) {
                getLogger().error("Pull failed", e);
            }
        }
    }

    public static final Duration TIMEOUT = Duration.ofSeconds(60 * 2);

    public static void pullIceshrimp(
            @NotNull PolicySource source
    ) throws URISyntaxException,
            IOException,
            UncheckedIOException,
            InterruptedException
    {
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
            if (getExistingPolicyHosts(PolicyType.Block).contains(host.getHost())) continue;
            createBlock(host.getHost());
        }

        getLogger().info("Completed pull from {}", source.getUrl());
    }

    public static void pullMisskey(
            @NotNull PolicySource source
    ) throws URISyntaxException,
            IOException,
            UncheckedIOException,
            InterruptedException
    {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest metaReq = HttpRequest.newBuilder()
                .uri(new URI(source.getUrl() + "api/admin/meta"))
                .timeout(TIMEOUT)
                .header("Authorization", "Bearer " + source.getToken())
                .GET()
                .build();

        HttpResponse<String> blockedRes = client.send(metaReq, HttpResponse.BodyHandlers.ofString());
        MisskeyAdminMeta meta = KtSerialization.serialize(MisskeyAdminMeta.class, blockedRes.body());

        for (String host : meta.getBlockedHosts()) {
            if (getExistingPolicyHosts(PolicyType.Block).contains(host)) continue;
            createBlock(host);
        }

        getLogger().info("Completed pull from {}", source.getUrl());
    }
}
