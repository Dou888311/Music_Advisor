package com.Dou888311;

public class Album {
    private String name;
    private String artists;
    private String url;

    public Album(String name, String artists, String url) {
        this.name = name;
        this.artists = artists;
        this.url = url;
    }

    public String getName() {return name;}
    public String getArtists() {return artists;}
    public String getUrl() {return url;}
}
