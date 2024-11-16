package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 4)
	data := readLines("day04/input.txt")

	result := PartOne(data)
	fmt.Println(result)

	result = PartTwo(data)
	fmt.Println(result)
}

func PartTwo(data [][]int) int {
	noOverlap := 0
	for _, d := range data {
		x1, y1, x2, y2 := d[0], d[1], d[2], d[3]
		if y1 < x2 || y2 < x1 {
			noOverlap += 1
		}
	}
	return len(data) - noOverlap
}

func PartOne(data [][]int) int {
	overlap := 0
	for _, d := range data {
		x1, y1, x2, y2 := d[0], d[1], d[2], d[3]
		if x1 >= x2 && y1 <= y2 || x2 >= x1 && y2 <= y1 {
			overlap += 1
		}
	}
	return overlap
}

func readLines(name string) [][]int {
	f, err := os.Open(name)
	utils.CheckErr(err)
	defer f.Close()

	scanner := bufio.NewScanner(f)
	lines := [][]int{}

	for scanner.Scan() {
		s := strings.Split(strings.Replace(scanner.Text(), "-", " ", -1), ",")
		line := []int{}
		for _, i := range s {
			v := strings.Split(i, " ")
			for _, j := range v {
				line = append(line, utils.Atoi(j))
			}
		}
		lines = append(lines, line)
	}

	return lines
}
