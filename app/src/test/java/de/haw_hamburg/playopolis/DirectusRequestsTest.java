package de.haw_hamburg.playopolis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;

import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DirectusRequestsTest {

    @Test
    public void testGetRequestTaskForGames() {
        // Api Path
        String apiUrlPath = "https://directus-se.up.railway.app/items/games/";

        ObjectMapper mapper = new ObjectMapper();
        final JsonNode[] apiResult = {mapper.createObjectNode()};

        // Create a mock executor
        Executor executor = Executors.newSingleThreadExecutor();

        // Create a mock listener for capturing the result
        DirectusRequests.GetRequestTask.OnRequestCompleteListener listener = new DirectusRequests.GetRequestTask.OnRequestCompleteListener() {
            @Override
            public void onRequestComplete(JsonNode result) {
                // Assert the expected result
                apiResult[0] = mapper.valueToTree(result);
            }
        };

        // Create a GetRequestTask instance
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, listener);

        // Execute the task with the sample JSON response
        getRequestTask.executeInBackground(apiUrlPath);

        // Wait for the task to complete (optional)
        try {
            Thread.sleep(5000);
            Assert.assertNotNull(apiResult[0]);
            Assert.assertEquals("God of War Ragnarök", apiResult[0].get("data").get(0).get("title").asText());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRequestTaskForUsers() {
        // Api Path
        String apiUrlPath = "https://directus-se.up.railway.app/items/users/50";

        ObjectMapper mapper = new ObjectMapper();
        final JsonNode[] apiResult = {mapper.createObjectNode()};

        // Create a mock executor
        Executor executor = Executors.newSingleThreadExecutor();

        // Create a mock listener for capturing the result
        DirectusRequests.GetRequestTask.OnRequestCompleteListener listener = new DirectusRequests.GetRequestTask.OnRequestCompleteListener() {
            @Override
            public void onRequestComplete(JsonNode result) {
                // Assert the expected result
                apiResult[0] = mapper.valueToTree(result);
            }
        };

        // Create a GetRequestTask instance
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, listener);

        // Execute the task with the sample JSON response
        getRequestTask.executeInBackground(apiUrlPath);

        // Wait for the task to complete (optional)
        try {
            Thread.sleep(5000);
            Assert.assertNotNull(apiResult[0]);
            Assert.assertEquals("NoobMaster1337", apiResult[0].get("data").get("username").asText());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}