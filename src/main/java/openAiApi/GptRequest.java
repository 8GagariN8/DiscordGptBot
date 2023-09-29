package openAiApi;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;

import java.util.ArrayList;
import java.util.List;

import static utils.Props.props;

public class GptRequest {
    public static List<ChatMessage> request(String prompt) {

        OpenAiService service = new OpenAiService(props.openAiToken());

        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(firstMsg);


        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .maxTokens(700)
                .build();
        Flowable<ChatCompletionChunk> flowable = service.streamChatCompletion(chatCompletionRequest);

        ChatMessage chatMessage = service
                .mapStreamToAccumulator(flowable)
                .lastElement()
                .blockingGet()
                .getAccumulatedMessage();
        messages.add(chatMessage);
        return messages;
    }
}

