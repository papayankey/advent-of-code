package com.github.papayankey.day_01;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.github.papayankey.InputSource;

public class Trebuchet {
    public static void main(String[] args) {
        var input = InputSource.load("day_01", "input.txt");

        // Part One
        var result = partOne(input);
        System.out.println(result);

        // Part Two
        result = partTwo(input);
        System.out.println(result);
    }

    public static int partOne(Path inputSource) {
        var calibrationValues = new ArrayList<Integer>();

        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var digits = scanner.nextLine().replaceAll("\\D", "");
                var first = digits.substring(0, 1);
                var last = digits.substring(digits.length() - 1);
                calibrationValues.add(Integer.parseInt(first + last));
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return sum(calibrationValues);
    }

    public static int partTwo(Path inputSource) {
        var pattern = "1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine";
        var calibrationValues = new ArrayList<Integer>();

        var wordMap = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8,
                "nine", 9);

        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var matcher = Pattern.compile(pattern).matcher(line);
                var digits = new ArrayList<Integer>();

                var index = 0;
                while (matcher.find(index)) {
                    var match = matcher.group();
                    if (Character.isDigit(match.charAt(0))) {
                        digits.add(Integer.parseInt(match));
                    } else {
                        digits.add(wordMap.get(match));
                    }
                    index += 1;
                }

                calibrationValues.add(Integer.parseInt(digits.getFirst() + "" + digits.getLast()));
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return sum(calibrationValues);
    }

    private static int sum(List<Integer> calibrationValues) {
        return calibrationValues.stream().mapToInt(Integer::intValue).sum();
    }
}
