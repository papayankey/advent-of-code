package utils

import (
	"log"
	"os"
	"strconv"
	"strings"

	"golang.org/x/exp/constraints"
)

type Ordered constraints.Ordered

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
func Min[T Ordered](x, y T) T {
	if x < y {
		return x
	}
	return y
}

// Max returns the greater between x and y
func Max[T Ordered](x, y T) T {
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
	return strings.Split(strings.TrimSpace(name), "\r")
}

// Set represents a set data structure
type set[T Ordered] map[T]bool

// Set creates and returns a new set
func Set[T Ordered](s []T) set[T] {
	m := set[T]{}
	for _, v := range s {
		m[v] = true
	}
	return m
}

// Difference returns a slice of unique member(s) of set a and b
func Difference[T Ordered](a, b set[T]) []T {
	out := []T{}
	for k := range a {
		if !b[k] {
			out = append(out, k)
		}
	}
	return out
}

// Intersection returns a slice of common member(s) of set a and b
func Intersection[T Ordered](a, b set[T]) []T {
	out := []T{}
	for k := range a {
		if b[k] {
			out = append(out, k)
		}
	}
	return out
}

// Union returns a set of all members of setA and setB
func Union[T Ordered](a, b set[T]) []T {
	s := set[T]{}
	for i := range a {
		s[i] = true
	}
	for j := range b {
		s[j] = true
	}
	out := []T{}
	for k := range s {
		out = append(out, k)
	}
	return out
}

// Abs returns the absolute value of a removing
// negative if present
func Abs[T Number](a T) T {
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
func Transpose[T Ordered](d [][]T) [][]T {
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

// Replace is a convenient util for replacing content of a string
func Replace(s string, oldnew ...string) string {
	return strings.NewReplacer(oldnew...).Replace(s)
}
