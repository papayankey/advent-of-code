package com.github.papayankey.AoC_2023.day_03;

import com.github.papayankey.AoC;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

public class GearRatios {
    public static void main(String[] args) {
        var lines = AoC.getInput(2023, 3);

        System.out.println(PartOne(getSymbols(lines), getNumbers(lines)));
        System.out.println(PartTwo(getSymbols(lines), getNumbers(lines)));
    }

    static int PartOne(List<Symbol> symbols, List<Number> numbers) {
        return symbols.stream()
                .filter(symbol -> symbol.value != '.')
                .flatMap(symbol -> numbers.stream().filter(symbol::isAdjacentTo).map(Number::value))
                .mapToInt(Integer::intValue)
                .sum();
    }

    static int PartTwo(List<Symbol> symbols, List<Number> numbers) {
        return symbols.stream()
                .filter(symbol -> symbol.value == '*')
                .map(symbol -> numbers.stream().filter(symbol::isAdjacentTo).toList())
                .filter(list -> list.size() == 2)
                .map(list -> list.stream().map(Number::value).reduce(1, (acc, curr) -> acc * curr))
                .mapToInt(Integer::intValue)
                .sum();
    }

    static List<Number> getNumbers(List<String> lines) {
        var numbers = new ArrayList<Number>();
        char[][] grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        for (int r = 0; r < grid.length; r++) {
            var digit = 0;
            Position start = null;
            for (int c = 0; c < grid[r].length; c++) {
                if (Character.isDigit(grid[r][c])) {
                    if (Objects.isNull(start)) start = new Position(r, c);
                    digit = (digit * 10) + grid[r][c] - '0';
                    continue;
                }
                if (Objects.nonNull(start)) {
                    numbers.add(new Number(digit, start, new Position(r, c - 1)));
                    // resets
                    digit = 0;
                    start = null;
                }
            }
            // add possible number at edge of grid
            if (Objects.nonNull(start)) {
                numbers.add(new Number(digit, start, new Position(r, grid[r].length - 1)));
            }
        }

        return numbers;
    }

    static List<Symbol> getSymbols(List<String> lines) {
        return IntStream.range(0, lines.size()).mapToObj(r ->
                IntStream.range(0, lines.get(r).length()).mapToObj(c -> !Character.isDigit(lines.get(r).charAt(c)) &&
                        lines.get(r).charAt(c) != '.' ? new Symbol(lines.get(r).charAt(c), new Position(r, c)) : null
                ).filter(Objects::nonNull)
        ).flatMap(Function.identity()).toList();
    }

    record Position(int x, int y) {
        public boolean inRange(Position start, Position end) {
            if (x < start.x - 1 || x > end.x + 1) return false;
            return y >= start.y - 1 && y <= end.y + 1;
        }
    }

    record Number(int value, Position start, Position end) {
    }

    record Symbol(char value, Position position) {
        public boolean isAdjacentTo(Number number) {
            return position.inRange(number.start, number.end);
        }
    }
}
