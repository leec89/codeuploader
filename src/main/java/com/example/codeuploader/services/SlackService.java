package com.example.codeuploader.services;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SlackService {

    @Value("${spring_slack_channel_id}")
    private String slackChannelId;

    @Value("${spring_slack_token}")
    private String slackToken;

    public ResponseEntity<ByteArrayResource> sendToSlack(String textToSlack) throws SlackApiException, IOException, URISyntaxException {

        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");

        // Call the chat.postMessage method using the built-in WebClient
        var result = client.chatPostMessage(r -> r
                        // The token you used to initialize your app
                        .token(slackToken)
                        .channel(slackChannelId)
                        .text(textToSlack)
                // You could also use a blocks[] array to send richer content
        );
        // Print result, which includes information about the message (like TS)
        logger.info("result {}", result);

        URI yahoo = new URI("/resources/");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
