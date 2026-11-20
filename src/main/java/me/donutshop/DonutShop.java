package me.donutshop;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public final class DonutShop extends JavaPlugin {
    public static final MiniMessage MM = MiniMessage.miniMessage();
    private static final String[] MENUS = {
            "main", "blocks", "end", "nether", "gear", "food", "redstone",
            "shard-main", "shard-spawners", "shard-keys", "shard-tools"
    };

    private Economy economy;
    private ShardManager shards;
    private MenuManager menus;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        for (String m : MENUS) {
            if (!new File(getDataFolder(), "menus/" + m + ".yml").exists()) saveResource("menus/" + m + ".yml", false);
        }
        shards = new ShardManager(this);
        menus = new MenuManager(this);
        menus.load();

        getServer().getPluginManager().registerEvents(menus, this);

        Commands c = new Commands(this);
        for (String n : List.of("shop", "shardshop", "shards")) {
            PluginCommand pc = getCommand(n);
            if (pc != null) {
                pc.setExecutor(c);
                pc.setTabCompleter(c);
            }
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> shards.save(), 6000L, 6000L);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ShardPlaceholders(this).register();
            getLogger().info("Registered PlaceholderAPI placeholders (%donutshop_shards%).");
        } else {
            getLogger().warning("PlaceholderAPI not found, so shard placeholders are unavailable.");
        }
        if (economy() == null) getLogger().warning("Vault/economy not found yet. Money shops need Vault + an economy plugin.");
    }

    @Override
    public void onDisable() {
        if (shards != null) shards.save();
    }

    public void reload() {
        reloadConfig();
        menus.load();
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ShardPlaceholders(this).register();
            getLogger().info("Registered PlaceholderAPI placeholders (%donutshop_shards%).");
        } else {
            getLogger().warning("PlaceholderAPI not found, so shard placeholders are unavailable.");
        }
    }

    public Economy economy() {
        if (economy == null && getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) economy = rsp.getProvider();
        }
        return economy;
    }

    public ShardManager shards() { return shards; }
    public MenuManager menus() { return menus; }

    public void msg(CommandSender to, String key, String... kv) {
        String s = getConfig().getString("messages." + key, key);
        for (int i = 0; i + 1 < kv.length; i += 2) s = s.replace(kv[i], kv[i + 1]);
        to.sendMessage(MM.deserialize(getConfig().getString("messages.prefix", "") + s));
    }

    public String price(boolean shardCurrency, double v) {
        DecimalFormat df = new DecimalFormat("#,##0.##");
        String sym = shardCurrency ? getConfig().getString("shard-symbol", "◆") : getConfig().getString("currency-symbol", "$");
        return sym + df.format(v);
    }

    /** Short price for chat, e.g. $5K or ◆1.5K. */
    public String priceShort(boolean shardCurrency, double v) {
        String sym = shardCurrency ? getConfig().getString("shard-symbol", "◆") : getConfig().getString("currency-symbol", "$");
        return sym + compact(v);
    }

    /** TOTEM_OF_UNDYING -> Totem Of Undying */
    public String pretty(String materialName) {
        StringBuilder sb = new StringBuilder();
        for (String w : materialName.toLowerCase().split("_")) {
            if (w.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
        }
        return sb.toString();
    }

    public String compact(double v) {
        DecimalFormat df = new DecimalFormat("#,##0.##");
        if (v >= 1e12) return df.format(v / 1e12) + "T";
        if (v >= 1e9) return df.format(v / 1e9) + "B";
        if (v >= 1e6) return df.format(v / 1e6) + "M";
        if (v >= 1e3) return df.format(v / 1e3) + "K";
        return df.format(v);
    }
}
