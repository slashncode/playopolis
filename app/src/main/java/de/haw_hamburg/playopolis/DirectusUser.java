package de.haw_hamburg.playopolis;

import java.util.ArrayList;

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