package com.Dou888311;

public class Model {
    String ACCESS_TOKEN;
    String API_PATH;
    String[] userInput;
    int page;
    private Requestable doRequest;

    public Model(String ACCESS_TOKEN, String API_PATH, int page) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        this.API_PATH = API_PATH;
        this.page = page;
    }

    public void inputProcessing(String input) {
        userInput = input.split(" ", 2);
        if (userInput[0].equals("new")) {
            setRequest(new NewRequest());
            request();
            return;
        }
        if (userInput[0].equals("featured")) {
            setRequest(new FeaturedRequest());
            request();
            return;
        }
        if (userInput[0].equals("categories")) {
            setRequest(new CategoriesRequest());
            request();
            return;
        }
        if (userInput[0].equals("playlists")) {
            PlaylistsRequest play = new PlaylistsRequest();
            play.whatToFind = userInput[1];
            setRequest(play);
            request();
            return;
        }
        if (userInput[0].equals("prev")) {
            prev();
            return;
        }
        if (userInput[0].equals("next")) {
            next();
        }
    }

    public void setRequest(Requestable doRequest) {
        this.doRequest = doRequest;
    }
    public void request() {
        this.doRequest.request(ACCESS_TOKEN, API_PATH, page);
    }
    public void next() {
        this.doRequest.next();
    }
    public void prev() {
        this.doRequest.prev();
    }
}
