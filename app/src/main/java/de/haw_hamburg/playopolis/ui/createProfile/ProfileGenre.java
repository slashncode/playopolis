package de.haw_hamburg.playopolis.ui.createProfile;

public class ProfileGenre {

    private final String tagName;
    private Boolean enabled = false;

    public ProfileGenre(String tagName){
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void toggleEnabled() {
        enabled = !enabled;
    }
}
