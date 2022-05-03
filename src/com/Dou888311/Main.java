package com.Dou888311;

public class Main {
    public static void main(String[] args) {
        String path = "https://accounts.spotify.com";
        String APIpath = "https://api.spotify.com";
        int page = 5;
        if (args.length > 0 && args[0].equals("-access")) {
            path = args[1];
        }
        if (args.length > 2 && args[2].equals("-resource")) {
            APIpath = args[3];
        }
        if (args.length > 4 && args[4].equals("-page")) {
            page = Integer.parseInt(args[5]);
        }
        User user = new User(path, APIpath, page);
        user.madeRequests();
    }
}
