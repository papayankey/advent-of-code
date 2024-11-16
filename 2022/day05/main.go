package main

import (
	"fmt"
	"strconv"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 5)

	result := PartOne("day05/input.txt")
	fmt.Println(result)

	result = PartTwo("day05/input.txt")
	fmt.Println(result)
}

func PartTwo(filename string) string {
	stacks, moves := readLines(filename)
	for _, m := range moves {
		temp := []string{}
		count, from, to := m[0], m[1]-1, m[2]-1
		for i := 1; i <= count; i++ {
			taken := stacks[from][len(stacks[from])-1]
			stacks[from] = stacks[from][:len(stacks[from])-1]
			temp = append(temp, taken)
		}
		for len(temp) > 0 {
			stacks[to] = append(stacks[to], temp[len(temp)-1])
			temp = temp[:len(temp)-1]
		}
	}
	return message(stacks)
}

func PartOne(filename string) string {
	stacks, moves := readLines(filename)
	for _, m := range moves {
		count, from, to := m[0], m[1]-1, m[2]-1
		for i := 1; i <= count; i++ {
			taken := stacks[from][len(stacks[from])-1]
			stacks[from] = stacks[from][:len(stacks[from])-1]
			stacks[to] = append(stacks[to], taken)
		}
	}
	return message(stacks)
}

func message(stacks [][]string) string {
	sb := strings.Builder{}
	for _, stack := range stacks {
		sb.WriteString(stack[len(stack)-1])
	}
	return sb.String()
}

func readLines(name string) ([][]string, [][]int) {
	lines := strings.Split(utils.ReadFile(name), "\r\n\r\n")
	s := strings.Split(strings.Replace(lines[0], "    ", "[] ", -1), "\r\n")

	stacks := [][]string{}
	for _, line := range s[:len(s)-1] {
		stacks = append(stacks, strings.Split(line, " "))
	}

	stacks = utils.Transpose(stacks)
	replacer := strings.NewReplacer("[", "", "]", "")

	for k, stack := range stacks {
		stacks[k] = strings.Split(utils.ReverseString(replacer.Replace(strings.Join(stack, ""))), "")
	}

	moves := [][]int{}
	line := strings.Split(lines[1], "\r\n")
	for _, l := range line {
		f := strings.Fields(l)
		t := []int{}
		for _, v := range f {
			vv, err := strconv.Atoi(v)
			if err == nil {
				t = append(t, vv)
			}
		}
		moves = append(moves, t)
	}
	return stacks, moves
}
