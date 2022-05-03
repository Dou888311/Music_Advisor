package com.Dou888311;

import java.util.Scanner;

public class Requests {
    public String ACCESS_TOKEN;
    public String API_PATH;
    public int page;


    public Requests(String ACCESS_TOKEN, String API_PATH, int page) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        this.API_PATH = API_PATH;
        this.page = page;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        Model mod = new Model(ACCESS_TOKEN, API_PATH, page);
        mod.inputProcessing(userInput);
        while (true) {
            userInput = sc.nextLine();
            mod.inputProcessing(userInput);
        }
    }
}
