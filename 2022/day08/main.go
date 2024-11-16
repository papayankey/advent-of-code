package main

import (
	"fmt"
	"log"
	"math"
	"os"
	"strconv"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 8)

	data := readLines("day08/input.txt")

	result := PartOne(data)
	fmt.Println(result)

	result = PartTwo(data)
	fmt.Println(result)
}

func PartTwo(data [][]int) int {
	sum := math.MinInt

	for row := 1; row < len(data)-1; row++ {
		for col := 1; col < len(data[0])-1; col++ {
			// Up
			t := 0
			for r := row - 1; r >= 0; r-- {
				t += 1
				if data[row][col] <= data[r][col] {
					break
				}
			}
			// Down
			d := 0
			for r := row + 1; r < len(data); r++ {
				d += 1
				if data[row][col] <= data[r][col] {
					break
				}
			}
			// Left
			l := 0
			for c := col - 1; c >= 0; c-- {
				l += 1
				if data[row][col] <= data[row][c] {
					break
				}
			}
			// Right
			r := 0
			for c := col + 1; c < len(data[0]); c++ {
				r += 1
				if data[row][col] <= data[row][c] {
					break
				}
			}

			total := t * d * l * r
			if total > sum {
				sum = total
			}
		}
	}

	return sum
}

func PartOne(data [][]int) int {
	sum := 0

	for row := 0; row < len(data); row++ {
		for col := 0; col < len(data[0]); col++ {
			// Up
			visible := true
			for r := row - 1; r >= 0; r-- {
				if data[row][col] <= data[r][col] {
					visible = false
					break
				}
			}
			if visible {
				sum += 1
			} else {
				// Down
				visible = true
				for r := row + 1; r < len(data); r++ {
					if data[row][col] <= data[r][col] {
						visible = false
						break
					}
				}
				if visible {
					sum += 1
				} else {
					// Left
					visible = true
					for c := col - 1; c >= 0; c-- {
						if data[row][col] <= data[row][c] {
							visible = false
							break
						}
					}
					if visible {
						sum += 1
					} else {
						// Right
						visible = true
						for c := col + 1; c < len(data[0]); c++ {
							if data[row][col] <= data[row][c] {
								visible = false
								break
							}
						}
						if visible {
							sum += 1
						}
					}
				}
			}
		}
	}

	return sum
}

func readLines(name string) [][]int {
	b, err := os.ReadFile(name)
	if err != nil {
		log.Fatal(err)
	}
	lines := strings.Split(string(b), "\n")
	data := [][]int{}
	for _, line := range lines {
		t := []int{}
		for _, v := range strings.Split(line, "") {
			x, _ := strconv.Atoi(v)
			t = append(t, x)
		}
		data = append(data, t)
	}
	return data
}
