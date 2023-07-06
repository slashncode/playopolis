package de.haw_hamburg.playopolis;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DirectusRequests {

    // Create an instance of Executor
    static Executor executor = Executors.newSingleThreadExecutor();


    // main for testing the different request functions / examples for how to use them
    //public static void main(String[] args) {
        //DirectusRequests.createUser("soeren.poppe@gmx.de", "spoppe", "password", new ArrayList<>(List.of("Bullethell")), "/Users/spoppe/Pictures/soeren.png", "test");
        //DirectusRequests.patchRequest(1, "email", "neuemail@gmx.de");
        //DirectusRequests.setFavoriteGame(1, 1);
        //DirectusRequests.setFavoriteGame(2, 1);
        //DirectusRequests.patchRequest(1, "genres", "");
    //}

    /**
     *
     * This function only registers a new user with his email, username and password.
     * The avatar, game genres, favorite games and description have to be patched in the next view.
     * Avatars and favorite games are O2M- / M2M-relationships and can only be added after the user
     * is registered and has an ID in the database.
     *
     * Adding an avatar looks like this:
     * String imageId = String.valueOf(getRequest("https://directus-se.up.railway.app/files?filter[filename_download][_eq]=" + createImage(imagePath) + "&fields[]=id").get("data").get(0).get("id"));
     * // Strip double quotes of image ID
     * String newImageId = imageId.substring(1, imageId.length() - 1);
     * DirectusRequests.patchRequest(userId, "avatar", newImageId);
     *
     * Adding the favorite games looks like this:
     * DirectusRequests.setFavoriteGame(gameId, userId);
     */

    public interface OnRegisterUserCompleteListener {
        void onRegisterUserComplete(String result);
    }

    public static void registerUser(String email, String username, String password, OnRegisterUserCompleteListener listener) {
        // Wrap the existing code inside a Runnable
        Runnable registerUserRunnable = new Runnable() {
            @Override
            public void run() {
                String apiUrl = "https://directus-se.up.railway.app/items/users";
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Create the object to be sent in the request
                    DirectusUser requestObject = new DirectusUser();
                    requestObject.setEmail(email);
                    requestObject.setUsername(username);
                    requestObject.setPassword(password);

                    // Serialize the object to JSON
                    ObjectMapper userObjectMapper = new ObjectMapper();
                    String jsonRequest = userObjectMapper.writeValueAsString(requestObject);

                    // Write the JSON request to the connection's output stream
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonRequest.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Get the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Close the connection
                    connection.disconnect();

                    String result = response.toString();

                    if (listener != null) {
                        listener.onRegisterUserComplete(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Execute the registerUserRunnable using the Executor
        executor.execute(registerUserRunnable);
    }

    // Call this method when you want to set a favorite game
    public static void setFavoriteGame(Integer gameId, Integer userId) {
        // Wrap the existing code inside a Runnable
        Runnable setFavoriteGameRunnable = new Runnable() {
            @Override
            public void run() {
                String apiUrl = "https://directus-se.up.railway.app/items/users_games";
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    UserGames userGamesRequestObject = new UserGames();
                    userGamesRequestObject.setGames_id(gameId);
                    userGamesRequestObject.setUsers_id(userId);

                    // Serialize the object to JSON
                    ObjectMapper userObjectMapper = new ObjectMapper();
                    String jsonRequest = userObjectMapper.writeValueAsString(userGamesRequestObject);

                    // Write the JSON request to the connection's output stream
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonRequest.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    System.out.println(gameId + " " + userId);

                    // Get the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Processthe response
                    System.out.println(response.toString());

                    // Close the connection
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Execute the setFavoriteGameRunnable using the Executor
        executor.execute(setFavoriteGameRunnable);
    }

    public interface OnCreateImageCompleteListener {
        void onCreateImageComplete(String fileName);
    }

    // Call this method when you want to create an image
    public static void createImage(String filePath, Context context, OnCreateImageCompleteListener createImageCompleteListener) {
        // Wrap the existing code inside a Runnable
        Runnable createImageRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Existing code from createImage method
                    String apiUrl = "https://directus-se.up.railway.app/files";
                    int randomInt = ThreadLocalRandom.current().nextInt(0, 999999 + 1);
                    String fileName = "image" + randomInt + ".jpg";

                    // Construct the boundary and request body
                    String boundary = "__X_BOUNDARY__";
                    String lineSeparator = "\r\n";
                    String boundaryLine = "--" + boundary;
                    String requestBody = boundaryLine + lineSeparator +
                            "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + lineSeparator +
                            "Content-Type: image/jpeg" + lineSeparator + lineSeparator;

                    // Open connection
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    // Write request body
                    OutputStream outputStream = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
                    writer.append(requestBody);
                    writer.flush();

                    // Write the image data
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Write closing boundary
                    writer.append(lineSeparator).append(boundaryLine).append("--").append(lineSeparator);
                    writer.close();

                    // Get the response
                    int responseCode = connection.getResponseCode();
                    String responseMessage = connection.getResponseMessage();

                    // Close connections
                    fileInputStream.close();
                    outputStream.close();
                    connection.disconnect();

                    // Handle the result (e.g., return the file name)
                    handleCreateImageResult(fileName, context);

                    if (createImageCompleteListener != null) {
                        createImageCompleteListener.onCreateImageComplete(fileName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Execute the createImageRunnable using the Executor
        executor.execute(createImageRunnable);
    }

    public static void handleCreateImageResult(String fileName, Context context) {
        AppPreferences.getInstance(context).setImageName(fileName);
    }

    // Update the GetRequestTask class
    public static class GetRequestTask {
        private Executor executor;
        private OnRequestCompleteListener listener;

        public GetRequestTask(Executor executor, OnRequestCompleteListener listener) {
            this.executor = executor;
            this.listener = listener;
        }

        public void executeInBackground(String... params) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    doInBackground(params);
                }
            });
        }

        public void doInBackground(String... params) {
            String apiUrlPath = params[0];
            try {
                URL url = new URL(apiUrlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response using Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.toString());

                // Close the connection
                connection.disconnect();

                if (listener != null) {
                    listener.onRequestComplete(rootNode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public interface OnRequestCompleteListener {
            void onRequestComplete(JsonNode result);
        }
    }

    // Call this method when you want to execute the PATCH request
    public static void executePatchRequest(String userId, JsonNode jsonNode) {
        // Wrap the existing code inside a Runnable
        Runnable patchRunnable = new Runnable() {
            @Override
            public void run() {
                // The existing code from patchRequest method
                String PATCH_URL = "https://directus-se.up.railway.app/items/users/" + userId;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // Create an HttpClient instance
                    HttpClient httpClient = HttpClients.createDefault();

                    // Create a PATCH request with the URL
                    HttpPatch httpPatch = new HttpPatch(PATCH_URL);

                    // Set the JSON payload for the request
                    String jsonPayload = jsonNode.toString();
                    StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
                    httpPatch.setEntity(entity);

                    // Execute the request
                    HttpResponse response = httpClient.execute(httpPatch);

                    System.out.println(response);

                    // Close the HttpClient
                    //httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Execute the patchRunnable using the Executor
        executor.execute(patchRunnable);
    }
}
