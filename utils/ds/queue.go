package ds

type Queue[T any] struct {
	Data []T
	Len  int
}

// NewQueue creates and returns a new queue
func NewQueue[T any](args ...T) *Queue[T] {
	q := &Queue[T]{Data: []T{}, Len: 0}
	q.Data = append(q.Data, args...)
	q.Len += len(args)
	return q
}

// Enqueue adds elem(s) to queue data
func (q *Queue[T]) Add(args ...T) *Queue[T] {
	q.Data = append(q.Data, args...)
	q.Len += len(args)
	return q
}

// Dequeue removes elem(s) from queue data
func (q *Queue[T]) Remove() T {
	elem := q.Data[0]
	q.Data = q.Data[1:]
	q.Len -= 1
	return elem
}

// IsEmpty checks if queue size is 0
func (q *Queue[T]) IsEmpty() bool {
	return q.Len == 0
}

// Peek returns element in front of the queue
func (q *Queue[T]) Peek() T {
	return q.Data[0]
}

func (q *Queue[T]) Clear() {
	q.Data = q.Data[:0]
	q.Len = 0
}
