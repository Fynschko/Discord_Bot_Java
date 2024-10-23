package com.DiscordBot.listeners;

import com.DiscordBot.databaseconnections.MySql;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.Channel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


public class EventListeners extends ListenerAdapter {

    long notficationChannelID = 1298400922308972554L;

    long memberRoleID = 1292877128068501585L;

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

        String user = Objects.requireNonNull(event.getMember()).getEffectiveName();
        List<Role> roles = event.getMember().getRoles();

        String message = user + "hat den Server Verlassen :(";

        Objects.requireNonNull(event.getGuild().getTextChannelById(notficationChannelID)).sendMessage(message).queue();

        try {
            MySql.update("INSERT INTO Roles (User_Name, Role) VALUES ("+user+", "+roles+")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        //SQL CODE

        Member member = event.getMember();
        Role role = event.getGuild().getRoleById(memberRoleID);

        event.getGuild().addRoleToMember(member, role).queue();
    }
}
