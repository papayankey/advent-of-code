package com.github.papayankey.day_04;

import com.github.papayankey.AoC;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ScratchCards {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 4);

        // Part One
        var result = partOne(input);
        System.out.println(result);

        // Part Two
        result = partTwo(input);
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
        Map<Integer, Card> cards = new TreeMap<>();

        try (var scanner = new Scanner(inputSource)) {
            while (scanner.hasNextLine()) {
                var parts = scanner.nextLine().replaceAll("Card\\s+", "").split(": ");
                var cardNumber = Integer.parseInt(parts[0]);
                var numbers = parts[1].split("\\|");
                var winnings = numbers[0].trim().split("\\s+");
                var numbersInHand = numbers[1].trim().split("\\s+");

                var map = new HashMap<String, String>();
                for (String num : numbersInHand) {
                    map.put(num, null);
                }

                var matches = 0;
                for (String win : winnings) {
                    if (map.containsKey(win)) {
                        matches++;
                    }
                }

                cards.put(cardNumber, new Card(cardNumber, matches));
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        for (Map.Entry<Integer, Card> card : cards.entrySet()) {
            var match = card.getValue().getMatch();

            if (match == 0) {
                continue;
            }

            addCopy(cards, card, match);
            var copy = card.getValue().getCopy();

            if (copy > 0) {
                for (int i = 1; i <= copy; i++) {
                    addCopy(cards, card, match);
                }
            }
        }

        int totalCopies = cards.values().stream().mapToInt(Card::getCopy).sum();

        return totalCopies + cards.size();
    }

    private static void addCopy(Map<Integer, Card> cards, Map.Entry<Integer, Card> card, int match) {
        for (int i = 1; i <= match; i++) {
            var next = cards.get(card.getKey() + i);
            next.setCopy(next.getCopy() + 1);
        }
    }

    private static class Card {
        private final int id;
        private final int match;
        private int copy;

        public Card(int id, int match) {
            this.id = id;
            this.match = match;
            this.copy = 0;
        }

        public int getMatch() {
            return match;
        }

        public int getCopy() {
            return copy;
        }

        public void setCopy(int copy) {
            this.copy = copy;
        }

        @Override
        public String toString() {
            return "Card [id=" + id + ", match=" + match + ", copy=" + copy + "]";
        }
    }
}
