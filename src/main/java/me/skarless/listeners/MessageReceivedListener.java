package me.skarless.listeners;

import me.skarless.utils.Command;
import me.skarless.utils.QueuedCommand;
import me.skarless.utils.TrickordTreat;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class MessageReceivedListener extends ListenerAdapter {

    public String prefix = "$";

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot())
            return;

        if(e.getMessage().getContentRaw().charAt(0) == prefix.charAt(0)){
            for (Command command : TrickordTreat.getInstance().cache.commands) {
                String[] args = e.getMessage().getContentRaw().split(" ");

                if (Objects.requireNonNull(e.getMember()).hasPermission(command.getPermission())) {
                    for (String s : command.getCommand()) {
                        String str = prefix + s;
                        if (args[0].equalsIgnoreCase(str)) {
                            try {
                                TrickordTreat.getInstance().cache.commandQueue.add(new QueuedCommand(e, args, command));
                                return;
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            }

            e.getChannel().sendMessage("Unknown command! Try `/help` for help.").queue();
        }
    }
}
