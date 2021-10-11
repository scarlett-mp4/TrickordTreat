package me.skarless.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.skarless.utils.Cache;
import me.skarless.utils.Command;
import me.skarless.utils.ServerData;
import me.skarless.utils.TrickordTreat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Treat extends Command {
    @Override
    public List<String> getCommand() {
        return List.of("treat");
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_WRITE;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        try {
            JsonObject serverObject = Objects.requireNonNull(TrickordTreat.getConfig()).get(e.getGuild().getId()).getAsJsonObject();
            MessageChannel channel = TrickordTreat.getInstance().jda.getTextChannelById(serverObject.get("eventChannel").getAsLong());

            if (e.getChannel().getId().equals(Objects.requireNonNull(channel).getId())) {
                for (int i = 0; i < Cache.monsterList.size(); i++) {
                    ServerData serverData = Cache.monsterList.get(i);
                    if (serverData.getServerID().equalsIgnoreCase(e.getGuild().getId())) {


                        if (serverData.getServerID().equals(e.getGuild().getId())) {
                            if (serverData.getType().equals("treat")) {
                                Cache.monsterList.remove(i);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setColor(Color.GREEN);
                                embedBuilder.setTitle("**Thank you, " + Objects.requireNonNull(e.getMember()).getUser().getName() + "!**");

                                JsonArray array = serverObject.get("stats").getAsJsonArray();
                                int old = 0;
                                for (int ii = 0; ii < array.size(); ii++) {
                                    JsonElement element = array.get(ii);
                                    if (element.getAsString().contains(e.getMember().getUser().getId())) {
                                        String[] s = array.get(ii).getAsString().split(" ");
                                        old = Integer.parseInt(s[1]);
                                        array.remove(ii);
                                    }
                                }

                                int doubleChance = ThreadLocalRandom.current().nextInt(0, 5);
                                if (doubleChance == 3) {
                                    array.add(e.getMember().getUser().getId() + " " + (old + 2));
                                    embedBuilder.setDescription("You have been rewarded __" + 2 + " [DOUBLE]__ points!");
                                } else {
                                    array.add(e.getMember().getUser().getId() + " " + (old + 1));
                                    embedBuilder.setDescription("You have been rewarded __" + 1 + "__ points!");
                                }

                                Objects.requireNonNull(TrickordTreat.getInstance().jda.getTextChannelById(channel.getId())).editMessageById(serverData.getMessageID(), embedBuilder.build()).queue();
                                serverObject.add("stats", array);
                                JsonObject newObject = TrickordTreat.getConfig();
                                newObject.add(e.getGuild().getId(), serverObject);
                                TrickordTreat.setConfig(newObject);
                            } else {
                                channel.sendMessage("This is a trick, not a treat! Try **$trick** instead.").queue();
                            }
                            return true;
                        }
                    }
                }
                channel.sendMessage("It doesn't look like there are any trick-or-treaters around...").queue();
            } else {
                e.getChannel().sendMessage("You can't use this command here!").queue();
            }
        } catch (Exception exception) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("`❌ Error!`");
            embedBuilder.setDescription("The event must be configured before you can do this!");
            e.getChannel().sendMessage(embedBuilder.build()).queue();
        }

        return true;
    }
}
