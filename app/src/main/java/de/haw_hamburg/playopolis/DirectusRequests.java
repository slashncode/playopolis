package de.haw_hamburg.playopolis;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class DirectusRequests {


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
     *
     * @param email
     * @param username
     * @param password
     */
    public static void registerUser(String email, String username, String password) {
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

            System.out.println(jsonRequest);

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

            // Process the response
            System.out.println(response.toString());

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setFavoriteGame(Integer gameId, Integer userId) {
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

            // Process the response
            System.out.println(response.toString());

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createImage(String filePath) throws IOException {
        // API endpoint URL
        String apiUrl = "https://directus-se.up.railway.app/files";
        // File details
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

        return fileName;
    }

    public static JsonNode getRequest(String apiUrlPath) {
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

            // Process the JSON response
            //System.out.println(rootNode);
            //System.out.println(rootNode.get("data").get(0).get("title"));

            // Close the connection
            connection.disconnect();

            return rootNode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void patchRequest(Integer userId, String key, String value) {
        String PATCH_URL = "https://directus-se.up.railway.app/items/users/" + userId.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Create an HttpClient instance
            HttpClient httpClient = HttpClients.createDefault();

            // Create a PATCH request with the URL
            HttpPatch httpPatch = new HttpPatch(PATCH_URL);

            // Set the JSON payload for the request
            String jsonPayload = "{\"" + key + "\": \"" + value + "\"}";
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPatch.setEntity(entity);

            // Execute the request
            HttpResponse response = httpClient.execute(httpPatch);

            // Close the HttpClient
            //httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void patchRequest(Integer userId, JsonNode jsonNode) {
        String PATCH_URL = "https://directus-se.up.railway.app/items/users/" + userId.toString();
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

            // Close the HttpClient
            //httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DirectusUser {
    private String email;
    private String username;
    private String password;
    private String avatar;
    private ArrayList<String> genres;
    private String description;
    private ArrayList<Integer> favorite_games;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Integer> getFavorite_games() {
        return favorite_games;
    }

    public void setFavorite_games(ArrayList<Integer> favorite_games) {
        this.favorite_games = favorite_games;
    }
}

class UserGames {
    private Integer users_id;
    private Integer games_id;

    public Integer getUsers_id() {
        return users_id;
    }

    public void setUsers_id(Integer users_id) {
        this.users_id = users_id;
    }

    public Integer getGames_id() {
        return games_id;
    }

    public void setGames_id(Integer games_id) {
        this.games_id = games_id;
    }
}
