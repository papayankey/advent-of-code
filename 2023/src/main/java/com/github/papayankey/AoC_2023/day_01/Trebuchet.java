package com.github.papayankey.AoC_2023.day_01;

import com.github.papayankey.AoC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Trebuchet {
    public static void main(String[] args) {
        var lines = AoC.getInput(2023, 1);

        System.out.println(PartOne(lines));
        System.out.println(PartTwo(lines));
    }

    public static int PartOne(List<String> lines) {
        return lines.stream().map(line -> line.replaceAll("\\D", ""))
                .map(digits -> digits.charAt(0) + digits.substring(digits.length() - 1))
                .mapToInt(Integer::parseInt)
                .sum();
    }

    public static int PartTwo(List<String> lines) {
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

            calibrationValues.add(Integer.parseInt(STR."\{digits.getFirst()}\{digits.getLast()}"));
        }

        return calibrationValues.stream().mapToInt(Integer::intValue).sum();
    }
}
