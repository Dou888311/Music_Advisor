package com.Dou888311;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRequest implements Requestable {
    int currentPage = 1;
    int maxPage;
    int page;
    List<Album> albums = new ArrayList<>();
    public void request(String ACCESS_TOKEN, String API_PATH, int page) {
        this.page = page;
        String responseBody = "";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(API_PATH + "/v1/browse/new-releases"))
                .GET()
                .build();
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
        JsonObject jo = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject newObj = jo.get("albums").getAsJsonObject();
        JsonArray newArray = newObj.getAsJsonArray("items");
        for (int i = 0; i < newArray.size(); i++) {
            JsonObject item = newArray.get(i).getAsJsonObject();
            JsonObject extUrl = item.get("external_urls").getAsJsonObject();
            String url = extUrl.get("spotify").getAsString();
            JsonArray artistInfo = item.get("artists").getAsJsonArray();
            String[] artists = new String[artistInfo.size()];
            for (int j = 0; j < artistInfo.size(); j++) {
                JsonObject ai = artistInfo.get(j).getAsJsonObject();
                artists[j] = ai.get("name").getAsString();
            }
            String name = item.get("name").getAsString();
            albums.add(new Album(name, Arrays.toString(artists), url));
        }
        maxPage = albums.size() / page;
        if (albums.size() % page > 0) {
            maxPage++;
        }
        int index = Math.min(page, albums.size());
        for (int i = 0; i < index; i++) {
            System.out.println(albums.get(i).getName());
            System.out.println(albums.get(i).getArtists());
            System.out.println(albums.get(i).getUrl());
        }
        System.out.println("---PAGE " + currentPage + " OF " + maxPage + "---");

    }
    public void prev() {
        if (currentPage == 1) {
            System.out.println("No more pages");
            return;
        }
        currentPage--;
        int indexStart = (currentPage - 1) * page;
        int indexEnd = Math.min(indexStart + page, albums.size());
        for (int i = indexStart; i < indexEnd; i++) {
            System.out.println(albums.get(i).getName());
            System.out.println(albums.get(i).getArtists());
            System.out.println(albums.get(i).getUrl());
        }
        System.out.println("---PAGE " + currentPage + " OF " + maxPage + "---");
    }
    public void next() {
        if (currentPage == maxPage) {
            System.out.println("No more pages");
            return;
        }
        currentPage++;
        int indexStart = (currentPage - 1) * page;
        int indexEnd = Math.min(indexStart + page, albums.size());
        for (int i = indexStart; i < indexEnd; i++) {
            System.out.println(albums.get(i).getName());
            System.out.println(albums.get(i).getArtists());
            System.out.println(albums.get(i).getUrl());
        }
        System.out.println("---PAGE " + currentPage + " OF " + maxPage + "---");
    }
}