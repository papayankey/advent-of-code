package com.github.papayankey.AoC_2023.day_09;

import com.github.papayankey.AoC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MirageMaintenance {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 9);

        System.out.println(PartOne(input));
        System.out.println(PartTwo(input));
    }

    static long PartOne(List<String> lines) {
        return sumExtrapolatedValues(lines, false);
    }

    static long PartTwo(List<String> lines) {
        return sumExtrapolatedValues(lines, true);
    }

    static long sumExtrapolatedValues(List<String> lines, boolean getPrevious) {
        long extrapolateSum = 0L;

        for (var line : lines) {
            List<Long> values = Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
            List<Long> firstOrLastElem = new ArrayList<>(List.of(getPrevious ? values.getFirst() : values.getLast()));

            while (notZeroes(values)) {
                List<Long> subSequence = new ArrayList<>();

                for (int i = 0, j = 1; j < values.size(); i++, j++) {
                    subSequence.add(values.get(j) - values.get(i));
                }

                values = subSequence;
                firstOrLastElem.add(getPrevious ? values.getFirst() : values.getLast());
            }

            if (getPrevious) {
                extrapolateSum += firstOrLastElem.reversed().stream().reduce(0L, (a, b) -> b - a);
                continue;
            }

            extrapolateSum += firstOrLastElem.stream().reduce(0L, Long::sum);
        }

        return extrapolateSum;
    }

    static boolean notZeroes(List<Long> subSeq) {
        return !subSeq.stream().allMatch(elem -> elem == 0);
    }
}
