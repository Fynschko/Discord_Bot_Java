package com.DiscordBot.listeners;

import com.DiscordBot.databaseconnections.MySql;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.ChannelUnion;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EventListeners extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(EventListeners.class);
    long notficationChannelID = 1298400922308972554L;
    //long memberRoleID = 1292877128068501585L;

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);

        String user = Objects.requireNonNull(event.getUser()).getEffectiveName();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channel = event.getChannel().getAsMention();
        String jumplink = event.getJumpUrl();
        String messageAuthorId = event.getMessageAuthorId();
        String messageAuthor = Objects.requireNonNull(event.getGuild().getMemberById(messageAuthorId)).getEffectiveName();
        String message = user + " hat auf eine Nachricht von: " + messageAuthor +  " reagiert mit: " + emoji + " in folfgendem channel: " + channel + " " +  jumplink;

        Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);

        try {
            String user = Objects.requireNonNull(event.getUser()).getEffectiveName();

            //leeres Array keine Rollen??
            List<Member> roles = event.getGuild().getMembersByEffectiveName(user, true);
            System.out.println(roles);
            String message = user + " hat den Server Verlassen :(";
            //System.out.println(roles);
            Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();


        } catch (NullPointerException e) {
            System.out.println("error:" + e );
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        super.onGuildMemberRoleAdd(event);

        List<Role> roles = event.getRoles();
        String user = event.getUser().getId();

        for (Role role : roles) {
            try {
                String stringRole = role.toString();
                String stringRoleEscape = stringRole.replaceAll("[:(){}=^A-Za-z\uD83D\uDD31| ]", "");
                System.out.println(stringRoleEscape);
                MySql.insertIntoRoleDB(user, stringRoleEscape);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        //SQL CODE
        //Event get Member get nicht (mehr testing)

        List<String> resultList;
        String userName = event.getUser().getEffectiveName();
        String userID = event.getUser().getId();
        String message = userName + " ist beigetreten!";
        Member member = event.getMember().getGuild().getMemberById(userID);

        try {
            resultList = MySql.getUserRole(userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (resultList.isEmpty()) {
            assert member != null;
            System.out.println(member.getEffectiveName() + "is not in Database");
        } else {

            for (String resultRole : resultList) {

                Role role = event.getGuild().getRoleById(resultRole);
                assert member != null;
                assert role != null;
                event.getGuild().addRoleToMember(member, role).queue();
            }
        }

        Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();
    }

    /*
    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        super.onChannelUpdateName(event);


       String newChannelName =  event.getChannel().getName();
       String message = "Channel " + newChannelName + " wurde umbennant!";

       Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();
    }

     */

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        super.onChannelCreate(event);

        String  ChannelName = event.getChannel().getName();
        String message = "Channel " + ChannelName + " wurde erstellt!";

        Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();

    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        super.onChannelDelete(event);

        String channelName = event.getChannel().getName();
        String message = "Channel " + channelName + " wurde gelöscht!";

        Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();
    } 
}




