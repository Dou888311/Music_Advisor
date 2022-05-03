package com.Dou888311;

import java.util.Scanner;

public class User {
    public String SERVER_PATH;
    public String API_PATH;
    public String ACCESS_TOKEN;
    int page;
    boolean isAuth = false;

    public User(String SERVER_PATH, String API_PATH, int page) {
        this.SERVER_PATH = SERVER_PATH;
        this.API_PATH = API_PATH;
        this.page = page;
    }

    public void madeRequests() {
        while (!isAuth) {
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (entryValidRequest(input)) {
                isAuth = true;
                ACCESS_TOKEN = serverAuthentification();
                requestsProcceding();
            } else {
                System.out.println("Please, provide access for application.");
            }
        }
    }

    public void requestsProcceding() {
        Requests requests = new Requests(ACCESS_TOKEN, API_PATH, page);
        requests.start();
    }

    public String serverAuthentification() {
        Server server = new Server(SERVER_PATH);
        server.serverStart();
        return server.getAccessToken();
    }

    public boolean entryValidRequest(String input) {
        if (input.equals("auth")) {
            return true;
        }
        return false;
    }
}
