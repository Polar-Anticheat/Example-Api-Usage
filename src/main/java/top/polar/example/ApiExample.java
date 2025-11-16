package top.polar.example;

import org.bukkit.plugin.java.JavaPlugin;
import top.polar.api.loader.LoaderApi;
import top.polar.example.hook.PolarApiHook;

/*
 https://docs.polar.top/api-overview.html

 ****
 The developers cannot guarantee functionality if the API is not used
 as instructed by our documentation or the comments present in this file.
 ****

 This plugin needs to depend on "PolarLoader"

 */
public class ApiExample extends JavaPlugin {

    @Override
    public void onLoad() {
        /*
         Create an instance of PolarApiHook.
         This needs to be in a separate hook class because for some edge cases
         the Java Bytecode Verifier may throw NoClassDefFoundError before
         the classes from the Polar plugin have been loaded.
         */
        PolarApiHook polarApiHook = new PolarApiHook(this.getLogger());
        /*
         This runnable will be called by the loader once it has finished loading the Polar plugin.
         In very rare cases, when using this in onEnable, the callback may register after Polar has been loaded,
         so it needs to be registered in onLoad.
         */
        LoaderApi.registerEnableCallback(polarApiHook);
    }
}
