package utils;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:local/local.properties",
        "classpath:application.properties"
})
public interface Props extends Config {
    Props props = ConfigFactory.create(Props.class);

    @Key("discord.bot.token")
    String discordToken();

    @Key("discord.channelId")
    String discordChannelId();

    @Key("openAI.api.token")
    String openAiToken();
}
