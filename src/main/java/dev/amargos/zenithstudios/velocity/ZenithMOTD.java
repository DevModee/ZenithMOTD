package dev.amargos.zenithstudios.velocity;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;

import java.nio.file.Files;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import dev.amargos.zenithstudios.velocity.commands.ZenithMOTDCommand;
import dev.amargos.zenithstudios.velocity.listeners.ProxyPingListener;
import dev.amargos.zenithstudios.velocity.variables.Messages;
import dev.amargos.zenithstudios.velocity.variables.VelocityVariables;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Plugin(
        id = "zenitdmotd",
        name = "ZenithMOTD",
        version = "2.0.0",
        description = "Easy MOTD customization plugin.",
        authors = ("Amargos")
)
public final class ZenithMOTD {
    private final Path path;
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final Logger logger;
    private VelocityVariables variables;
    private Messages messages;

    @Inject
    public ZenithMOTD(
            @DataDirectory Path path,
            CommandManager commandManager,
            EventManager eventManager,
            Logger logger
    ) {
        this.path = path;
        this.commandManager = commandManager;
        this.eventManager = eventManager;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (!reload()) {
            return;
        }

        final CommandMeta meta = commandManager.metaBuilder("zenithmotd")
                .plugin(this)
                .build();

        commandManager.register(meta, new ZenithMOTDCommand(this));
        eventManager.register(this, new ProxyPingListener(this));
    }

    public VelocityVariables getVariables() {
        return variables;
    }

    public Messages getMessages() {
        return messages;
    }

    public boolean reload() {
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch(IOException e) {
                logger.error("Cannot create Plugin directory", e);
                return false;
            }
        }

        VelocityVariables vars;
        try {
            vars = VelocityVariables.loadConfig(path, this);
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
            logger.error("Cannot load plugin configuration", e);
            return false;
        }

        Messages msg;
        try {
            msg = Messages.loadConfig(path, this);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Cannot load plugin messages");
            return false;
        }

        if (msg != null) {
            this.messages = msg;
        }

        if (vars != null) {
            this.variables = vars;
        }

        return vars != null && msg != null;
    }
}
