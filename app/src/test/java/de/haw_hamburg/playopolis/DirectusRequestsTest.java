package de.haw_hamburg.playopolis;

import org.junit.Test;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
public class DirectusRequestsTest {

    @Test
    public void testRegisterUser() throws Exception {
        String apiUrl = "https://directus-se.up.railway.app/items/users";

        // Mock the HttpURLConnectionWrapper class
        HttpURLConnectionWrapper connectionWrapper = mock(HttpURLConnectionWrapper.class);
        when(connectionWrapper.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connectionWrapper.getInputStream()).thenReturn(mock(InputStream.class));

        // Mock the URL class and its openConnection method
        URL url = mock(URL.class);
        when(url.openConnection()).thenReturn((URLConnection) connectionWrapper);

        // Mock the ObjectMapper class
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValueAsString(any(DirectusUser.class))).thenReturn("jsonRequest");

        // Create an instance of the class under test
        DirectusRequests myClass = new DirectusRequests();

        // Invoke the method under test
        myClass.registerUser("test@example.com", "testUser", "testPassword");

        // Verify that the expected methods were called
        verify(url).openConnection();
        verify(connectionWrapper).setRequestMethod("POST");
        verify(connectionWrapper).setRequestProperty("Content-Type", "application/json");
        verify(connectionWrapper).setDoOutput(true);
        verify(connectionWrapper.getOutputStream()).write(any(byte[].class));
        verify(connectionWrapper.getOutputStream()).flush();
        verify(connectionWrapper.getOutputStream()).close();
        verify(connectionWrapper).getInputStream();
        verify(connectionWrapper).disconnect();
    }

    // Wrapper class for HttpURLConnection to enable mocking
    public interface HttpURLConnectionWrapper {
        void setRequestMethod(String method);
        void setRequestProperty(String key, String value);
        void setDoOutput(boolean doOutput);
        OutputStream getOutputStream() throws IOException;
        InputStream getInputStream() throws IOException;
        void disconnect();
    }
}
 */