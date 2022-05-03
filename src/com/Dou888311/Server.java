package com.Dou888311;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Server {
    public String CLIENT_SECRET = "e368fe4efb464c5ab5c92e8dc71f9d76";
    public String ACCESS_CODE = "";
    public String SERVER_PATH;
    public String REDIRECT_URI = "http://localhost:8080";
    public String CLIENT_ID = "1ecd013bf84847ab87e1a714ee03b008";
    public String ACCESS_TOKEN;

    public Server(String SERVER_PATH) {
        this.SERVER_PATH = SERVER_PATH;
    }

    public String uri;
    HttpServer server;



    /**
     * Start server and get ACCESS_CODE
     */
    void serverStart() {
        uri = SERVER_PATH + "/authorize?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response" +
                "_type=code";
        System.out.println("use this link to request the access code:");
        System.out.println(uri);
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.start();
            server.createContext("/",
                    new HttpHandler() {
                        public void handle(HttpExchange exchange) throws IOException {
                            String query = exchange.getRequestURI().getQuery();
                            String request;
                            if (query != null && query.contains("code")) {
                                ACCESS_CODE = query.substring(5);
                                System.out.println("code received");
                                System.out.println(ACCESS_CODE);
                                request = "Got the code. Return back to your program.";
                            } else {
                                request = "Authorization code not found. Try again.";
                            }
                            exchange.sendResponseHeaders(200, request.length());
                            exchange.getResponseBody().write(request.getBytes());
                            exchange.getResponseBody().close();
                        }
                    });
            System.out.println("waiting for code...");
            while (ACCESS_CODE.length() == 0) {
                Thread.sleep(100);
            }
            server.stop(5);
        } catch (InterruptedException | IOException e) {
            System.out.println("Server error");
        }
    }

    /**
     *  Gettin access TOKEN based on ACCESS_CODE
     */
    public String getAccessToken() {
        System.out.println("making http request for access_token...");
        System.out.println("response:");
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("" +
                        "grant_type=authorization_code" +
                        "&code=" + ACCESS_CODE + "" +
                        "&client_id=" + CLIENT_ID +"" +
                        "&client_secret=" + CLIENT_SECRET +"" +
                        "&redirect_uri=" + REDIRECT_URI))
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assert response != null;
            System.out.println(response.body());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            ACCESS_TOKEN = jo.get("access_token").getAsString();
            System.out.println("---SUCCESS---");
            return ACCESS_TOKEN;

        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }
        return ACCESS_TOKEN;
    }
}