package site.remlit.policysync;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.remlit.aster.model.plugin.AsterPlugin;
import site.remlit.policysync.model.Configuration;
import site.remlit.policysync.service.ConfigService;
import site.remlit.policysync.service.PullService;

public class PolicySync implements AsterPlugin {

    @Override
    public @NotNull Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    private Thread pullThread;

    @Override
    public void enable() {
        ConfigService.initialize();

        pullThread = new Thread(() -> {
            try {
                while (!pullThread.isInterrupted()) {
                    // Converts frequency to number of minutes
                    PullService.pull();
                    Thread.sleep(ConfigService.getConfig().getFrequency() * (60L * 1000L));
                }
            } catch (InterruptedException ignored) {
            }
        });
        pullThread.setName("PolicySync Pull");
        pullThread.start();

        getLogger().info("Started pull thread, running every {} minutes", ConfigService.getConfig().getFrequency());

        /*
        there's no policy creation event???

        getLogger().debug("Registering policy creation listener...");
        EventRegistry.addListener();
        * */
    }

    @Override
    public void disable() {
        pullThread.interrupt();
        getLogger().info("Stopped pull thread");
    }

}
