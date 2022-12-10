package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

func main() {
	d := readLines("input.txt")

	result := partOne(d)
	fmt.Println("Part 1:", result)

	result = partTwo(d)
	fmt.Println("Part 2:", result)
}

func partTwo(data [][]string) int {
	aim := 0
	horizontal := 0
	depth := 0
	for _, v := range data {
		num, _ := strconv.Atoi(v[1])
		switch v[0] {
		case "forward":
			horizontal += num
			depth += (aim * num)
		case "down":
			aim += num
		case "up":
			aim -= num
		}
	}
	return horizontal * depth
}

func partOne(data [][]string) int {
	horizontal := 0
	depth := 0
	for _, v := range data {
		num, _ := strconv.Atoi(v[1])
		switch v[0] {
		case "forward":
			horizontal += num
		case "down":
			depth += num
		case "up":
			depth -= num
		}
	}
	return horizontal * depth
}

func readLines(fileName string) [][]string {
	f, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()
	scanner := bufio.NewScanner(f)
	data := [][]string{}
	for scanner.Scan() {
		fields := strings.Fields(scanner.Text())
		data = append(data, fields)
	}
	return data
}
