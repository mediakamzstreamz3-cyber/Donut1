package me.donutshop;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * PlaceholderAPI placeholders:
 *   %donutshop_shards%            raw number, e.g. 12500
 *   %donutshop_shards_commas%     12,500
 *   %donutshop_shards_formatted%  12.5K
 */
public final class ShardPlaceholders extends PlaceholderExpansion {
    private final DonutShop plugin;

    public ShardPlaceholders(DonutShop plugin) {
        this.plugin = plugin;
    }

    @Override public @NotNull String getIdentifier() { return "donutshop"; }
    @Override public @NotNull String getAuthor() { return "DonutShop"; }
    @Override public @NotNull String getVersion() { return plugin.getPluginMeta().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";
        long v = plugin.shards().get(player.getUniqueId());
        switch (params.toLowerCase()) {
            case "shards": return String.valueOf(v);
            case "shards_commas": return new DecimalFormat("#,##0").format(v);
            case "shards_formatted": return plugin.compact(v);
            default: return null;
        }
    }
}
