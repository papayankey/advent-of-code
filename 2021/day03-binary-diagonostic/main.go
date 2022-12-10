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
	lines := readLines("input.txt")

	pc := partOne(lines)
	fmt.Println("Part 1:", pc)

	pc = partTwo(lines)
	fmt.Println("Part 2:", pc)
}

func partTwo(d [][]string) int {
	oxygenRating := getRating(d, true)
	carbonDioxideRating := getRating(d, false)

	return oxygenRating * carbonDioxideRating
}

func partOne(d [][]string) int {
	var gammaRateBuffer strings.Builder
	rowSize := len(d)
	colSize := len(d[0])
	for col := 0; col < colSize; col++ {
		zeros := 0
		ones := 0
		for row := 0; row < rowSize; row++ {
			v, _ := strconv.Atoi(d[row][col])
			if v == 0 {
				zeros++
			} else {
				ones++
			}
		}
		if zeros > ones {
			gammaRateBuffer.WriteString("0")
		} else {
			gammaRateBuffer.WriteString("1")
		}
	}

	gammaRate := gammaRateBuffer.String()

	var epsilonRateBuffer strings.Builder
	for _, r := range gammaRate {
		if string(r) == "1" {
			epsilonRateBuffer.WriteString("0")
		} else {
			epsilonRateBuffer.WriteString("1")
		}
	}
	epsilonRate := epsilonRateBuffer.String()

	powerConsumption := binaryToDecimal(gammaRate) * binaryToDecimal(epsilonRate)
	return powerConsumption
}

func getRating(d [][]string, isOxygenRating bool) int {
	result := []string{}
	for j := 0; j < len(d[0]); j++ {
		zeros := 0
		ones := 0
		for i := 0; i < len(d); i++ {
			if d[i][j] == "1" {
				ones++
			} else {
				zeros++
			}
		}
		dd := [][]string{}
		keepValue := ""
		if isOxygenRating {
			if zeros == ones {
				keepValue = "1"
			} else {
				keepValue = max(zeros, ones)
			}
		} else {
			if zeros == ones {
				keepValue = "0"
			} else {
				keepValue = min(zeros, ones)
			}
		}
		for i := 0; i < len(d); i++ {
			if d[i][j] == keepValue {
				dd = append(dd, d[i])
			}
		}
		d = dd
		if len(d) == 1 {
			result = append(result, d[0]...)
			break
		}

	}
	v, _ := strconv.ParseInt(strings.Join(result, ""), 2, 64)
	return int(v)
}

func max(zeros, ones int) string {
	if zeros > ones {
		return "0"
	}
	return "1"
}

func min(zeros, ones int) string {
	if zeros < ones {
		return "0"
	}
	return "1"
}

func binaryToDecimal(binary string) int {
	v, _ := strconv.ParseInt(binary, 2, 64)
	return int(v)
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
		res := strings.Split(scanner.Text(), "")
		data = append(data, res)
	}
	return data
}
