package net.dzikoysk.funnyguilds.hook;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class PluginHook {

    public static final String PLUGIN_FUNNYTAB           = "FunnyTab";
    public static final String PLUGIN_WORLDGUARD         = "WorldGuard";
    public static final String PLUGIN_VAULT              = "Vault";
    public static final String PLUGIN_PLACEHOLDERAPI     = "PlaceholderAPI";
    public static final String PLUGIN_BUNGEETABLISTPLUS  = "BungeeTabListPlus";
    public static final String PLUGIN_MVDWPLACEHOLDERAPI = "MVdWPlaceholderAPI";

    private static final List<String> HOOK_LIST = new ArrayList<>();

    public static void init() {
        tryInit(PLUGIN_FUNNYTAB, () -> {
            FunnyTabHook.initFunnyDisabler();
            return true;
        }, false);

        tryInit(PLUGIN_WORLDGUARD, () -> {
            try {
                Class.forName("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");
                Class.forName("com.sk89q.worldguard.protection.flags.Flag");

                if (Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion().startsWith("7")) {
                    FunnyGuilds.getInstance().getPluginLogger().warning("Support for WorldGuard v7.0.0 or newer is currently unavailable");
                    return false;
                }
                else {
                    WorldGuardHook.initWorldGuard();
                    return true;
                }
            }
            catch (final ClassNotFoundException exception) {
                FunnyGuilds.getInstance().getPluginLogger().warning("FunnyGuilds supports only WorldGuard v6.2 or newer");
                return false;
            }
        });

        tryInit(PLUGIN_BUNGEETABLISTPLUS, () -> {
            try {
                Class.forName("codecrafter47.bungeetablistplus.api.bukkit.Variable");
                BungeeTabListPlusHook.initVariableHook();

                return true;
            }
            catch (final ClassNotFoundException exception) {
                return false;
            }
        });

        tryInit(PLUGIN_MVDWPLACEHOLDERAPI, () -> {
            try {
                Class.forName("be.maximvdw.placeholderapi.PlaceholderReplacer");
                MVdWPlaceholderAPIHook.initPlaceholderHook();

                return true;
            }
            catch (final ClassNotFoundException exception) {
                return false;
            }
        });

        tryInit(PLUGIN_VAULT, () -> {
            VaultHook.initEconomyHook();
            return true;
        });

        tryInit(PLUGIN_PLACEHOLDERAPI, () -> {
            PlaceholderAPIHook.initPlaceholderHook();
            return true;
        });
    }

    public static void tryInit(final String plugin, final Supplier<Boolean> init, final boolean notifyIfMissing) {
        if (Bukkit.getPluginManager().getPlugin(plugin) != null) {
            if (! init.get()) {
                return;
            }

            HOOK_LIST.add(plugin);
        }
        else if (notifyIfMissing) {
            FunnyGuilds.getInstance().getPluginLogger().info(plugin + " plugin could not be found, some features may not be available");
        }
    }

    public static void tryInit(final String plugin, final Supplier<Boolean> init) {
        tryInit(plugin, init, true);
    }

    public static boolean isPresent(final String plugin) {
        return HOOK_LIST.contains(plugin);
    }

    private PluginHook() {
    }

}
