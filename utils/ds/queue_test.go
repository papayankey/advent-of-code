package ds

import (
	"testing"
)

func TestNewQueue(t *testing.T) {
	d := []int{1, 2, 3, 4}
	q := NewQueue(d...)

	got := q.Len
	want := 4

	if got != want {
		t.Errorf("NewQueue(%v) = %v but want %v", d, got, want)
	}
}

func TestEnqueue(t *testing.T) {
	q := NewQueue(1, 2)
	q.Add(3)

	got := q.Data[0]
	want := 1

	if got != want {
		t.Errorf("Enqueue(%v) = %v but want %v", 3, got, want)
	}
}

func TestDequeue(t *testing.T) {
	d := []int{1, 2}
	q := NewQueue(d...)

	got := q.Remove()
	want := 1

	if got != want {
		t.Errorf("Dequeue() = %v but want %v", got, want)
	}

	got = q.Remove()
	want = 2

	if got != want {
		t.Errorf("Dequeue() = %v but want %v", got, want)
	}

	got = q.Len
	want = 0

	if got != want {
		t.Errorf("Dequeue() = %v but want %v", got, want)
	}
}
