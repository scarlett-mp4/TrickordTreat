package me.skarless.event;

import com.google.gson.JsonObject;
import me.skarless.Main;
import me.skarless.utils.Cache;
import me.skarless.utils.MonsterTypes;
import me.skarless.utils.ServerData;
import me.skarless.utils.TrickordTreat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Timer {

    public void initEvent() {
        new Thread(() -> {
            do {

                if (Main.DEBUG)
                    System.out.println(Cache.monsterList);

                try {
                    List<String> serverIDs = new ArrayList<>(TrickordTreat.getConfig().keySet());
                    for (String id : serverIDs) {
                        try {
                            JsonObject serverObject = TrickordTreat.getConfig().get(id).getAsJsonObject();
                            if (serverObject.has("status")) {
                                if (!serverObject.get("status").getAsString().equals("RUNNING")) {
                                    continue;
                                }
                            } else {
                                continue;
                            }

                            MessageChannel channel = TrickordTreat.getInstance().jda.getTextChannelById(serverObject.get("eventChannel").getAsLong());

                            if (!isMonsterSent(id)) {
                                MonsterTypes monster = MonsterTypes.ZOMBIE;
                                int i = ThreadLocalRandom.current().nextInt(1, MonsterTypes.values().length + 1);
                                for (MonsterTypes m : MonsterTypes.values()) {
                                    if (m.getId() == i)
                                        monster = m;
                                }

                                String type = "trick";
                                int random = ThreadLocalRandom.current().nextInt(1, 3);
                                if (random == 1) {
                                    type = "trick";
                                } else if (random == 2) {
                                    type = "treat";
                                }

                                EmbedBuilder builder = new EmbedBuilder();
                                builder.setColor(Color.ORANGE);
                                builder.setTitle("**A trick-or-treater has appeared!**");
                                builder.setDescription("Don't be shy, open the door and greet them with a **$" + type + "**!");
                                builder.setImage(monster.getUrlString());
                                if (type.equals("treat")) {
                                    builder.setThumbnail("https://i.imgur.com/w7ABSVq.png");
                                } else {
                                    builder.setThumbnail("https://i.imgur.com/0lqJFpr.png");
                                }
                                MonsterTypes finalMonster = monster;
                                String finalType = type;
                                channel.sendMessage(builder.build()).queue((message -> {
                                    Cache.monsterList.add(new ServerData(id, message.getIdLong(), finalMonster, finalType));
                                    if (Main.DEBUG)
                                        System.out.println(id + ": sent monster");
                                }));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    TimeUnit.MINUTES.sleep(ThreadLocalRandom.current().nextInt(Main.DELAY_MIN, Main.DELAY_MAX + 1));

                } catch (Exception ignored) {
                }
            } while (true);
        }).start();
    }

    private boolean isMonsterSent(String serverID) {
        for (ServerData serverSave : Cache.monsterList) {
            if (serverSave.getServerID().equals(serverID)) {
                return true;
            }
        }
        return false;
    }
}
