package com.github.papayankey.AoC_2023.day_04;

import com.github.papayankey.AoC;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScratchCards {
    public static void main(String[] args) {
        var lines = AoC.getInput(2023, 4);

        System.out.println(PartOne(lines));
        System.out.println(PartTwo(lines));
    }

    static int PartOne(List<String> lines) {
        var totalPoints = 0;

        for (var line : lines) {
            var parts = line.replaceAll("Card\\s+\\d+:\\s", "").split("\\|");
            var winnings = parts[0].trim().split("\\s+");

            Map<String, String> map = Arrays.stream(parts[1].trim().split("\\s+"))
                    .collect(Collectors.toMap(Function.identity(), _ -> ""));

            var cardPoint = 0;

            for (String win : winnings) {
                if (map.containsKey(win)) {
                    if (cardPoint == 0) cardPoint += 1;
                    else cardPoint *= 2;
                }
            }

            totalPoints += cardPoint;
        }

        return totalPoints;
    }

    static int PartTwo(List<String> lines) {
        Map<Integer, Card> cards = new TreeMap<>();

        for (int i = 0; i < lines.size(); i++) {
            var parts = lines.get(i).replaceAll("Card\\s+\\d+:\\s", "").split("\\|");

            int matches = (int) Arrays.stream(parts[0].trim().split("\\s+"))
                    .flatMap(win -> Arrays.stream(parts[1].trim().split("\\s+")).filter(win::equals))
                    .count();

            cards.put(i, new Card(i, matches, 0));
        }

        cards.entrySet().forEach(card -> {
            int matches = card.getValue().matches;
            if (matches > 0) {
                duplicateCard(cards, card, matches);
                IntStream.range(0, card.getValue().copies).forEach(_ -> duplicateCard(cards, card, matches));
            }
        });

        return cards.values().stream().mapToInt(Card::copies).sum() + cards.size();
    }

    static void duplicateCard(Map<Integer, Card> cards, Map.Entry<Integer, Card> card, int match) {
        IntStream.rangeClosed(1, match).forEach(i ->
                cards.computeIfPresent(card.getKey() + i, (_, c) -> new Card(c.id, c.matches, c.copies + 1)));
    }

    record Card(int id, int matches, int copies) {
    }
}
