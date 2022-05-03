package com.Dou888311;

public interface Requestable {
    void request(String ACCESS_TOKEN, String API_PATH, int page);
    void prev();
    void next();
}
