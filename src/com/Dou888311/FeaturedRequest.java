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
import java.util.List;

public class FeaturedRequest implements Requestable {
    List<Categories> categories = new ArrayList<>();
    int currentPage = 1;
    int maxPage;
    int page;
    public void request(String ACCESS_TOKEN, String API_PATH, int page) {
        this.page = page;
        String responseBody = "";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(API_PATH + "/v1/browse/featured-playlists"))
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
        JsonObject playListObj = jo.get("playlists").getAsJsonObject();
        JsonArray playListArray = playListObj.getAsJsonArray("items");
        for (int i = 0; i < playListArray.size(); i++) {
            JsonObject jso = playListArray.get(i).getAsJsonObject();
            JsonObject extUrls = jso.getAsJsonObject("external_urls");
            String url = extUrls.get("spotify").getAsString();
            categories.add(new Categories(jso.get("name").getAsString(), "unknown", url));
        }
        maxPage = categories.size() / page;
        if (categories.size() % page > 0) {
            maxPage++;
        }
        int index = Math.min(page, categories.size());
        for (int i = 0; i < index; i++) {
            System.out.println(categories.get(i).getName());
            System.out.println(categories.get(i).getHref());
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
        int indexEnd = Math.min(indexStart + page, categories.size());
        for (int i = indexStart; i < indexEnd; i++) {
            System.out.println(categories.get(i).getName());
            System.out.println(categories.get(i).getHref());
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
        int indexEnd = Math.min(indexStart + page, categories.size());
        for (int i = indexStart; i < indexEnd; i++) {
            System.out.println(categories.get(i).getName());
            System.out.println(categories.get(i).getHref());
        }
        System.out.println("---PAGE " + currentPage + " OF " + maxPage + "---");
    }
}
