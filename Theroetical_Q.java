import java.lang.Math;

public class Theroetical_Q {
	public static void insertTimes(FibonacciHeap heap, long times) {
		for (int i = 0; i < times; i++) {heap.insert(i);}
	}
	
	public static void deleteMinTimes(FibonacciHeap heap, long times) {
		for (int i = 0; i < times; i++) {heap.deleteMin();}
	}
	 
	public static void main(String[] args) {
		long insertions = 6561;
		long deletions = (long) ((3 * (insertions - 1)) / 4);
		FibonacciHeap heap = new FibonacciHeap();
		long startTime = System.nanoTime();
		insertTimes(heap, insertions);
		deleteMinTimes(heap, deletions);
		long endTime = System.nanoTime();
		long totTime = endTime - startTime;
		System.out.println(totTime + ", " + FibonacciHeap.totalLinks(
				) + ", "+ FibonacciHeap.totalCuts() + ", " + heap.potential());
	}
}
