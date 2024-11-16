package main

import (
	"fmt"

	"github.com/papayankey/utils"
)

func main() {
	utils.AoC(2022, 6)

	result := PartOne("day06/input.txt")
	fmt.Println(result)

	result = PartTwo("day06/input.txt")
	fmt.Println(result)
}

func PartTwo(name string) int {
	s := utils.ReadFile(name)

	set := map[byte]bool{}
	l, r := 0, 0

	for r < len(s) {
		if !set[s[r]] {
			set[s[r]] = true
			if len(set) == 14 {
				break
			}
			r += 1
		} else {
			delete(set, s[l])
			l += 1
		}
	}

	return r + 1
}

func PartOne(name string) int {
	s := utils.ReadFile(name)

	l, r := 0, 3

	for r < len(s) {
		if s[l] != s[l+1] && s[l] != s[l+2] && s[l] != s[r] &&
			s[l+1] != s[l+2] && s[l+1] != s[r] && s[l+2] != s[r] {
			break
		}
		l += 1
		r += 1
	}

	return r + 1
}
