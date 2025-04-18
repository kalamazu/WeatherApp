package com.liao.weatherapp.Playerfactory;

public class MediaFile {
    private long id;
    private String title;
    private String artist;
    private long duration;
    private String path;

    public MediaFile(long id, String title, String artist, long duration, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
    }

    // Getters
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public long getDuration() { return duration; }
    public String getPath() { return path; }
}