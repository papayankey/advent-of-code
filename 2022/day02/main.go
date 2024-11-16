package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 2)
	data := readLines("day02/input.txt")

	result := PartOne(data)
	fmt.Println(result)

	result = PartTwo(data)
	fmt.Println(result)
}

func PartTwo(d [][]string) int {
	scores := map[string]int{
		"A": 1,
		"B": 2,
		"C": 3,
	}

	strategy := map[string]int{
		"X": 0,
		"Y": 3,
		"Z": 6,
	}

	game := map[string]string{
		"AX": "AC",
		"AY": "AA",
		"AZ": "AB",
		"BX": "BA",
		"BY": "BB",
		"BZ": "BC",
		"CX": "CB",
		"CY": "CC",
		"CZ": "CA",
	}

	totalScore := 0
	for _, v := range d {
		key := fmt.Sprintf("%v%v", v[0], v[1])
		mapped := strings.Split(game[key], "")[1]
		totalScore += strategy[v[1]] + scores[mapped]
	}

	return totalScore
}

func PartOne(d [][]string) int {
	scores := map[string]int{
		"lose": 0,
		"draw": 3,
		"win":  6,
	}

	strategy := map[string]int{
		"X": 1,
		"Y": 2,
		"Z": 3,
	}

	totalScore := 0
	for _, v := range d {
		key := fmt.Sprintf("%v%v", v[0], v[1])
		if key == "AX" || key == "BY" || key == "CZ" {
			totalScore += scores["draw"] + strategy[v[1]]
		} else if key == "AY" || key == "BZ" || key == "CX" {
			totalScore += scores["win"] + strategy[v[1]]
		} else {
			totalScore += scores["lose"] + strategy[v[1]]
		}
	}

	return totalScore
}

func readLines(fileName string) [][]string {
	f, err := os.Open(fileName)

	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()

	scanner := bufio.NewScanner(f)

	lines := [][]string{}

	for scanner.Scan() {
		line := strings.Fields(strings.Trim(scanner.Text(), " "))
		lines = append(lines, line)
	}

	return lines
}
