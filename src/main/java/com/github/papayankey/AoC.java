package com.github.papayankey;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class AoC {
    private static final String SESSION = "";

    private AoC() {
        throw new IllegalStateException("Utility class");
    }

    public static List<String> getInput(int year, int day) {
        var dayFormat = day < 10 ? STR."0\{day}" : STR."\{day}";
        Path path = Path.of(STR."src/main/java/com/github/papayankey/AoC_\{year}/day_\{dayFormat}/input.txt").toAbsolutePath();

        if (Files.exists(path)) {
            return readToList(path);
        }

        CookieHandler.setDefault(new CookieManager());

        HttpCookie sessionCookie = new HttpCookie("session", SESSION);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        URI uri = null;
        try {
            ((CookieManager) CookieHandler.getDefault()).getCookieStore().add(URI.create("https://adventofcode.com"),
                    sessionCookie);
            uri = URI.create(STR."https://adventofcode.com/\{year}/day/\{day}/input");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri)
                .GET().build();

        try (HttpClient client = HttpClient.newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .connectTimeout(Duration.ofSeconds(10))
                .build()) {

            try {
                client.send(req, HttpResponse.BodyHandlers.ofFile(Files.createFile(path))).body();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        return readToList(path);
    }

    private static List<String> readToList(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return reader.lines().toList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }
}
