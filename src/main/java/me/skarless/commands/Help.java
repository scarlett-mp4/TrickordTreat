package me.skarless.commands;

import me.skarless.utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Help extends Command {

    @Override
    public List<String> getCommand() {
        return Collections.singletonList("help");
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_WRITE;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle("`❔ Help`");
        embedBuilder.setDescription("Thank you for participating in the Trick'cord Treat Discord event! Every 10-20 minutes, a trick-or-treater will appear in a specified channel." +
                " From there, you will be tasked to either give them a treat or trick them to your heart's content. The winner of the event will be given a unique role that declares them the winner of this event." +
                " Have fun!" +
                "\n\n" + "**Commands:**" +
                "\n" + "__$trick__ - Trick a trick-or-treater." +
                "\n" + "__$treat__ - Give a trick-or-treater a treat." +
                "\n" + "__$leaderboard__ - Displays the leaderboard." +
                "\n" + "__$rank__ - Displays your rank." +
                "\n" + "__$help__ - Displays this menu." +
                "\n" + "__$event__ - Admin command used to manage the event." +
                "\n" + "" +
                "\n" + "[Invite the bot to your server!](https://discord.com/api/oauth2/authorize?client_id=893550270968102912&permissions=8&scope=bot)"
        );
        e.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
