package com.bono.soundcloud;

import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 12/02/16.
 */
public class Result {

    private String description;
    private String duration;
    private String iconUrl;
    private Image image;
    private String title;
    private String url;

    public Result(String url, String title, String duration) {
        this.url = url;
        this.title = title;
        this.duration = duration;
    }

    public Result(String description, String url, String title, String duration) {
        this(url, title, duration);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Result{" +
                "description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", image=" + image +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
