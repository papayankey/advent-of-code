package com.github.papayankey.AoC_2023.day_04;

import com.github.papayankey.AoC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class ScratchCards {
    public static void main(String[] args) {
        var lines = AoC.getInput(2023, 4);

        // Part One
        var result = partOne(lines);
        System.out.println(result);

        // Part Two
        result = partTwo(lines);
        System.out.println(result);
    }

    public static int partOne(List<String> lines) {
        var totalPoints = 0;

        for (var line : lines) {
            var parts = line.replaceAll("Card\\s+\\d+:\\s", "").split("\\|");
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

        return totalPoints;
    }

    public static int partTwo(List<String> lines) {
        Map<Integer, Card> cards = new TreeMap<>();

        for (var line : lines) {
            var parts = line.replaceAll("Card\\s+", "").split(": ");
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

            cards.put(cardNumber, new Card(cardNumber, matches, 0));
        }

        for (Map.Entry<Integer, Card> card : cards.entrySet()) {
            var match = card.getValue().match;

            if (match > 0) {
                duplicateCard(cards, card, match);
                var copy = card.getValue().copy;

                if (copy > 0) {
                    IntStream.rangeClosed(1, copy).forEach(_ -> duplicateCard(cards, card, match));
                }
            }
        }

        return cards.values().stream().mapToInt(Card::copy).sum() + cards.size();
    }

    private static void duplicateCard(Map<Integer, Card> cards, Map.Entry<Integer, Card> card, int match) {
        IntStream.rangeClosed(1, match).forEach(i ->
                cards.computeIfPresent(card.getKey() + i, (_, c) -> new Card(c.id, c.match, c.copy + 1)));
    }

    record Card(int id, int match, int copy) {
    }
}
