package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

type BingoChecker map[int]map[string][]int

func main() {
	score := partOne()
	fmt.Println("First win score:", score)

	score = partTwo()
	fmt.Println("Last win score:", score)

}

func partTwo() int {
	randNums, boards := readLines("input.txt")
	bingoChecker := generateBingoChecker(boards)
	return getBingoScore(boards, randNums, bingoChecker, false)
}

func partOne() int {
	randNums, boards := readLines("input.txt")
	bingos := generateBingoChecker(boards)
	return getBingoScore(boards, randNums, bingos, true)
}

func getBingoScore(boards [][][]string, randNums []string, bingoChecker BingoChecker, checkFirstWin bool) int {
	completionOrder := []int{}
	bingoBoard := -1
	lastRandNum := -1
	for _, num := range randNums {
		isBingo := false
		for currBoard, board := range boards {
			for row := 0; row < 5; row++ {
				isMarked := false
				for col := 0; col < 5; col++ {
					if board[row][col] == num {
						board[row][col] = "X"
						updateBingo(bingoChecker, currBoard, row, col)
						if board, ok := checkBingo(bingoChecker, completionOrder); ok {
							isBingo = true
							num, _ := strconv.Atoi(num)
							lastRandNum = num
							completionOrder = append(completionOrder, board)
						}
						isMarked = true
						break
					}
				}
				if isMarked {
					break
				}
			}
			if checkFirstWin && isBingo {
				break
			}
		}
		// choose the first bingo board
		if checkFirstWin && isBingo {
			bingoBoard = completionOrder[0]
			break
		}
		// choose the last completed board
		// when all boards are bingo!
		if len(completionOrder) == len(boards) {
			bingoBoard = completionOrder[len(completionOrder)-1]
			break
		}
	}

	return calculateScore(boards[bingoBoard], lastRandNum)
}

func calculateScore(board [][]string, lastRandNum int) int {
	unmarkedSum := 0
	for _, row := range board {
		for _, v := range row {
			if v != "X" {
				unmarkValue, _ := strconv.Atoi(v)
				unmarkedSum += unmarkValue
			}
		}
	}
	return unmarkedSum * lastRandNum
}

func checkBingo(bingos BingoChecker, completedBoards []int) (int, bool) {
	isBingo := false
	bingoBoard := -1
	for board, bingo := range bingos {
		// skip completed boards
		if contains(completedBoards, board) {
			continue
		}
		for _, values := range bingo {
			for _, v := range values {
				if v == 5 {
					isBingo = true
					bingoBoard = board
					break
				}
			}
			if isBingo {
				break
			}
		}
		if isBingo {
			break
		}
	}
	return bingoBoard, isBingo
}

func generateBingoChecker(boards [][][]string) BingoChecker {
	bingos := BingoChecker{}
	for i := 0; i < len(boards); i++ {
		bingos[i] = map[string][]int{
			"rows": {0, 0, 0, 0, 0},
			"cols": {0, 0, 0, 0, 0},
		}
	}
	return bingos
}

func updateBingo(bingo map[int]map[string][]int, currBoard, row, col int) {
	bingo[currBoard]["rows"][row] += 1
	bingo[currBoard]["cols"][col] += 1
}

func contains(s []int, v int) bool {
	for _, item := range s {
		if item == v {
			return true
		}
	}
	return false
}

func readLines(fileName string) ([]string, [][][]string) {
	f, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()
	scanner := bufio.NewScanner(f)

	scanner.Scan()
	randNums := strings.Split(scanner.Text(), ",")

	boards := [][][]string{}
	for scanner.Scan() {
		board := [][]string{}
		for i := 0; i < 5; i++ {
			scanner.Scan()
			line := strings.Fields(scanner.Text())
			board = append(board, line)
		}
		boards = append(boards, board)
	}

	return randNums, boards
}
