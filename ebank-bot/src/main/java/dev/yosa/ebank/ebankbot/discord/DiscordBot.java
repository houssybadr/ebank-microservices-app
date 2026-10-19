package dev.yosa.ebank.ebankbot.discord;

import dev.yosa.ebank.ebankbot.agents.EbankAiAgent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class DiscordBot extends ListenerAdapter {

    private final EbankAiAgent agent;

    public DiscordBot(EbankAiAgent agent) {
        this.agent = agent;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String query = event.getMessage().getContentRaw();
        String response = agent.chat(new Prompt(query));
        event.getChannel().sendMessage(response).queue();
    }
}
