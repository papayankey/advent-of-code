package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
)

func main() {
	data := readLines("input.txt")

	count := partOne(data)
	fmt.Println("day01:", count)

	count = partTwo(data)
	fmt.Println("day02:", count)
}

func partTwo(data []int) int {
	count, start := 0, 0
	end := 2
	prev := sum(data, start, end)
	for end < len(data)-1 {
		start++
		end++
		currSum := sum(data, start, end)
		if currSum > prev {
			count++
		}
		prev = currSum
	}
	return count
}

func sum(d []int, start, end int) int {
	return d[start] + d[start+1] + d[end]
}

func partOne(data []int) int {
	count := 0
	for i := 1; i < len(data); i++ {
		if data[i] > data[i-1] {
			count++
		}
	}
	return count
}

func readLines(fileName string) []int {
	f, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()
	scanner := bufio.NewScanner(f)
	data := []int{}
	for scanner.Scan() {
		v, _ := strconv.Atoi(scanner.Text())
		data = append(data, v)
	}
	return data
}
