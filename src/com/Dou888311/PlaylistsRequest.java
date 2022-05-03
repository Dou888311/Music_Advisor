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

public class PlaylistsRequest implements Requestable {

    String ACCESS_TOKEN;
    String API_PATH;
    String currentCategoryID;
    String whatToFind;
    List<Categories> categories = new ArrayList<>();
    int currentPage = 1;
    int maxPage;
    int page;

    public void request(String ACCESS_TOKEN, String API_PATH, int page) {
        this.page = page;
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        this.API_PATH = API_PATH;
        if (categoriesIdFinder(whatToFind)) {
            String responseBody = "";
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
                    .uri(URI.create(API_PATH + "/v1/browse/categories/" + currentCategoryID + "/playlists"))
                    .GET()
                    .build();
            try {
                HttpClient client = HttpClient.newBuilder().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                responseBody = response.body();
            } catch (IOException | InterruptedException e) {
                e.getMessage();
            }
            if (errorResponseCheck(responseBody)) {
                JsonObject jo = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject playlists = jo.getAsJsonObject("playlists");
                JsonArray items = playlists.getAsJsonArray("items");
                for (int i = 0; i < items.size(); i++) {
                    JsonObject job = items.get(i).getAsJsonObject();
                    JsonObject extUrl = job.get("external_urls").getAsJsonObject();
                    String url = extUrl.get("spotify").getAsString();
                    String name = job.get("name").getAsString();
                    categories.add(new Categories(name, "unknown", url));
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
        }
        else {
            System.out.println("Specified id doesn't exist");
        }
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

    public boolean categoriesIdFinder(String categoryName) {
        String responseBody = "";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(API_PATH + "/v1/browse/categories"))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject cat = json.get("categories").getAsJsonObject();
        JsonArray items = cat.get("items").getAsJsonArray();
        for (int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            if (item.get("name").getAsString().equals(categoryName)) {
                currentCategoryID = item.get("id").getAsString();
                return true;
            }
        }
        return false;
    }

    public boolean errorResponseCheck(String responseBody) {
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        try {
            JsonObject error = json.get("error").getAsJsonObject();
            String message = error.get("message").getAsString();
            System.out.println(message);
            return false;
        } catch (NullPointerException e) {
            return true;
        }
    }
}
