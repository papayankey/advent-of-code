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
        System.out.println(PartTwo(input));
    }

    static long PartOne(List<String> lines) {
        return calculateWinning(lines, false);
    }

    static long PartTwo(List<String> lines) {
        return calculateWinning(lines, true);
    }

    static long calculateWinning(List<String> lines, boolean addJokers) {
        long[] rank = {1};
        return lines.stream().map(line -> getHand(line, addJokers))
                .sorted()
                .mapToLong(hand -> hand.bid * rank[0]++)
                .sum();
    }

    static Hand getHand(String str, boolean addJokers) {
        List<String> pairs = Arrays.stream(str.split("\\s+")).toList();
        List<Card> cards = Arrays.stream(pairs.getFirst().split("")).map(s -> getCard(s, addJokers)).toList();

        List<Long> pattern = cards.stream().collect(groupingBy(Card::name, counting())).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getValue)
                .toList();

        return new Hand(cards, getKind(cards, pattern, addJokers), Long.parseLong(pairs.getLast()));
    }

    static Kind getKind(List<Card> cards, List<Long> pattern, boolean addJokers) {
        var jokerCount = cards.stream().filter(card -> card.name == 'J').count();
        var size = pattern.size();

        return switch (size) {
            case 1 -> FIVE_OF_KIND;
            case 2 -> {
                if (pattern.getFirst() == 4) {
                    yield addJokers && (jokerCount == 1 || jokerCount == 4) ? FIVE_OF_KIND : FOUR_OF_KIND;
                }
                yield addJokers && (jokerCount == 2 || jokerCount == 3) ? FIVE_OF_KIND : FULL_HOUSE;
            }
            case 3 -> {
                if (pattern.getFirst() == 3) {
                    yield addJokers && (jokerCount == 1 || jokerCount == 3) ? FOUR_OF_KIND : THREE_OF_KIND;
                }
                yield addJokers && jokerCount == 1 ?
                        FULL_HOUSE : addJokers && jokerCount == 2 ?
                        FOUR_OF_KIND : TWO_PAIR;
            }
            case 4 -> addJokers && (jokerCount == 1 || jokerCount == 2) ? THREE_OF_KIND : ONE_PAIR;
            default -> addJokers && jokerCount == 1 ? ONE_PAIR : HIGH_CARD;
        };
    }

    static Card getCard(String str, boolean addJokers) {
        char c = str.charAt(0);
        return switch (c) {
            case 'A' -> new Card(c, 14);
            case 'K' -> new Card(c, 13);
            case 'Q' -> new Card(c, 12);
            case 'J' -> addJokers ? new Card(c, 1) : new Card(c, 11);
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
