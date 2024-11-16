package com.github.papayankey.AoC_2023.day_06;

import com.github.papayankey.AoC;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class WaitForIt {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 6);

        System.out.println(PartOne(getPairs(input)));
        System.out.println(PartTwo(getPairs(input)));
    }

    static long PartOne(List<Pair> pairs) {
        return pairs.stream().map(WaitForIt::getHigherDistanceCount)
                .reduce(1L, (a, c) -> a * c);
    }

    static long PartTwo(List<Pair> pairs) {
        List<String> pair = pairs.stream().collect(Collectors.teeing(
                Collectors.mapping(p -> String.valueOf(p.time), Collectors.joining("")),
                Collectors.mapping(p -> String.valueOf(p.distance), Collectors.joining("")),
                List::of
        ));

        return WaitForIt.getHigherDistanceCount(new Pair(pair.getFirst(), pair.getLast()));
    }

    static long getHigherDistanceCount(Pair pair) {
        return LongStream.range(1, pair.time)
                .mapToObj(t -> t * (pair.time - t))
                .filter(d -> d > pair.distance)
                .count();
    }

    static List<Pair> getPairs(List<String> lines) {
        var iterator = lines.iterator();
        var time = iterator.next().replaceAll("Time:\\s+", "").split("\\s+");
        var distance = iterator.next().replaceAll("Distance:\\s+", "").split("\\s+");

        return IntStream.range(0, time.length)
                .mapToObj(i -> new Pair(time[i], distance[i]))
                .toList();
    }

    record Pair(long time, long distance) {
        Pair(String time, String distance) {
            this(Long.parseLong(time), Long.parseLong(distance));
        }
    }
}
