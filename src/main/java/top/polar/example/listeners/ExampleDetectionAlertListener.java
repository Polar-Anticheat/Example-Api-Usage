package top.polar.example.listeners;

import top.polar.api.user.event.DetectionAlertEvent;
import top.polar.api.user.event.type.CheckType;

import java.util.function.Consumer;

/*
 https://docs.polar.top/api-overview.html#important-nuances-regarding-events

 ****
 The developers cannot guarantee functionality if the API is not used
 as shown by the code in this example or the comments present in the files.
 ****
 */
public class ExampleDetectionAlertListener implements Consumer<DetectionAlertEvent> {

    @Override
    public void accept(DetectionAlertEvent event) {
        CheckType checkType = event.check().type();
        long latency = event.user().connection().latency();
        System.out.printf("%s flagged %s (ping: %d)%n", event.user().username(), checkType, latency);
    }
}
