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

public class Rank extends Command {
    @Override
    public List<String> getCommand() {
        return List.of("rank", "r", "stats");
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_WRITE;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        User target;
        try {
            long num = Long.parseLong(args[1].substring(3, args[1].length() - 1));
            target = TrickordTreat.getInstance().jda.getUserById(num);
        } catch (Exception exception) {
            target = e.getMember().getUser();
        }

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
                embedBuilder.setDescription("There is currently no ongoing event!");
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

        for (int i = 0; i < list.size(); i++) {
            LeaderboardPlayer user = list.get(i);

            if(user.user().getId().equals(target.getId())){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.ORANGE);
                embedBuilder.setTitle("`💪 " + target.getName() + "'s Rank:`");
                embedBuilder.setThumbnail(target.getAvatarUrl());
                embedBuilder.setDescription("**Rank:** " + (i + 1) + "/" + e.getGuild().getMembers().size() + "\n" + "**Score:** " + user.score() + " points");
                e.getChannel().sendMessage(embedBuilder.build()).queue();
                return true;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle("`💪 " + target.getName() + "'s Rank:`");
        embedBuilder.setThumbnail(target.getAvatarUrl());
        embedBuilder.setDescription("**Rank:** N/A\n**Score:** 0 points");
        embedBuilder.addField("No info found!", "It looks like this user has not started playing yet. Make sure to let them know how much fun you're having!", true);
        e.getChannel().sendMessage(embedBuilder.build()).queue();

        return true;
    }
}
