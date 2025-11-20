package top.polar.example.hook;

import org.bukkit.event.Listener;
import top.polar.api.PolarApi;
import top.polar.api.PolarApiAccessor;
import top.polar.api.command.Subcommands;
import top.polar.api.event.listener.repository.EventListenerRepository;
import top.polar.api.exception.PolarNotLoadedException;
import top.polar.api.user.event.DetectionAlertEvent;
import top.polar.example.ApiExample;
import top.polar.example.commands.LogsSubcommand;
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

            // Subcommand API usage
            Subcommands subcommands = polarApi.subcommands();

            // Register custom subcommand
            subcommands.registerSubcommand(
                    new LogsSubcommand("test", false, "polar.command.test"),
                    ApiExample.getPlugin(ApiExample.class)
            );

            // Modify Polar subcommands
            subcommands.polarSubcommands().get("mitigations")
                    .hidden(true) // hide from tab-complete
                    .label("m"); // change label to m

            subcommands.polarSubcommands().get("monitor")
                    .enabled(false); // disable

            // Register example event
            EventListenerRepository eventRepository = polarApi.events().repository();
            eventRepository.registerListener(DetectionAlertEvent.class, new ExampleDetectionAlertListener());

            System.out.println("This server's ID is " + polarApi.server().uuid());
        } catch (PolarNotLoadedException __) {
            logger.severe("API access violation - Polar Anticheat is not loaded");
        }
    }

}
