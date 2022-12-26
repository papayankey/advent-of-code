package ds

import (
	"reflect"
	"testing"
)

func TestIntersection(t *testing.T) {
	a := NewSet(2, 3, 1, 4)
	b := NewSet(1, 2, 5)

	got := a.Intersection(b)
	want := NewSet(1, 2)

	if !reflect.DeepEqual(got, want) {
		t.Errorf("Intersection(%v) = %v but want %v", b, got, want)
	}
}

func TestDifference(t *testing.T) {
	a := NewSet(2, 3, 1, 4)
	b := NewSet(1, 2, 5)

	got := a.Difference(b)
	want := NewSet(3, 4, 5)

	if !reflect.DeepEqual(got, want) {
		t.Errorf("Difference(%v) = %v but want %v", b, got, want)
	}
}

func TestUnion(t *testing.T) {
	a := NewSet(2, 3, 1, 4)
	b := NewSet(1, 2, 5)

	got := a.Union(b)
	want := NewSet(1, 2, 3, 4, 5)

	if !reflect.DeepEqual(got, want) {
		t.Errorf("Union[%v]() = %v but want %v", b, got, want)
	}
}
