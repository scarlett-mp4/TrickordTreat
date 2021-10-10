package me.skarless.listeners;

import com.google.gson.JsonObject;
import me.skarless.utils.TrickordTreat;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent e) {
        JsonObject object = TrickordTreat.getConfig();
        System.out.println("Bot has left server: " + e.getGuild().getName());

        if(object.has(e.getGuild().getId())){
            object.remove(e.getGuild().getId());
        }

        TrickordTreat.setConfig(object);
    }
}
