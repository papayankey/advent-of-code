package com.github.papayankey.AoC_2023.day_01;

import com.github.papayankey.AoC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Trebuchet {
    public static void main(String[] args) {
        var lines = AoC.getInput(2023, 1);

        // Part One
        var result = partOne(lines);
        System.out.println(result);

        // Part Two
        result = partTwo(lines);
        System.out.println(result);
    }

    public static int partOne(List<String> lines) {
        var calibrationValues = new ArrayList<Integer>();

        for (var line : lines) {
            var digits = line.replaceAll("\\D", "");
            var first = digits.substring(0, 1);
            var last = digits.substring(digits.length() - 1);
            calibrationValues.add(Integer.parseInt(first + last));
        }

        return sum(calibrationValues);
    }

    public static int partTwo(List<String> lines) {
        var pattern = "1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine";
        var calibrationValues = new ArrayList<Integer>();

        var wordMap = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8,
                "nine", 9);

        for (var line : lines) {
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

        return sum(calibrationValues);
    }

    private static int sum(List<Integer> calibrationValues) {
        return calibrationValues.stream().mapToInt(Integer::intValue).sum();
    }
}
