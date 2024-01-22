package org.example;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.MINUTES, 3);

        crptApi.create(new CrptApi.Document(), "1");
        System.out.println(1);
        crptApi.create(new CrptApi.Document(), "1");
        System.out.println(2);
        crptApi.create(new CrptApi.Document(), "1");
        System.out.println(3);
        crptApi.create(new CrptApi.Document(), "1");
        System.out.println(4);
    }
}