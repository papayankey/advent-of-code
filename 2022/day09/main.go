package main

import (
	"fmt"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 9)

	moves := readLines("day09/input.txt")

	result := PartOne(moves)
	fmt.Println("Total tail visits at least once:", result)

	result = PartTwo(moves)
	fmt.Println("Total tail visits at least once:", result)
}

func PartTwo(moves [][]string) int {
	// knots initial state
	knots := make([][]int, 10)
	for k := range knots {
		knots[k] = []int{16, 12}
	}

	TailVisits := map[string]bool{"(16,12)": true}

	for _, move := range moves {
		direction, amount := move[0], utils.Atoi(move[1])
		for i := 1; i <= amount; i++ {
			// move head knot
			knots[0][0] += directions(direction)[0]
			knots[0][1] += directions(direction)[1]
			// move respective knots accordingly
			for j := 1; j < len(knots); j++ {
				head := knots[j-1]
				tail := knots[j]
				// two knots are not touching if the difference between row or cols is greater than 1
				rowDifference := head[0] - tail[0]
				colDifference := head[1] - tail[1]
				notTouchingKnots := utils.Abs(rowDifference) > 1 || utils.Abs(colDifference) > 1
				if notTouchingKnots {
					tail[0] += utils.Sign(rowDifference)
					tail[1] += utils.Sign(colDifference)
					if j == len(knots)-1 {
						TailVisits[fmt.Sprintf("(%v,%v)", tail[0], tail[1])] = true
					}
				}
			}
		}
	}

	return len(TailVisits)
}

func PartOne(moves [][]string) int {
	head := []int{5, 1}
	tail := []int{5, 1}

	TailVisits := map[string]bool{"(5,1)": true}

	for _, move := range moves {
		direction, amount := move[0], utils.Atoi(move[1])
		for i := 1; i <= amount; i++ {
			// move head knot
			head[0] += directions(direction)[0]
			head[1] += directions(direction)[1]

			rowDifference := head[0] - tail[0]
			colDifference := head[1] - tail[1]
			notTouchingKnots := utils.Abs(rowDifference) > 1 || utils.Abs(colDifference) > 1

			// move tail knot if distance between cols or rows is greater than 1
			if notTouchingKnots {
				tail[0] += utils.Sign(rowDifference)
				tail[1] += utils.Sign(colDifference)
				TailVisits[fmt.Sprintf("(%v,%v)", tail[0], tail[1])] = true
			}
		}
	}
	return len(TailVisits)
}

func directions(name string) []int {
	dirs := map[string][]int{
		"R": {0, 1}, "L": {0, -1},
		"U": {-1, 0}, "D": {1, 0},
	}
	return dirs[name]
}

func readLines(name string) [][]string {
	lines := strings.Split(utils.ReadFile(name), "\n")
	moves := [][]string{}

	for _, line := range lines {
		moves = append(moves, strings.Fields(line))
	}

	return moves
}
