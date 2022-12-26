package ds

import (
	"golang.org/x/exp/constraints"
)

// Set represents a collection of elements with no duplicates
type Set[T constraints.Ordered] struct {
	Data map[T]bool
	Len  int
}

// Set creates and returns a new set
func NewSet[T constraints.Ordered](args ...T) *Set[T] {
	s := &Set[T]{Data: make(map[T]bool), Len: 0}
	for _, v := range args {
		s.Data[v] = true
	}
	s.Len += len(args)
	return s
}

// Add inserts elem(s) into the set
func (s *Set[T]) Add(args ...T) {
	for _, v := range args {
		s.Data[v] = true
	}
	s.Len += len(args)
}

// Remove deletes an elem from the set
func (s *Set[T]) Remove(key T) bool {
	delete(s.Data, key)
	_, ok := s.Data[key]
	if ok {
		s.Len -= 1
	}
	return ok
}

// Clear removes all elem(s) from the set
func (s *Set[T]) Clear() {
	s.Data = map[T]bool{}
	s.Len = 0
}

// Difference returns a slice of unique member(s) of set a and b
func (s *Set[T]) Difference(b *Set[T]) *Set[T] {
	out := NewSet[T]()
	for i := range b.Data {
		if !s.Data[i] {
			out.Data[i] = true
			out.Len += 1
		}
	}
	for j := range s.Data {
		if !b.Data[j] {
			out.Data[j] = true
			out.Len += 1
		}
	}
	return out
}

// Intersection returns a slice of common member(s) of set a and b
func (s *Set[T]) Intersection(b *Set[T]) *Set[T] {
	out := NewSet[T]()
	for k := range b.Data {
		if s.Data[k] {
			out.Data[k] = true
			out.Len += 1
		}
	}
	return out
}

// Union returns a set of all members of setA and setB
func (s *Set[T]) Union(b *Set[T]) *Set[T] {
	out := NewSet[T]()
	for k, v := range s.Data {
		out.Data[k] = v
	}
	for k, v := range b.Data {
		out.Data[k] = v
	}
	out.Len = len(out.Data)
	return out
}
