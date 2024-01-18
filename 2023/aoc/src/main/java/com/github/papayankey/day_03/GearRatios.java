package com.github.papayankey.day_03;

import com.github.papayankey.AoC;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GearRatios {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 3);

        // Part One
        var result = partOne(input);
        System.out.println(result);
    }

    public static int partOne(Path inputSource) {
        var result = 0;
        String[][] grid = new String[140][];

        try (var scanner = new Scanner(inputSource)) {
            var index = 0;
            while (scanner.hasNextLine()) {
                grid[index++] = scanner.nextLine().split("");
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        for (int row = 0; row < grid.length; row++) {
            var start = -1;
            var digit = 0;
            for (int col = 0; col < grid[row].length; col++) {
                if (Character.isDigit(grid[row][col].charAt(0))) {
                    digit = (digit * 10) + Integer.parseInt(grid[row][col]);
                    if (col == 0 || col - 1 > -1 && !Character.isDigit(grid[row][col - 1].charAt(0))) {
                        start = col;
                    }
                    if (col < grid[row].length && col + 1 < grid[row].length
                            && !Character.isDigit(grid[row][col + 1].charAt(0))) {
                        List<String> adjacents = getAdjacents(grid, row, start, col);

                        for (String symbol : adjacents) {
                            if (symbol.charAt(0) != '.') {
                                result += digit;
                            }
                        }
                        // resets
                        start = -1;
                        digit = 0;
                    }
                }
            }
        }

        return result;
    }

    private static List<String> getAdjacents(String[][] grid, int row, int start, int end) {
        var adjacents = new ArrayList<String>();
        if (start - 1 > -1) {
            adjacents.add(grid[row][start - 1]);
            if (row - 1 > -1) {
                adjacents.add(grid[row - 1][start - 1]);
            }
            if (row + 1 < grid.length) {
                adjacents.add(grid[row + 1][start - 1]);
            }
        }
        if (end + 1 < grid[row].length) {
            adjacents.add(grid[row][end + 1]);
            if (row - 1 > -1) {
                adjacents.add(grid[row - 1][end + 1]);
            }
            if (row + 1 < grid.length) {
                adjacents.add(grid[row + 1][end + 1]);
            }
        }
        for (int y = start; y <= end; y++) {
            if (row - 1 > -1) {
                adjacents.add(grid[row - 1][y]);
            }
            if (row + 1 < grid.length) {
                adjacents.add(grid[row + 1][y]);
            }
        }
        return adjacents;
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
