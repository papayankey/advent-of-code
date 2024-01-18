package com.github.papayankey.day_02;

import com.github.papayankey.AoC;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CubeConundrum {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 2);

        // Part One
        var result = partOne(input);
        System.out.println(result);

        // Part Two
        result = partTwo(input);
        System.out.println(result);
    }

    private static int partOne(Path inputSource) {
        var maximumCubes = Map.of("red", 12, "green", 13, "blue", 14);
        var result = 0;

        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var parts = scanner.nextLine().trim().split(":");
                var gameId = Integer.parseInt(parts[0].trim().split(" ")[1]);
                var cubes = parts[1].trim().replaceAll("; |, ", ",").split(",");
                var isValidGame = true;

                for (int i = 0; i < cubes.length; i++) {
                    var cube = cubes[i].trim().split(" ");
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
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return result;
    }

    private static int partTwo(Path inputSource) {
        var result = 0;

        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var minimumCubes = new HashMap<String, Integer>();
                var parts = scanner.nextLine().trim().split(":");
                var cubes = parts[1].trim().replaceAll("; |, ", ",").split(",");

                for (int i = 0; i < cubes.length; i++) {
                    var cube = cubes[i].trim().split(" ");
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
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return result;
    }
}
