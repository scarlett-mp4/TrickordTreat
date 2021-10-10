package me.skarless.utils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class QueuedCommand {
    private final MessageReceivedEvent e;
    private final String[] args;
    private final Command command;

    public QueuedCommand(MessageReceivedEvent e, String[] args,
                         Command command) {
        this.e = e;
        this.args = args;
        this.command = command;
    }

    public MessageReceivedEvent e() {
        return e;
    }

    public String[] args() {
        return args;
    }

    public Command command() {
        return command;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (QueuedCommand) obj;
        return Objects.equals(this.e, that.e) &&
                Objects.equals(this.args, that.args) &&
                Objects.equals(this.command, that.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e, args, command);
    }

    @Override
    public String toString() {
        return "SentCommand[" +
                "e=" + e + ", " +
                "args=" + args + ", " +
                "command=" + command + ']';
    }

}
