package main

import (
	"fmt"
	"sort"
	"strings"

	"github.com/papayankey/utils"
)

type Monkey struct {
	Items    []int
	Op       []string
	Test     int
	Decision map[bool]int
}

func main() {
	utils.AoC(2022, 11)

	result := PartOne(20, "day11/input.txt")
	fmt.Println("Part one:", result)

	result = PartTwo(10000, "day11/input.txt")
	fmt.Println("Part two:", result)
}

func PartTwo(rounds int, input string) int {
	monkeys := readLines(input)
	inspections := map[int]int{}

	// math trick to manage elf's worry levels
	// - get all divisors and find LCM for all
	divisors := []int{}
	for _, m := range monkeys {
		divisors = append(divisors, m.Test)
	}

	for i := 1; i <= rounds; i++ {
		for current, monkey := range monkeys {
			for _, old := range monkey.Items {
				var item int
				if monkey.Op[1] == "old" {
					item = old
				} else {
					item = utils.Atoi(monkey.Op[1])
				}
				val := Op(monkey.Op[0], old, item) % utils.LCM(divisors...)
				var m *Monkey
				if val%monkey.Test == 0 {
					m = monkeys[monkey.Decision[true]]
				} else {
					m = monkeys[monkey.Decision[false]]
				}
				// throw item to another monkey to inspect
				m.Items = append(m.Items, val)
				inspections[current] += 1
			}
			// clear monkey's items
			monkey.Items = monkey.Items[:0]
		}
	}

	values := utils.MapValues(inspections)
	sort.Ints(values)

	return values[len(values)-2] * values[len(values)-1]
}

func PartOne(rounds int, input string) int {
	monkeys := readLines(input)
	inspections := map[int]int{}

	for i := 1; i <= rounds; i++ {
		for current, monkey := range monkeys {
			for _, old := range monkey.Items {
				var item int
				if monkey.Op[1] == "old" {
					item = old
				} else {
					item = utils.Atoi(monkey.Op[1])
				}
				val := int(Op(monkey.Op[0], old, item) / 3)
				var m *Monkey
				if val%monkey.Test == 0 {
					m = monkeys[monkey.Decision[true]]
				} else {
					m = monkeys[monkey.Decision[false]]
				}
				// throw item to another monkey to inspect
				m.Items = append(m.Items, val)
				inspections[current] += 1
			}
			// clear monkey's items
			monkey.Items = monkey.Items[:0]
		}
	}

	values := utils.MapValues(inspections)
	sort.Ints(values)

	return values[len(values)-2] * values[len(values)-1]
}

func Op(op string, x, y int) int {
	var res int
	switch op {
	case "*":
		res = x * y
	case "+":
		res = x + y
	}
	return res
}

func readLines(name string) []*Monkey {
	lines := strings.Split(utils.ReadFile(name), "\n\n")
	monkeys := []*Monkey{}

	for _, line := range lines {
		line = utils.Replace(strings.TrimSpace(line), ":", "", "=", "")
		parts := strings.Split(line, "\n")

		items := strings.Split(strings.Fields(utils.Replace(parts[1], ", ", ","))[2], ",")

		monkey := Monkey{
			Items: utils.Map(items, func(s string) int { return utils.Atoi(s) }),
			Op: []string{
				strings.Fields(parts[2])[3],
				strings.Fields(parts[2])[4],
			},
			Test: utils.Atoi(strings.Fields(parts[3])[3]),
			Decision: map[bool]int{
				true:  utils.Atoi(strings.Fields(parts[4])[5]),
				false: utils.Atoi(strings.Fields(parts[5])[5]),
			},
		}
		monkeys = append(monkeys, &monkey)
	}
	return monkeys
}
