package com.github.papayankey.day_02;

import com.github.papayankey.AoC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeConundrum {
    public static void main(String[] args) {
        var lines = AoC.getInput(2023, 2);

        // Part One
        var result = partOne(lines);
        System.out.println(result);

        // Part Two
        result = partTwo(lines);
        System.out.println(result);
    }

    private static int partOne(List<String> lines) {
        var maximumCubes = Map.of("red", 12, "green", 13, "blue", 14);
        var result = 0;

        for (var line : lines) {
            var parts = line.trim().split(":");
            var gameId = Integer.parseInt(parts[0].trim().split(" ")[1]);
            var cubes = parts[1].trim().replaceAll("; |, ", ",").split(",");
            var isValidGame = true;

            for (String s : cubes) {
                var cube = s.trim().split(" ");
                var cubeScore = Integer.parseInt(cube[0]);
                var cubeColor = cube[1].trim();

                if (cubeScore > maximumCubes.get(cubeColor)) {
                    isValidGame = false;
                }
            }

            if (isValidGame) {
                result += gameId;
            }
        }

        return result;
    }

    private static int partTwo(List<String> lines) {
        var result = 0;

        for (var line : lines) {
            var minimumCubes = new HashMap<String, Integer>();
            var parts = line.trim().split(":");
            var cubes = parts[1].trim().replaceAll("; |, ", ",").split(",");

            for (String s : cubes) {
                var cube = s.trim().split(" ");
                var cubeScore = Integer.parseInt(cube[0]);
                var cubeColor = cube[1].trim();

                if (minimumCubes.containsKey(cubeColor)) {
                    if (cubeScore > minimumCubes.get(cubeColor)) {
                        minimumCubes.put(cubeColor, cubeScore);
                    }
                } else {
                    minimumCubes.put(cubeColor, cubeScore);
                }
            }

            var power = minimumCubes.values().stream().reduce(1, (accum, curr) -> accum * curr);
            result += power;
        }

        return result;
    }
}
