package dev.yosa.ebank.ebankbot.telegram;

import dev.yosa.ebank.ebankbot.agents.EbankAiAgent;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@ConditionalOnProperty(name = "telegram.enabled", havingValue = "true", matchIfMissing = true)
public class TelegramBot extends TelegramLongPollingBot {

    private final EbankAiAgent agent;

    public TelegramBot(EbankAiAgent agent, @Value("${telegram.bot.token}") String token) {
        super(token);
        this.agent = agent;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return "ebank_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String query = update.getMessage().getText();
            String response = agent.chat(new Prompt(query));
            try {
                execute(SendMessage.builder().chatId(chatId).text(response).build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
