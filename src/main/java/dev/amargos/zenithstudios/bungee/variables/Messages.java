package dev.amargos.zenithstudios.bungee.variables;

import dev.amargos.zenithstudios.bungee.utils.ConfigurationUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class Messages {
    private final ConfigurationUtil configurationUtil;
    private String reload;
    private String usage;
    private String unknownCommand;
    private String noPermission;

    public Messages(final ConfigurationUtil configurationUtil) {
        this.configurationUtil = configurationUtil;
        reload();
    }

    public void reload() {
        final Configuration messages = configurationUtil.getConfiguration("%datafolder%/Messages.yml");

        reload = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.reload"));
        usage = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.usage"));
        unknownCommand = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.unknowncommand"));
        noPermission = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.nopermission"));
    }

    public String getReload() {
        return reload;
    }

    public String getUsage() {
        return usage;
    }

    public String getUnknownCommand() {
        return unknownCommand;
    }

    public String getNoPermission() {
        return noPermission;
    }
}