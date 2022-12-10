package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"

	"github.com/papayankey/utils"
)

type Passwords map[string]string
type Policies map[string][]int

func main() {
	result := getValidPasswords("input.txt", PartOne)
	fmt.Println(result)

	result = getValidPasswords("input.txt", PartTwo)
	fmt.Println(result)

}

func PartTwo(pol []int, l, ps string) int {
	if string(ps[pol[0]-1]) == l && string(ps[pol[1]-1]) != l ||
		string(ps[pol[0]-1]) != l && string(ps[pol[1]-1]) == l {
		return 1
	}
	return 0
}

func PartOne(pol []int, letter, password string) int {
	c := strings.Count(password, letter)
	if c >= pol[0] && c <= pol[1] {
		return 1
	}
	return 0
}

func getValidPasswords(name string, fn func([]int, string, string) int) int {
	f, err := os.Open(name)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()

	scanner := bufio.NewScanner(f)

	count := 0

	for scanner.Scan() {
		parts := strings.Split(scanner.Text(), ":")
		left := strings.Fields(parts[0])

		password := strings.TrimSpace(parts[1])
		policy := []int{}
		temp := strings.Split(left[0], "-")

		for _, v := range temp {
			policy = append(policy, utils.Atoi(v))
		}

		count += fn(policy, left[1], password)
	}

	return count
}
