package main

import (
	"fmt"
	"strings"

	"github.com/papayankey/utils"
)

func main() {
	data := readLines("input.txt")

	result := PartOne(data)
	fmt.Println(result)

	result = PartTwo(data)
	fmt.Println(result)
}

func PartTwo(nums []int) int {
	var res int

outer:
	for i := 0; i < len(nums); i++ {
		for j := i + 1; j < len(nums); j++ {
			for k := j + 1; k < len(nums); k++ {
				if nums[i]+nums[j]+nums[k] == 2020 {
					res = nums[i] * nums[j] * nums[k]
					break outer
				}
			}
		}
	}
	return res
}

func PartOne(nums []int) int {
	var out int
	seen := map[int]int{}
	for _, n := range nums {
		if _, ok := seen[n]; ok {
			out = n * seen[n]
			break
		}
		seen[2020-n] = n
	}
	return out
}

func readLines(name string) []int {
	lines := strings.Split(utils.ReadFile(name), "\r\n")
	nums := []int{}

	for _, line := range lines {
		nums = append(nums, utils.Atoi(line))
	}

	return nums
}
