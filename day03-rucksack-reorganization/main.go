package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"unicode"
)

func main() {
	data := readLines("input.txt")

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
		left := d[:len(d)/2]
		right := d[len(d)/2:]

		setA := set(left)
		setB := set(right)

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
		line := scanner.Text()
		lines = append(lines, line)
	}

	return lines
}
