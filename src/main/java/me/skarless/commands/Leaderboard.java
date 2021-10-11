package me.skarless.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.skarless.utils.Command;
import me.skarless.utils.LeaderboardPlayer;
import me.skarless.utils.TrickordTreat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends Command {
    @Override
    public List<String> getCommand() {
        return List.of("leaderboard", "lb", "top");
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_WRITE;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {

        JsonObject object = TrickordTreat.getConfig();
        if (!object.has(e.getGuild().getId())) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("`❌ Error!`");
            embedBuilder.setDescription("The event must be configured before you can do this!");
            e.getChannel().sendMessage(embedBuilder.build()).queue();

            return true;
        }
        JsonObject serverObject = object.getAsJsonObject(e.getGuild().getId());
        if (serverObject.has("status")) {
            if (!serverObject.get("status").getAsString().equalsIgnoreCase("RUNNING")) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.ORANGE);
                embedBuilder.setTitle("`❌ Error!`");
                embedBuilder.setDescription("There is currently isn't an ongoing event!");
                e.getChannel().sendMessage(embedBuilder.build()).queue();

                return true;
            }
        }

        JsonArray array = serverObject.get("stats").getAsJsonArray();
        List<LeaderboardPlayer> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            String[] s = element.getAsString().split(" ");
            User user = TrickordTreat.getInstance().jda.getUserById(Long.parseLong(s[0]));
            list.add(new LeaderboardPlayer(user, Integer.parseInt(s[1])));
        }

        list.sort(Comparator.comparing(LeaderboardPlayer::score).reversed());

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            try {
                builder.append("**").append(i).append(":** ").append(list.get(i - 1).user().getAsMention()).append(" - *").append(list.get(i - 1).score()).append(" points*\n");
            } catch (Exception exc) {
                builder.append("Empty - *0 points*\n");
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(builder.toString());
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle("`🏆 Leaderboard:`");
        e.getChannel().sendMessage(embedBuilder.build()).queue();

        return true;
    }
}
