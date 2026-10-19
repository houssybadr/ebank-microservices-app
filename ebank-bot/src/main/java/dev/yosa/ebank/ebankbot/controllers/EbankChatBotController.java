package dev.yosa.ebank.ebankbot.controllers;

import dev.yosa.ebank.ebankbot.agents.EbankAiAgent;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EbankChatBotController {

    private final EbankAiAgent agent;

    public EbankChatBotController(EbankAiAgent agent) {
        this.agent = agent;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam(defaultValue = "Hello") String query) {
        return agent.chat(new Prompt(query));
    }

    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestParam(defaultValue = "Hello") String query) {
        return agent.chatStream(new Prompt(query));
    }
}
