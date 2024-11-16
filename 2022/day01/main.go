package main

import (
	"bufio"
	"fmt"
	"log"
	"math"
	"os"
	"sort"
	"strconv"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 1)
	data := readLines("day01/input.txt")

	// Elf with most carrying calories
	result := PartOne(data)
	fmt.Println(result)

	// Last three Elves with most carrying calories
	result = PartTwo(data)
	fmt.Println(result)
}

func PartTwo(d []int) int {
	sort.Ints(d)
	size := len(d)
	return d[size-1] + d[size-2] + d[size-3]
}

func PartOne(calories []int) int {
	max := math.MinInt
	for _, cal := range calories {
		if cal > max {
			max = cal
		}
	}
	return max
}

func readLines(fileName string) []int {
	f, err := os.Open(fileName)

	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()

	scanner := bufio.NewScanner(f)

	lines := []string{}
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}
	lines = append(lines, "")

	data := []int{}
	sums := 0

	for _, v := range lines {
		if len(v) != 0 {
			num, _ := strconv.Atoi(v)
			sums += num
		} else {
			data = append(data, sums)
			sums = 0
		}
	}

	return data
}
