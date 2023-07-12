package de.haw_hamburg.playopolis;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DirectusRequestsTest {

    @Test
    public void testGetRequestTaskForGames() {
        // Api Path
        String apiUrlPath = "https://directus-se.up.railway.app/items/games/";

        // Create a mock executor
        Executor executor = Executors.newSingleThreadExecutor();

        // Create a mock listener for capturing the result
        DirectusRequests.GetRequestTask.OnRequestCompleteListener listener = new DirectusRequests.GetRequestTask.OnRequestCompleteListener() {
            @Override
            public void onRequestComplete(JsonNode result) {
                // Assert the expected result
                Assert.assertNotNull(result);
                Assert.assertEquals("God of War Ragnarök", result.get("data").get(0).get("title").asText());
            }
        };

        // Create a GetRequestTask instance
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, listener);

        // Execute the task with the sample JSON response
        getRequestTask.executeInBackground(apiUrlPath);

        // Wait for the task to complete (optional)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRequestTaskForUsers() {
        // Api Path
        String apiUrlPath = "https://directus-se.up.railway.app/items/users/50";

        // Create a mock executor
        Executor executor = Executors.newSingleThreadExecutor();

        // Create a mock listener for capturing the result
        DirectusRequests.GetRequestTask.OnRequestCompleteListener listener = new DirectusRequests.GetRequestTask.OnRequestCompleteListener() {
            @Override
            public void onRequestComplete(JsonNode result) {
                // Assert the expected result
                Assert.assertNotNull(result);
                Assert.assertEquals("NoobMaster13372", result.get("data").get("username").asText());
            }
        };

        // Create a GetRequestTask instance
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, listener);

        // Execute the task with the sample JSON response
        getRequestTask.executeInBackground(apiUrlPath);

        // Wait for the task to complete (optional)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}