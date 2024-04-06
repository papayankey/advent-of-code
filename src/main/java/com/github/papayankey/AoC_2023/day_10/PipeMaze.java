package com.github.papayankey.AoC_2023.day_10;

import com.github.papayankey.AoC;

import java.util.*;

import static com.github.papayankey.AoC_2023.day_10.PipeMaze.Direction.*;

public class PipeMaze {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 10);

        System.out.println(PartOne(input));
    }

    static int PartOne(List<String> lines) {
        List<Tile> coordinates = loopCoordinates(buildGrid(lines));
        return coordinates.getLast().distance();
    }

    static Grid buildGrid(List<String> lines) {
        Tile[][] tiles = new Tile[lines.size()][lines.getFirst().length()];
        Tile startTile = null;

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(row).length(); col++) {
                char c = lines.get(row).charAt(col);
                Tile newTile = new Tile(c, row, col);
                if (c == 'S') startTile = newTile;
                tiles[row][col] = newTile;
            }
        }

        return new Grid(tiles, startTile);
    }

    static List<Tile> loopCoordinates(Grid grid) {
        List<Tile> coordinates = new ArrayList<>();
        Map<String, Boolean> visited = new LinkedHashMap<>();
        Queue<Tile> queue = new LinkedList<>();

        Tile start = grid.start();
        queue.add(start);
        visited.put(STR."\{start.row()},\{start.col()}", true);
        coordinates.add(start);

        while (!queue.isEmpty()) {
            Tile tile = queue.poll();
            List<Tile> neighbors = tile.neighbors(grid.tiles());
            for (Tile neighbor : neighbors) {
                String key = STR."\{neighbor.row()},\{neighbor.col()}";
                if (!visited.containsKey(key)) {
                    visited.put(key, true);
                    queue.add(neighbor);
                    coordinates.add(neighbor);
                }
            }
        }

        return coordinates;
    }

    record Grid(Tile[][] tiles, Tile start) {
    }

    record Tile(char symbol, int row, int col, int distance) {

        public Tile(char symbol, int row, int col) {
            this(symbol, row, col, 0);
        }

        public List<Tile> neighbors(Tile[][] tiles) {
            List<Tile> neighbors = new ArrayList<>();

            for (var direction : values()) {
                int r = row + direction.row();
                int c = col + direction.col();

                if (!inRange(r, c, tiles) || !isPipe(tiles[r][c].symbol())) continue;
                char neighborSymbol = tiles[r][c].symbol();

                if (direction.equals(NORTH) && isValidMovement(symbol, neighborSymbol, NORTH)) {
                    neighbors.add(new Tile(neighborSymbol, row - 1, col, distance + 1));
                } else if (direction.equals(SOUTH) && isValidMovement(symbol, neighborSymbol, SOUTH)) {
                    neighbors.add(new Tile(neighborSymbol, row + 1, col, distance + 1));
                } else if (direction.equals(EAST) && isValidMovement(symbol, neighborSymbol, EAST)) {
                    neighbors.add(new Tile(neighborSymbol, row, col + 1, distance + 1));
                } else if (direction.equals(WEST) && isValidMovement(symbol, neighborSymbol, WEST)) {
                    neighbors.add(new Tile(neighborSymbol, row, col - 1, distance + 1));
                }
            }

            return neighbors;
        }

        private boolean isValidMovement(char current, char neighbor, Direction direction) {
            var movements = Map.of(
                    NORTH, List.of("S|LJ", "|7F"),
                    SOUTH, List.of("S|F7", "|LJ"),
                    EAST, List.of("S-FL", "-7J"),
                    WEST, List.of("S-7J", "-FL")
            );
            return movements.entrySet().stream()
                    .anyMatch(move -> move.getKey().equals(direction) && move.getValue().getFirst().contains(String.valueOf(current)) &&
                            move.getValue().getLast().contains(String.valueOf(neighbor)));
        }

        private boolean inRange(int row, int col, Tile[][] tiles) {
            return row >= 0 && row <= tiles.length - 1 && col >= 0 && col <= tiles[0].length - 1;
        }

        private boolean isPipe(char c) {
            return c == '|' || c == '7' || c == '-' || c == 'J' || c == 'F' || c == 'L';
        }
    }

    enum Direction {
        NORTH(-1, 0), SOUTH(1, 0), EAST(0, 1), WEST(0, -1);
        private final int row;
        private final int col;

        Direction(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int row() {
            return row;
        }

        public int col() {
            return col;
        }
    }
}
