package top.polar.example.hook;

import top.polar.api.PolarApi;
import top.polar.api.PolarApiAccessor;
import top.polar.api.event.listener.repository.EventListenerRepository;
import top.polar.api.exception.PolarNotLoadedException;
import top.polar.api.user.event.DetectionAlertEvent;
import top.polar.api.user.event.type.CheckType;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;

/*
 https://docs.polar.top/api-overview.html

 ****
 The developers cannot guarantee functionality if the API is not used
 as instructed by our documentation or the comments present in this file.
 ****
 */
public class PolarApiHook implements Runnable {

    private final Logger logger;

    public PolarApiHook(Logger logger) {
        this.logger = logger;
    }

    // This method will be executed by the loader after Polar has been loaded. It must not be called from anywhere else.
    @Override
    public void run() {
        try {
            // PolarApiAccessor and all other Polar classes can only be accessed after Polar has been loaded.
            WeakReference<PolarApi> weakApi = PolarApiAccessor.access();
            PolarApi polarApi = weakApi.get();

            logger.info("Successfully hooked into Polar");

            // Register events
            EventListenerRepository eventRepository = polarApi.events().repository();
            eventRepository.registerListener(DetectionAlertEvent.class, event -> {
                CheckType checkType = event.check().type();
                System.out.println(event.user().username() + " flagged " + checkType);
            });
        } catch (PolarNotLoadedException __) {
            logger.severe("API access violation - Polar Anticheat is not loaded");
        }
    }
}
