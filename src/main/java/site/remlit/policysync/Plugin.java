package site.remlit.policysync;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.remlit.aster.model.plugin.AsterPlugin;
import site.remlit.aster.registry.EventRegistry;
import site.remlit.policysync.service.ConfigService;
import site.remlit.policysync.service.PullService;

public class Plugin implements AsterPlugin {

    @Override
    public @NotNull Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void enable() {
        getLogger().debug("Starting pull thread...");

        Thread pullThread = new Thread(() -> {
            try {
                // Converts frequency to number of minutes
                Thread.sleep(ConfigService.getConfig().getFrequency() * (60L * 1000L));
                PullService.pull();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        pullThread.setName("PolicySync Pull");
        pullThread.start();

        /*
        there's no policy creation event???

        getLogger().debug("Registering policy creation listener...");
        EventRegistry.addListener();
        * */
    }

    @Override
    public void disable() {

    }

}
