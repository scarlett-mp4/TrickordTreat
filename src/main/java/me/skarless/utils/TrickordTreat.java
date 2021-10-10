package me.skarless.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.skarless.Main;
import me.skarless.commands.*;
import me.skarless.event.Timer;
import me.skarless.listeners.GuildLeaveListener;
import me.skarless.listeners.MessageReceivedListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TrickordTreat {
    // GSON
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // Cache
    public final Cache cache;
    // JDA
    public JDA jda;
    // Main instance
    private static TrickordTreat instance;
    public static TrickordTreat getInstance() {
        return instance;
    }

    // Constructor
    public TrickordTreat() throws IOException {
        instance = this;
        cache = new Cache();

        initConfig();
        initCommands();
        new Timer().initEvent();

        onEnable();
    }

    /**
     * Bot startup logic
     */
    public void onEnable() {
        JDABuilder builder = JDABuilder.createDefault(Main.TOKEN);

        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setActivity(Activity.playing("Type: $help"));
        builder.addEventListeners(new MessageReceivedListener());
        builder.addEventListeners(new GuildLeaveListener());

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        // Command queue handler - should prolly move this :P
        new Thread(() -> {
            do {
                List<QueuedCommand> list = cache.commandQueue;
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list.isEmpty())
                    continue;
                try {
                    QueuedCommand command = cache.commandQueue.get(0);
                    boolean bool = command.command().execute(command.e(), command.args());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cache.commandQueue.remove(0);
            } while (true);
        }).start();
    }

    /**
     * Returns the config
     *
     * @return JsonObject
     */
    public static JsonObject getConfig() {
        try {
            File file = new File("cache.txt");
            if (!file.exists()) {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("{}");
                writer.close();
            }

            Scanner scanner = new Scanner(file);
            StringBuilder s = new StringBuilder();
            while (scanner.hasNext()) {
                s.append(scanner.nextLine());
            }
            return new Gson().fromJson(s.toString(), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set the config
     *
     * @param object - Replace the config's JsonObject
     */
    public static void setConfig(JsonObject object) {
        try {
            File file = new File("cache.txt");
            if (!file.exists()) {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("{}");
                writer.close();
            }

            PrintWriter writer = new PrintWriter(file);
            writer.print(gson.toJson(object));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes bot commands
     */
    public void initCommands() {
        new Help();
        new Event();
        new Leaderboard();
        new Rank();
        new Treat();
        new Trick();
    }

    /**
     * Initializes the config
     */
    public void initConfig() throws IOException {
        File file = new File("cache.txt");

        if (!file.exists()) {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("{}");
            writer.close();
        }
    }
}
