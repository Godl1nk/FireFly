package rip.firefly.webapi.bot;

import rip.firefly.webapi.manager.DataManager;
import rip.firefly.webapi.util.EmbedUtil;
import rip.firefly.webapi.util.FileUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.RoleImpl;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

public class DiscordBot extends ListenerAdapter {

    public static JDA jda;

    public static void startBot()
    {
        try
        {
            jda = JDABuilder.createDefault("OTU1MTM4ODExNDk3NzA5NjM4.YjdUVg.-wR9HPoX5UWId1izM5QVLPsWTDo")
                    .addEventListeners(new DiscordBot())
                    .build();
            jda.awaitReady();
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
       //These are provided with every event in JDA
        JDA jda = event.getJDA();                      //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

       //Event specific information
        User author = event.getAuthor();               //The user that sent the message
        Message message = event.getMessage();          //The message that was received.
        MessageChannel channel = event.getChannel();   //This is the MessageChannel that the message was sent to.
       //  This could be a TextChannel, PrivateChannel, or Group!

        String msg = message.getContentDisplay();             //This returns a human readable version of the Message. Similar to
       // what you would see in the client.

        boolean bot = author.isBot();                   //This boolean is useful to determine if the User that
       // sent the Message is a BOT or not!

        if (event.isFromType(ChannelType.TEXT))        //If this message was sent to a Guild TextChannel
        {
           //Because we now know that this message was sent in a Guild, we can do guild specific things
           // Note, if you don't check the ChannelType before using these methods, they might return null due
           // the message possibly not being from a Guild!

            Guild guild = event.getGuild();            //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
            TextChannel textChannel = event.getTextChannel();//The TextChannel that this message was sent to.
            Member member = event.getMember();         //This Member that sent the message. Contains Guild specific information about the User!

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();               //If this is a Webhook message, then there is no Member associated
            }                                          // with the User, thus we default to the author for name.
            else {
                name = member.getEffectiveName();      //This will either use the Member's nickname if they have one,
            }                                          // otherwise it will default to their username. (User#getName())

            if (author != jda.getUserById(jda.getSelfUser().getId()) || !message.isWebhookMessage()) {
                System.out.printf("(%s) [#%s] <%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
            }
        }

        if (msg.startsWith("!whitelist")) {
            Member member = event.getMember();
            if (member != null) {
                if (!member.getRoles().contains(new RoleImpl(955256038876123157L, message.getGuild()))) {
                    event.getMessage().addReaction("U+274E").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.error("FireFly // Error", "You Do Not Have Permission To Use This Command"))
                                    .build()
                    ).queue();
                    return;
                }
                String mmsg = message.getContentStripped().replace("!whitelist ", "");
               //event.getMessage().addReaction("✔").queue();


                if (isValidMD5(mmsg)) {
                    if (!FileUtil.getWhitelistedHwids().contains(mmsg)) {
                        FileUtil.writeLine(mmsg);
                        event.getMessage().addReaction("U+2705").queue();
                        event.getChannel().sendMessage(
                                new MessageBuilder()
                                        .setEmbeds(EmbedUtil.success("FireFly // Success", "Whitelisted HWID \"" + mmsg + "\" Successfully"))
                                        .build()
                        ).queue();
                    } else {
                        event.getMessage().addReaction("U+274E").queue();
                        event.getChannel().sendMessage(
                                new MessageBuilder()
                                        .setEmbeds(EmbedUtil.error("FireFly // Error", "That HWID Is Already Whitelisted"))
                                        .build()
                        ).queue();
                    }
                } else {
                    event.getMessage().addReaction("U+274E").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.error("FireFly // Error", "Invalid HWID"))
                                    .build()
                    ).queue();
                }

            }

        } else if (msg.startsWith("!unwhitelist")) {
            Member member = event.getMember();
            if (member != null) {
                String mmsg = message.getContentStripped().replace("!unwhitelist ", "");

                if (!member.getRoles().contains(new RoleImpl(955256038876123157L, message.getGuild()))) {
                    event.getMessage().addReaction("U+274E").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.error("FireFly // Error", "You Do Not Have Permission To Use This Command"))
                                    .build()
                    ).queue();
                    return;
                }

                if (isValidMD5(mmsg)) {
                    if (FileUtil.getWhitelistedHwids().contains(mmsg)) {
                        FileUtil.removeLine(mmsg);
                        event.getMessage().addReaction("U+2705").queue();
                        event.getChannel().sendMessage(
                                new MessageBuilder()
                                        .setEmbeds(EmbedUtil.success("FireFly // Success", "Unwhitelisted HWID \"" + mmsg +"\" Successfully"))
                                        .build()
                        ).queue();
                    } else {
                        event.getMessage().addReaction("U+274E").queue();
                        event.getChannel().sendMessage(
                                new MessageBuilder()
                                        .setEmbeds(EmbedUtil.error("FireFly // Error", "That HWID Is Not Whitelisted"))
                                        .build()
                        ).queue();
                    }
                } else {
                    event.getMessage().addReaction("U+274E").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.error("FireFly // Error", "Invalid HWID"))
                                    .build()
                    ).queue();

                }

            }

        } else if (msg.startsWith("!reboot")) {
            Member member = event.getMember();
            if (member != null) {
                String mmsg = message.getContentStripped().replace("!reboot ", "");

                if (!member.getRoles().contains(new RoleImpl(927387961065103390L, message.getGuild()))) {
                    event.getMessage().addReaction("U+274E").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.error("FireFly // Error", "You Do Not Have Permission To Use This Command"))
                                    .build()
                    ).queue();
                    return;
                }

                if (mmsg.matches("\\d+") && !(Integer.parseInt(mmsg) > Integer.MAX_VALUE)) {
                    event.getMessage().addReaction("U+2705").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.info("FireFly // Reboot", "Rebooting In " + mmsg + " Seconds"))
                                    .build()
                    ).queue();

                    try {
                        TimeUnit.SECONDS.sleep(Integer.parseInt(mmsg));
                        System.exit(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    event.getMessage().addReaction("U+274E").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.error("FireFly // Error", "Invalid Time Till Reboot!"))
                                    .build()
                    ).queue();
                }

            }

        } else if (msg.startsWith("!totalvl")) {
            Member member = event.getMember();
            if (member != null) {
                String mmsg = message.getContentStripped().replace("!totalvl ", "");

                    event.getMessage().addReaction("U+2705").queue();
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .setEmbeds(EmbedUtil.info("FireFly // Total Flags", "Total Flags: " + DataManager.getVl()))
                                    .build()).queue();

            }
        } else if (msg.startsWith("!servers")) {
            Member member = event.getMember();
            if (member != null) {
                String mmsg = message.getContentStripped().replace("!totalvl ", "");

                event.getMessage().addReaction("U+2705").queue();
                event.getChannel().sendMessage(
                        new MessageBuilder()
                                .setEmbeds(EmbedUtil.info("FireFly // Servers", "Servers Running FireFly: " + DataManager.getServers()))
                                .build())
                        .queue();

            }
        }
    }

    public boolean isValidMD5(String s) {
        return s.matches("^[a-fA-F0-9]{32}$");
    }

    public static void sendLog(String hwid, String port, String ip) {
        jda.getGuildById(926692019433250857L).getTextChannelById(927352105361088614L).sendMessageEmbeds(
                EmbedUtil.info(
                        "**FireFly Has Been Started On A Server**",
                        "~~----------------------------------------------------------------------~~\n" +
                                "**IP:** " + ip + "\n" +
                                "**Port:** " + port + "\n" +
                                "**HWID:** " + hwid +"\n" +
                                "~~----------------------------------------------------------------------~~"
                )
        ).queue();
    }

}
