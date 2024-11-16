package com.github.papayankey.AoC_2023.day_08;

import com.github.papayankey.AoC;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HauntedWasteland {
    public static void main(String[] args) {
        var input = AoC.getInput(2023, 8);
        var inputString = String.join("\n", input);

        System.out.println(PartOne(inputString));
        System.out.println(PartTwo(inputString));
    }

    static long PartOne(String input) {
        return calculateSteps(getNodes(input), input, "AAA", "ZZZ");
    }

    static long PartTwo(String input) {
        Map<String, Node> nodes = getNodes(input);

        return nodes.keySet().stream().filter(name -> name.endsWith("A"))
                .map(node -> calculateSteps(nodes, input, node, "Z"))
                .reduce(lcm())
                .orElse(0L);
    }

    static long calculateSteps(Map<String, Node> nodes, String inputString, String startNode, String endSuffix) {
        List<Path> paths = getPaths(inputString);
        var steps = 0L;

        for (int i = 0; ; i++) {
            if (startNode.endsWith(endSuffix)) {
                break;
            }
            Node node = nodes.get(startNode);
            Path path = paths.get(i %= paths.size());
            startNode = path.name().equals("L") ? node.left() : node.right();
            steps++;
        }

        return steps;
    }

    static BinaryOperator<Long> lcm() {
        return (a, b) -> a * b / gcd(a, b);
    }

    /* GCD using Euclidean algorithm */
    static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long temp = a;
            a = b;
            b = temp % b;
        }
        return a;
    }

    static Map<String, Node> getNodes(String input) {
        var pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

        Function<String, Optional<Node>> createNode = line -> {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return Optional.of(new Node(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
            return Optional.empty();
        };

        return Arrays.stream(input.split("\n\n")[1].split("\n"))
                .map(createNode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Node::name, Function.identity()));
    }

    static List<Path> getPaths(String input) {
        return Arrays.stream(input.split("\n\n")[0].split(""))
                .map(Path::new)
                .toList();
    }

    record Node(String name, String left, String right) {
    }

    record Path(String name) {
    }
}
