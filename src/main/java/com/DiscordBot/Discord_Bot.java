package com.DiscordBot;

import com.DiscordBot.databaseconnections.MySql;
import com.DiscordBot.listeners.EventListeners;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.*;

public class Discord_Bot {

    public Discord_Bot() throws LoginException, SQLException, IOException {

        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        String dbPass = config.get("DATABASEPASSWORD");
        String dbUser = config.get("DATABASEUSERNAME");

        JDABuilder builder =  JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        builder.setActivity(Activity.playing("Hobo-Simulater"));
        builder.setStatus(OnlineStatus.ONLINE);
        JDA bot = builder.build();

        bot.addEventListener(new EventListeners());

        MySql.connect(dbUser, dbPass);

    }

    public static void main(String[] args) {

            try {
                Discord_Bot bot = new Discord_Bot();
            } catch (LoginException | SQLException | IOException e) {
                System.out.println("Something Wrong with the Token"+e);
            }
    }
}
