package utils

import (
	"log"
	"os"
	"strconv"
	"strings"

	"golang.org/x/exp/constraints"
)

type Number interface {
	~int | ~int8 | ~int16 | ~int32 | ~int64 | ~float32 | ~float64
}

// Atoi converts a string to an integer
func Atoi(s string) int {
	v, err := strconv.Atoi(s)
	CheckErr(err)
	return v
}

// Itoa converts an integer to a string
func Itoa(x int) string {
	return strconv.Itoa(x)
}

// Min returns the lesser between x and y
func Min[T constraints.Ordered](x, y T) T {
	if x < y {
		return x
	}
	return y
}

// Max returns the greater between x and y
func Max[T constraints.Ordered](x, y T) T {
	if x > y {
		return x
	}
	return y
}

// ReverseString changes the order of runes of a string
// so that the last rune becomes first and so on
func ReverseString(s string) string {
	b := []byte(s)
	for i := 0; i < len(s)/2; i++ {
		j := len(b) - 1 - i
		b[i], b[j] = b[j], b[i]
	}
	return string(b)
}

// CheckErr checks and panic if an error is not nil
func CheckErr(e error) {
	if e != nil {
		log.Fatal(e)
	}
}

// ReadFile reads file content into memory as a string
func ReadFile(name string) string {
	b, err := os.ReadFile(name)
	CheckErr(err)
	return string(b)
}

// Lines split a string by "\n"
func Lines(name string) []string {
	return strings.Split(strings.TrimSpace(name), "\n")
}

// Abs returns the absolute value of a removing
// negative if present
func Abs[T Number](a T) T {
	if a < 0 {
		return -a
	}
	return a
}

// Euclidean algorithm
//		Given a and b, replace a with remainder of a%b until b == 0
//		Example:
// 		GCD(12, 6) -> GCD(6, 12 mod 6) -> GCD(6, 0) -> 6
// GCD returns the largest positive integer which divides a and b
func GCD(a, b int) int {
	a, b = Abs(a), Abs(b)
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

// LCM returns the smallest positive integer that
// is divisible by nums
func LCM(nums ...int) int {
	r := (nums[0] * nums[1]) / GCD(nums[0], nums[1])
	for _, v := range nums[2:] {
		r = LCM(r, v)
	}
	return r
}

// Transpose
func Transpose[T constraints.Ordered](d [][]T) [][]T {
	o := [][]T{}
	cols := len(d[0])
	rows := len(d)
	for c := 0; c < cols; c++ {
		t := []T{}
		for r := 0; r < rows; r++ {
			t = append(t, d[r][c])
		}
		o = append(o, t)
	}
	return o
}

// ReverseString changes the order of members of a slice
// so that the last member becomes first and so on
func ReverseSlice[T any](s []T) []T {
	c := make([]T, len(s))
	copy(c, s)
	for i := 0; i < len(c)/2; i++ {
		j := len(c) - 1 - i
		c[i], c[j] = c[j], c[i]
	}
	return c
}

// Replace is a convenient util for replacing parts of a string
func Replace(s string, oldnew ...string) string {
	return strings.NewReplacer(oldnew...).Replace(s)
}

func Map[T any, U any](s []T, fn func(T) U) []U {
	var m []U
	for _, v := range s {
		m = append(m, fn(v))
	}
	return m
}

func Filter[T any](s []T, fn func(T) bool) []T {
	var m []T
	for _, v := range s {
		if fn(v) {
			m = append(m, v)
		}
	}
	return m
}

func MapKeys[T constraints.Ordered, U any](m map[T]U) []T {
	var keys []T
	for k := range m {
		keys = append(keys, k)
	}
	return keys
}

func MapValues[T constraints.Ordered, U any](m map[T]U) []U {
	var values []U
	for _, v := range m {
		values = append(values, v)
	}
	return values
}
