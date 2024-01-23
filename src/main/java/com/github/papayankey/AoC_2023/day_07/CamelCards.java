package com.github.papayankey.AoC_2023.day_07;

import com.github.papayankey.AoC;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.github.papayankey.AoC_2023.day_07.CamelCards.Kind.*;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class CamelCards {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 7);

        System.out.println(PartOne(input));
    }

    static long PartOne(List<String> lines) {
        long[] rank = {1};
        return lines.stream().map(CamelCards::getHand)
                .sorted()
                .mapToLong(hand -> hand.bid * rank[0]++)
                .sum();
    }

    static long PartTwo(List<String> lines) {
        return 0L;
    }

    static Hand getHand(String str) {
        List<String> pairs = Arrays.stream(str.split("\\s+")).toList();
        List<Card> cards = Arrays.stream(pairs.getFirst().split("")).map(CamelCards::getCard).toList();

        List<Long> pattern = cards.stream().collect(groupingBy(Card::name, counting())).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getValue)
                .toList();

        return new Hand(cards, getKind(pattern), Long.parseLong(pairs.getLast()));
    }

    static Kind getKind(List<Long> pattern) {
        var size = pattern.size();
        if (size == 1) return FIVE_OF_KIND;
        if (size == 2) {
            if (pattern.getFirst() == 4) return FOUR_OF_KIND;
            return FULL_HOUSE;
        }
        if (size == 3) {
            if (pattern.getFirst() == 3) return THREE_OF_KIND;
            return TWO_PAIR;
        }
        if (size == 4) return ONE_PAIR;
        return HIGH_CARD;
    }

    static Card getCard(String str) {
        char c = str.charAt(0);
        return switch (c) {
            case 'A' -> new Card(c, 14);
            case 'K' -> new Card(c, 13);
            case 'Q' -> new Card(c, 12);
            case 'J' -> new Card(c, 11);
            case 'T' -> new Card(c, 10);
            default -> new Card(c, c - '0');
        };
    }

    enum Kind {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_KIND, FULL_HOUSE, FOUR_OF_KIND, FIVE_OF_KIND
    }

    record Hand(List<Card> cards, Kind kind, long bid) implements Comparable<Hand> {
        @Override
        public int compareTo(Hand other) {
            // compare by kind
            var result = this.kind.compareTo(other.kind);
            if (result != 0) return result;

            // compare by card rank
            final int CARDS_SIZE = 5;
            int index = 0;

            while (index < CARDS_SIZE) {
                result = this.cards.get(index).compareTo(other.cards.get(index));
                if (result != 0) return result;
                index++;
            }

            return 0;
        }
    }

    record Card(char name, int rank) implements Comparable<Card> {
        @Override
        public int compareTo(Card other) {
            return Integer.compare(this.rank, other.rank);
        }
    }
}
