package me.skarless.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class Command {

    public Command() {
        TrickordTreat.getInstance().cache.commands.add(this);
    }

    public abstract List<String> getCommand();

    public abstract Permission getPermission();

    public abstract boolean execute(MessageReceivedEvent e, String[] args);

}
