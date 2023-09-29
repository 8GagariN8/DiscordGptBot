package bot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static openAiApi.GptRequest.request;
import static utils.Props.props;
import static utils.SplitMessage.splitGPTMessage;

@Slf4j
public class BaseDiscordBot {

    public static void main(String[] args) {

        DiscordClient client = DiscordClient.create(props.discordToken());
        GatewayDiscordClient gateway = client.login().block();

        Objects.requireNonNull(gateway).on(ReadyEvent.class).subscribe(event -> {
            final User self = event.getSelf();
            log.info("Logged in as " + self.getUsername());
        });

        gateway.on(MessageCreateEvent.class).subscribe(event -> {

            Message message = event.getMessage();

            if (message.getContent().startsWith("/gpt") && message.getChannelId().equals(Snowflake.of(props.discordChannelId()))) {
                log.info(message.getAuthor() + ": " + message.getContent());
                MessageChannel channel = message.getChannel().block();
                String responseMsg = request(message.getContent()).get(1).getContent();

                if (responseMsg.length() < 2000) {
                    Objects.requireNonNull(channel).createMessage(responseMsg).block();
                } else {
                    List<String> arrPhrases = splitGPTMessage(responseMsg);
                    for (String string : arrPhrases) {
                        Objects.requireNonNull(channel).createMessage(string).block();
                    }
                }
            }
        });
        gateway.onDisconnect().block();
    }
}