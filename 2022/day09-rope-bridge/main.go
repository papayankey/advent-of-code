package main

import (
	"fmt"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	moves := readLines("input.txt")

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
					tail[0] += sign(rowDifference)
					tail[1] += sign(colDifference)
					if j == len(knots)-1 {
						TailVisits[fmt.Sprintf("(%v,%v)", tail[0], tail[1])] = true
					}
				}
			}
		}
	}

	return len(TailVisits)
}

func sign(n int) int {
	if n == 0 {
		return 0
	}
	if n < 0 {
		return -1
	}
	return 1
}

func PartOne(moves [][]string) int {
	Head := []int{5, 1}
	Tail := []int{5, 1}

	TailVisits := map[string]int{"(5,1)": 1}

	for _, move := range moves {
		direction, amount := move[0], utils.Atoi(move[1])
		for i := 1; i <= amount; i++ {
			Head[0], Head[1] = directions(direction)[0]+Head[0], directions(direction)[1]+Head[1]
			if utils.Abs(Head[0]-Tail[0]) > 1 || utils.Abs(Head[1]-Tail[1]) > 1 {
				if Head[0] != Tail[0] && Head[1] != Tail[1] {
					if Head[1] > Tail[1] {
						if Head[0] < Tail[0] {
							// move top-right
							Tail[0], Tail[1] = Tail[0]+directions("UR")[0], Tail[1]+directions("UR")[1]
						} else {
							// move bottom-right
							Tail[0], Tail[1] = Tail[0]+directions("DR")[0], Tail[1]+directions("DR")[1]
						}
					} else {
						if Head[0] < Tail[0] {
							// move top-left
							Tail[0], Tail[1] = Tail[0]+directions("UL")[0], Tail[1]+directions("UL")[1]
						} else {
							// move bottom-left
							Tail[0], Tail[1] = Tail[0]+directions("DL")[0], Tail[1]+directions("DL")[1]
						}
					}
				} else {
					// move behind head either same row or column
					Tail[0], Tail[1] = directions(direction)[0]+Tail[0], directions(direction)[1]+Tail[1]
				}
				TailVisits[fmt.Sprintf("(%v,%v)", Tail[0], Tail[1])] += 1
			}
		}
	}
	return len(TailVisits)
}

func directions(name string) []int {
	directions := map[string][]int{
		"R": {0, 1}, "L": {0, -1},
		"U": {-1, 0}, "D": {1, 0},
		"UR": {-1, 1}, "UL": {-1, -1},
		"DR": {1, 1}, "DL": {1, -1},
	}
	return directions[name]
}

func readLines(name string) [][]string {
	lines := strings.Split(utils.ReadFile(name), "\n")
	moves := [][]string{}

	for _, line := range lines {
		moves = append(moves, strings.Fields(line))
	}

	return moves
}
