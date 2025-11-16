package top.polar.example.hook;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.polar.api.PolarApi;
import top.polar.api.PolarApiAccessor;
import top.polar.api.event.listener.repository.EventListenerRepository;
import top.polar.api.exception.PolarNotLoadedException;
import top.polar.api.user.User;
import top.polar.api.user.event.DetectionAlertEvent;
import top.polar.example.listeners.ExampleDetectionAlertListener;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;

/*
 https://docs.polar.top/api-overview.html

 ****
 The developers cannot guarantee functionality if the API is not used
 as shown by the code in this example or the comments present in the files.
 ****
 */
public class PolarApiHook implements Runnable, Listener {

    private final Logger logger;
    private PolarApi polarApi;

    public PolarApiHook(Logger logger) {
        this.logger = logger;
    }

    // This method will be executed by the loader after Polar has been loaded. It must not be called from anywhere else.
    @Override
    public void run() {
        try {
            // PolarApiAccessor and all other Polar classes can only be accessed after Polar has been loaded.
            WeakReference<PolarApi> weakApi = PolarApiAccessor.access();
            polarApi = weakApi.get();

            logger.info("Successfully hooked into Polar");

            // Register example event
            EventListenerRepository eventRepository = polarApi.events().repository();
            eventRepository.registerListener(DetectionAlertEvent.class, new ExampleDetectionAlertListener());

            System.out.println("This server ID is " + polarApi.server().uuid());
        } catch (PolarNotLoadedException __) {
            logger.severe("API access violation - Polar Anticheat is not loaded");
        }
    }

    // This is an example listener and has not been registered yet.
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (polarApi == null) {
            // This should never be true, but just in case.
            return;
        }

        // Increase the unstable connection limit and allow health indicators for user "libdeflate" only.
        polarApi.userRepository()
                .queryUserByUuid(event.getPlayer().getUniqueId())
                .filter(user -> user.username().equals("libdeflate"))
                .map(User::configOverride)
                .ifPresent(configOverride -> {
                    configOverride.unstableConnectionLimit(10_000);
                    configOverride.healthFilter(false);
                });
    }
}
