package dev.yosa.ebank.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EbankAiAgent {

    private static final String SYSTEM_PROMPT = """
            You are an assistant that answers user questions about customers and bank accounts
            based on the context provided by your tools.
            If no context is provided, answer with 'I don't know'.
            """;

    private final ChatClient chatClient;

    public EbankAiAgent(ChatClient.Builder builder, ChatMemory chatMemory, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(toolCallbackProvider)
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    public String chat(Prompt prompt) {
        return chatClient.prompt(prompt).call().content();
    }

    public Flux<String> chatStream(Prompt prompt) {
        return chatClient.prompt(prompt).stream().content();
    }
}
