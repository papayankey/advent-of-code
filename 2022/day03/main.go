package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
	"unicode"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 3)
	data := readLines("day03/input.txt")

	result := PartOne(data)
	fmt.Println(result)

	result = PartTwo(data)
	fmt.Println(result)
}

func PartTwo(data []string) int {
	sum := 0

	for i := 0; i < len(data); i += 3 {
		setA := set(data[i])
		setB := set(data[i+1])
		setC := set(data[i+2])

		for v := range setA {
			if setB[v] && setC[v] {
				sum += priority(v)
			}
		}
	}

	return sum
}

func PartOne(data []string) int {
	sum := 0

	for _, d := range data {
		setA := set(d[:len(d)/2])
		setB := set(d[len(d)/2:])

		for v := range setB {
			if setA[v] {
				sum += priority(v)
			}
		}
	}

	return sum
}

func priority(r rune) int {
	isLower := unicode.IsLower(r)
	if isLower {
		return int(r - 'a' + 1)
	}
	return int(r - 'A' + 27)
}

func set(d string) map[rune]bool {
	m := map[rune]bool{}
	for _, v := range d {
		m[v] = true
	}
	return m
}

func readLines(fileName string) []string {
	f, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()

	scanner := bufio.NewScanner(f)

	lines := []string{}

	for scanner.Scan() {
		line := strings.Trim(scanner.Text(), " ")
		lines = append(lines, line)
	}

	return lines
}
