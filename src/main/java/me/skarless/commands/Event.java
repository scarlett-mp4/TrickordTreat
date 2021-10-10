package me.skarless.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.skarless.utils.Command;
import me.skarless.utils.TrickordTreat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Event extends Command {
    @Override
    public List<String> getCommand() {
        return Collections.singletonList("event");
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        try {
            switch (args[1]) {
                case "setup":
                    try {
                        long l1 = Long.decode(args[2].substring(2, args[2].length() - 1));
                        long l2 = Long.decode(args[3].substring(3, args[3].length() - 1));
                        MessageChannel targetChannel = TrickordTreat.getInstance().jda.getTextChannelById(l1);
                        Role targetRole = TrickordTreat.getInstance().jda.getRoleById(l2);
                        JsonObject object = TrickordTreat.getConfig();
                        JsonObject serverObject = new JsonObject();

                        if (object.has(e.getGuild().getId())) {
                            serverObject = object.getAsJsonObject(e.getGuild().getId());
                        }
                        serverObject.addProperty("eventChannel", Objects.requireNonNull(targetChannel).getId());
                        serverObject.addProperty("eventRole", Objects.requireNonNull(targetRole).getId());
                        object.add(e.getGuild().getId(), serverObject);

                        TrickordTreat.setConfig(object);

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle("`✅ Complete!`");
                        builder.setDescription("You have successfully configured the event.");
                        builder.setColor(Color.GREEN);
                        e.getChannel().sendMessage(builder.build()).queue();
                    } catch (Exception exception) {
                        e.getChannel().sendMessage("Incorrect usage: event setup <#channel> <@role>").queue();
                    }
                    return true;

                case "start":
                    JsonObject object = TrickordTreat.getConfig();
                    if (!object.has(e.getGuild().getId())) {
                        e.getChannel().sendMessage("You must run the setup command first!").queue();
                        return true;
                    }
                    JsonObject serverObject = object.getAsJsonObject(e.getGuild().getId());

                    if (serverObject.has("status")) {
                        if (serverObject.get("status").getAsString().equalsIgnoreCase("RUNNING")) {
                            e.getChannel().sendMessage("Event has already been started!").queue();
                            return true;
                        }
                    }

                    JsonArray leaderboard = new JsonArray();
                    serverObject.add("stats", leaderboard);
                    serverObject.addProperty("status", "RUNNING");
                    TrickordTreat.setConfig(object);

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("`✅ Complete!`");
                    builder.setDescription("The event has begun.");
                    builder.setColor(Color.GREEN);
                    e.getChannel().sendMessage(builder.build()).queue();
                    return true;

                case "end":
                    JsonObject object1 = TrickordTreat.getConfig();
                    if (!object1.has(e.getGuild().getId())) {
                        e.getChannel().sendMessage("You must run the setup command first!").queue();
                        return true;
                    }
                    JsonObject serverObject1 = object1.getAsJsonObject(e.getGuild().getId());

                    if (serverObject1.has("status")) {
                        if (!serverObject1.get("status").getAsString().equalsIgnoreCase("RUNNING")) {
                            e.getChannel().sendMessage("You can't end an event that isn't running!").queue();
                            return true;
                        }
                    }

                    JsonArray array = serverObject1.get("stats").getAsJsonArray();
                    int highScore = 0;
                    long winner = 0;
                    for (JsonElement players : array) {
                        long playerID = Long.parseLong(players.getAsString().split(" ")[0]);
                        int value = Integer.parseInt(players.getAsString().split(" ")[1]);

                        if (value > highScore) {
                            highScore = value;
                            winner = playerID;
                        }
                    }

                    MessageChannel channel = TrickordTreat.getInstance().jda.getTextChannelById(serverObject1.get("eventChannel").getAsLong());
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    if (winner == 0) {
                        embedBuilder.setTitle("`✨ The event has concluded! ✨`");
                        embedBuilder.setDescription("It appears that no one participated in this event.\nMake sure to compete next time for a chance at limited-time prizes!");
                        embedBuilder.setColor(Color.ORANGE);
                    } else {
                        User user = TrickordTreat.getInstance().jda.getUserById(winner);
                        Role role = TrickordTreat.getInstance().jda.getRoleById(serverObject1.get("eventRole").getAsLong());
                        embedBuilder.setTitle("`✨ The event has concluded! ✨`");
                        embedBuilder.setDescription(user.getAsMention() + " has won the event and received the " + role.getAsMention() + "role!");
                        embedBuilder.setColor(Color.ORANGE);

                        try {
                            e.getGuild().addRoleToMember(user.getId(), role).queue();
                        } catch (HierarchyException hierarchyException) {
                            EmbedBuilder newEmbedBuilder = new EmbedBuilder();
                            newEmbedBuilder.setColor(Color.RED);
                            newEmbedBuilder.setTitle("`❌ Error!`");
                            newEmbedBuilder.setDescription("I do not have permission to give this role! Please contact an administrator for help.");
                            channel.sendMessage(newEmbedBuilder.build()).queue();
                        }
                    }
                    channel.sendMessage(embedBuilder.build()).queue();

                    embedBuilder.setTitle("`✅ Complete!`");
                    embedBuilder.setDescription("You have successfully ended the event.");
                    embedBuilder.setColor(Color.GREEN);
                    e.getChannel().sendMessage(embedBuilder.build()).queue();

                    serverObject1.addProperty("status", "STOPPED");
                    object1.add(e.getGuild().getId(), serverObject1);
                    TrickordTreat.setConfig(object1);
                    return true;

                case "set":
                    try {
                        long num = Long.parseLong(args[2].substring(3, args[2].length() - 1));
                        User user = TrickordTreat.getInstance().jda.getUserById(num);
                        int value = Integer.parseInt(args[3]);

                        JsonObject object2 = TrickordTreat.getConfig();
                        if (!object2.has(e.getGuild().getId())) {
                            e.getChannel().sendMessage("You must run the setup command first!").queue();
                            return true;
                        }
                        JsonObject serverObject2 = object2.getAsJsonObject(e.getGuild().getId());

                        if (serverObject2.has("status")) {
                            if (!serverObject2.get("status").getAsString().equalsIgnoreCase("RUNNING")) {
                                e.getChannel().sendMessage("You can't edit an event that isn't running!").queue();
                                return true;
                            }
                        }

                        JsonArray jsonArray = serverObject2.get("stats").getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement element = jsonArray.get(i);
                            if (element.getAsString().contains(user.getId())) {
                                jsonArray.remove(i);
                            }
                        }
                        jsonArray.add(user.getId() + " " + value);

                        serverObject2.add("stats", jsonArray);
                        object2.add(e.getGuild().getId(), serverObject2);
                        TrickordTreat.setConfig(object2);

                        EmbedBuilder embedBuilder1 = new EmbedBuilder();
                        embedBuilder1.setTitle("`✅ Complete!`");
                        embedBuilder1.setDescription("The user, " + user.getAsTag().substring(0, user.getAsTag().length() - 5) + ", has a new point value of " + value + "!");
                        embedBuilder1.setColor(Color.GREEN);
                        e.getChannel().sendMessage(embedBuilder1.build()).queue();

                    } catch (Exception exception) {
                        e.getChannel().sendMessage("Incorrect Usage: $event set <@user> <integer: points>").queue();
                        exception.printStackTrace();
                    }

                    return true;
            }
        } catch (Exception ignored) {
        }

        e.getChannel().sendMessage("Unknown args. Valid arguments: setup, start, end, set").queue();
        return true;
    }
}
