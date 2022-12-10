package main

import (
	"bufio"
	"fmt"
	"log"
	"math"
	"os"
	"strconv"
	"strings"
)

func main() {
	// horizontal and vertical vents
	overlappingPointsCount := partOne(false)
	fmt.Println("Part one:", overlappingPointsCount)

	// including diagonal vents
	overlappingPointsCount = partTwo(true)
	fmt.Println("Part two:", overlappingPointsCount)
}

func partOne(withDiagonal bool) int {
	overlappingPoints := getOverlappingPoints("input.txt", withDiagonal)
	return overlappingPoints
}

func partTwo(withDiagonal bool) int {
	overlappingPointss := getOverlappingPoints("input.txt", withDiagonal)
	return overlappingPointss
}

func getOverlappingPoints(fileName string, withDiagonal bool) int {
	lines := readLines(fileName)
	pointCount := map[string]int{}

	for _, line := range lines {
		x1, y1 := line[0], line[1]
		x2, y2 := line[2], line[3]

		if isVertical(x1, x2) {
			minY := min(y1, y2)
			maxY := max(y1, y2)
			for y := minY; y <= maxY; y++ {
				point := fmt.Sprintf("%d,%d", x1, y)
				pointCount[point]++
			}
		}

		if isHorizontal(y1, y2) {
			minX := min(x1, x2)
			maxX := max(x1, x2)
			for x := minX; x <= maxX; x++ {
				point := fmt.Sprintf("%d,%d", x, y1)
				pointCount[point]++
			}
		}

		if withDiagonal && isDiagonal(x1, x2, y1, y2) {
			xRange := getRange(x1, x2)
			yRange := getRange(y1, y2)
			for k, x := range xRange {
				y := yRange[k]
				point := fmt.Sprintf("%d,%d", x, y)
				pointCount[point]++
			}
		}
	}
	return calculateOverlappingPoints(pointCount)
}

func calculateOverlappingPoints(pointCount map[string]int) int {
	sum := 0
	for _, v := range pointCount {
		if v >= 2 {
			sum++
		}
	}
	return sum
}

func getRange(a1, a2 int) []int {
	values := []int{}
	steps := int(math.Abs(float64(a1)-float64(a2))) + 1
	for step := 0; step < steps; step++ {
		if a1 < a2 {
			values = append(values, a1+step)
		} else {
			values = append(values, a1-step)
		}
	}
	return values
}

func isDiagonal(x1, x2, y1, y2 int) bool {
	return x1 != x2 && y1 != y2
}

func isHorizontal(x1, x2 int) bool {
	return x1 == x2
}

func isVertical(y1, y2 int) bool {
	return y1 == y2
}

func min(x, y int) int {
	if x < y {
		return x
	}
	return y
}

func max(x, y int) int {
	if x > y {
		return x
	}
	return y
}

func readLines(fileName string) [][]int {
	f, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()
	scanner := bufio.NewScanner(f)
	lines := [][]int{}
	for scanner.Scan() {
		points := strings.Split(strings.Replace(scanner.Text(), " -> ", ",", 1), ",")
		line := []int{}
		for _, point := range points {
			v, _ := strconv.Atoi(point)
			line = append(line, v)
		}
		lines = append(lines, line)
	}
	return lines
}
