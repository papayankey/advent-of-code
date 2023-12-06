package com.github.papayankey.day_04;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

import com.github.papayankey.InputSource;

public class ScratchCards {
    public static void main(String[] args) {
        // var input = InputSource.load("day_04", "example.txt");
        var input = InputSource.load("day_04", "input.txt");

        // Part One
        var result = partOne(input);
        System.out.println(result);
    }

    public static int partOne(Path inputSource) {
        var totalPoints = 0;

        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var parts = scanner.nextLine().replaceAll("Card\\s+\\d+:\\s", "").split("\\|");
                var winnings = parts[0].trim().split("\\s+");
                var numbersInHand = parts[1].trim().split("\\s+");

                var map = new HashMap<String, String>();
                for (String num : numbersInHand) {
                    map.put(num, null);
                }

                var cardPoint = 0;
                var isFirstMatch = true;

                for (String win : winnings) {
                    if (map.containsKey(win)) {
                        if (isFirstMatch) {
                            cardPoint += 1;
                            isFirstMatch = false;
                        } else {
                            cardPoint *= 2;
                        }
                    }
                }

                totalPoints += cardPoint;
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return totalPoints;
    }

    public static int partTwo(Path inputSource) {
        var result = 0;
        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

}
