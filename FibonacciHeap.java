/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */

public class FibonacciHeap {
	private static int totalLinks = 0;
	private static int totalCuts = 0;
	private int size = 0;
	private int markedNum = 0;
	private int treeNum = 0;
	private HeapNode last;
	private HeapNode first;
	private HeapNode min;
	
	/*
	 * Time Complexity: O(1)
	 */
	public FibonacciHeap() {last = min = first = null;}
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    * 
    * Time Complexity: O(1)
    */
    public boolean isEmpty() {return last == null;}
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    * 
    * Time Complexity: O(1)
    */
    public HeapNode insert(int key) { 
    	size++;
    	treeNum++;
    	HeapNode node = new HeapNode(key);
    	if (isEmpty()) {last = first = min = node;}
		first.setLeftAndRight(node);
		last.setRightAndLeft(node);
		first = node;
		min = min.getKey() < key ? min : node;
    	return node;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    * Time Complexity: O(log n), amortized
    */
    public void deleteMin() {
    	if (size() <= 1) {
    		min = last = first = null;
    		size = 0;
    		treeNum = 0;
    	}
    	else {
    		size--;
    		treeNum += min.getRank() - 1;
    		HeapNode leftNode = min.getLeft();
    		HeapNode rightNode = min.getRight();
    		HeapNode leftChild = min.getChild();
    		if (leftChild == null) {
    			if (last.getKey() == min.getKey()) {last = min.getLeft();}
    			if (first.getKey() == min.getKey()) {first = min.getRight();}
    			leftNode.setRightAndLeft(rightNode);
    		}
    		else {
    			HeapNode rightChild = leftChild.getLeft();
    			if (min.getKey() == last.getKey()) {last = rightChild;}
    			if (min.getKey() == first.getKey()) {first = leftChild;}
    			if (leftNode.getKey() != min.getKey() && min.getKey() != rightNode.getKey()) {
    				leftNode.setRightAndLeft(leftChild);
            		rightNode.setLeftAndRight(rightChild);
    			}
    		}
    		updateMinAndConsolidate();
    	}
     	return ;
    }
    /**
     * public void findNewMin()
     * 
     * updates the new minimum after deleting the current.
     * makes full consolidation.
     * 
     * Time Complexity: O(log n), amortized
     */
    public void updateMinAndConsolidate() {
    	HeapNode[] rankTrees = new HeapNode[(int) Math.floor(
    			1 + (Math.log(size()) / Math.log((1 + Math.sqrt(5)) / 2)))];
    	HeapNode curNode = first;
    	HeapNode nextNode;
    	HeapNode sameRankNode;
    	for (int i = 0; i < treeNum; i++) {
    		if (curNode.isSigned()) {
    			curNode.unsign();
    			markedNum--;
    		}
    		curNode.setParent(null);
    		nextNode = curNode.getRight();
    		sameRankNode = rankTrees[curNode.getRank()];
    		while (sameRankNode != null) {
    			rankTrees[curNode.getRank()] = null;
    			curNode = combineTwoTrees(curNode, sameRankNode);
    			sameRankNode = rankTrees[curNode.getRank()];
    		}
    		rankTrees[curNode.getRank()] = curNode;
    		curNode = nextNode;
    	}
    	updateHeapAccordingToTheArray(rankTrees);
    	return ;
    }
    
    /**
     * public HeapNode combineTwoTrees(t1, t2)
     * combine two trees with the same degree.
     * @type smaller, bigger - HeapNode. 
     * @param smaller, bigger - the roots of those trees.
     * @return the root of the combined tree. (HeapNode)
     * 
     * Time Complexity: O(1)
     */
    public HeapNode combineTwoTrees(HeapNode smaller,HeapNode bigger) {
    	if (smaller.getKey() > bigger.getKey()) {return combineTwoTrees(bigger, smaller);}
    	totalLinks++;
    	HeapNode smallerLeftChild = smaller.getChild();
    	smaller.setChild(bigger);
    	bigger.setParent(smaller);
    	smaller.incRank();
    	if (smallerLeftChild != null) {
    		HeapNode smallerRightChild = smallerLeftChild.getLeft();
        	bigger.setLeftAndRight(smallerRightChild);
        	bigger.setRightAndLeft(smallerLeftChild);
    	}
    	else {bigger.setLeftAndRight(bigger);}
    	smaller.setLeftAndRight(smaller);
    	return smaller;
    }
    
    /**
     * public void updateHeapAccordingToTheArray(HeapNode[] rankArray)
     * gets the heap and the ranked tree array, and updates the heap
     * according to the array (including updating the minimum).
     * @param rankArray - the array from the findNewMin() process.
     * 
     * Time Complexity: O(log n)
     */
    public void updateHeapAccordingToTheArray(HeapNode[] rankArray) {
    	HeapNode curNode = min = last = first = null;
    	treeNum = 0;
    	for (HeapNode node: rankArray) {
    		if (node != null) {
    			treeNum++;
	    		if (first == null) {min = first = node;}
	    		else {
	    			curNode.setRightAndLeft(node);
	    			min = min.getKey() < node.getKey() ? min : node;
	    		}
    			last = curNode = node;
    		}
    	}
    	first.setLeftAndRight(last);
    	return;
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    * 
    * Time Complexity: O(1)
    */
    public HeapNode findMin() {return this.min;} 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    * 
    * Time Complexity: O(1)
    */
    public void meld (FibonacciHeap heap2) {
    	this.size += heap2.size();
    	this.treeNum += heap2.treeNum();
    	this.markedNum += heap2.markedNum();
    	if (this.isEmpty()) {
    		this.last = heap2.last;
    		this.first = heap2.first;
    		this.min = heap2.min;
    	}
    	else if (!heap2.isEmpty()) {
	    	first.setLeftAndRight(heap2.last);
	    	last.setRightAndLeft(heap2.first);
	    	last = heap2.last;
	    	this.min = this.min.getKey() < heap2.min.getKey() ? this.min : heap2.min;
    	}
    	return ;  		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    * 
    * Time Complexity: O(1)
    */
    public int size() {return this.size;}
    
    /**
     * public int treeNum()
     *
     * Returns the number of elements in the heap.
     * 
     * Time Complexity: O(1)
     */
     public int treeNum() {return this.treeNum;}
     
     /**
      * public int markedNum()
      *
      * Returns the number of elements in the heap.
      * 
      * Time Complexity: O(1)
      */
      public int markedNum() {return this.markedNum;}
      
    /**
    * public int[] `Rep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    * Time Complexity: O(log n)
    */
    public int[] countersRep() {
    	if (this.isEmpty()) {return new int[0];}
    	int[] arr = new int[(int) Math.floor(1 + (Math.log(size()) / Math.log((1 + Math.sqrt(5)) / 2)))];
    	int maxRank = 0;
    	HeapNode curNode = this.first;
    	for (int i = 0; i < this.treeNum ; i++) {
    		int curRank = curNode.getRank();
    		arr[curRank] += 1;
    		maxRank = curRank > maxRank ? curRank : maxRank;
    		curNode = curNode.getRight();
    	}
    	int[] finalArr = new int[maxRank + 1];
    	for (int j = 0; j < maxRank + 1; j++) {finalArr[j] = arr[j];}
        return finalArr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    * 
    * Time complexity: O(log n), amortized
    */
    public void delete(HeapNode x) {
    	if (min.getKey() == Integer.MIN_VALUE) {
    		if (x.getParent() != null) {cascadingCut(x);}
    		min = x;
    	}
    	else {decreaseKey(x, x.getKey() - min.getKey() + 1);}
    	deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * 
    * Time Complexity: O(1), amortized
    */
    public void decreaseKey(HeapNode x, int delta) {
    	x.setKey(x.getKey() - delta);
    	HeapNode parent = x.getParent();
    	if (parent == null) {min = min.getKey() < x.getKey() ? min : x;}
    	else if (x.getKey() < parent.getKey()) {cascadingCut(x);}
    	return ;
    }
    
    /*
     * Time Complexity: O(1), amortized
     */
    public void cascadingCut(HeapNode node) {
    	boolean repeat;
    	do {
    		HeapNode parent = node.getParent();
    		repeat = cut(node);
    		node = parent;
    	} while (repeat);
    }
    
    /**
     * @param node
     * Time Complexity: O(1)
     */
    public boolean cut(HeapNode node) {
    	totalCuts++;
    	HeapNode parent = node.getParent();
    	Boolean repeat = parent.isSigned();  //checks if we need to do it again.
    	if (!repeat && parent.getParent() != null) { 
    		parent.sign();
    		this.markedNum++;
    	}
    	parent.decRank();
    	if (parent.getRank() > 0) {
    		//Checks if the node was only child.
    		HeapNode right = node.getRight();
        	if (parent.getChild() == node) {parent.setChild(right);}
        	right.setLeftAndRight(node.getLeft());
    	}
    	else {parent.setChild(null);}
    	//Puts the node in the left of the tree list.
    	treeNum++;
    	first.setLeftAndRight(node);
    	last.setRightAndLeft(node);
    	node.setParent(null);
    	if (node.isSigned()) {  //Makes sure the the node is unsigned as a root.
    		markedNum--;
    		node.unsign();
    	}
    	first = node;
    	this.min = this.min.getKey() < node.getKey() ? this.min : node;  //Updates minimum.
    	return repeat;
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    * 
    * Time Complexity: O(1)
    */
    public int nonMarked() {return size - markedNum;}

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap.
    * 
    * Time Complexity: O(1)
    */
    public int potential() {return treeNum + 2 * markedNum;}

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    * 
    * Time Complexity: O(1)
    */
    public static int totalLinks() {return totalLinks;}

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods).
    * 
    * Time Complexity: O(1)
    */
    public static int totalCuts() {return totalCuts;}

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H.
    * 
    * Time Complexity: O(k * deg(H))
    */
    public static int[] kMin(FibonacciHeap H, int k) {
    	if (k == 0 || H.isEmpty()) {return new int[0];} //checks for edge cases
    	int[] arr = new int[k];
    	FibonacciHeap meanSus = new FibonacciHeap();
    	HeapNode curOrigin = H.min;
    	HeapNode curNew = meanSus.insert(curOrigin.getKey());
    	curNew.setOtherHeap(H.min);
    	for (int i = 0; i < k; i++) {
    		//Finds the minimum key from the helper heap.
    		curNew = meanSus.findMin();
    		curOrigin = curNew.getOtherHeap();
    		arr[i] = curNew.getKey();
    		curOrigin = curOrigin.getChild();
    		//Adds the children of the deleted node to the helper heap.
    		if (curOrigin != null) {
    			int stop = curOrigin.getKey();
    			do {
    				curNew = meanSus.insert(curOrigin.getKey());
    				curNew.setOtherHeap(curOrigin);
    				curOrigin = curOrigin.getRight();
    			} while(curOrigin.getKey() != stop);
    		}
    		meanSus.deleteMin();
    	}
    	return arr;
    }
    
    public HeapNode getFirst() {return first;}
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode {

    	public int key;
    	private int rank;
    	private boolean sign;
    	private HeapNode left;
    	private HeapNode right;
    	private HeapNode child;
    	private HeapNode parent;
    	private HeapNode otherHeap;

    	public HeapNode(int key) {
    		this.key = key;
    		this.rank = 0;
    		sign = false;
    		right = left = this;
    		child = parent = otherHeap = null;
    	}

    	public int getKey() {return this.key;}
    	
    	public int getRank() {return this.rank;}
    	
    	public HeapNode getLeft() {return left;}
    	
    	public HeapNode getRight() {return right;}
    	
    	public HeapNode getChild() {return child;}
    	
    	public HeapNode getParent() {return parent;}
    	
    	public HeapNode getOtherHeap() {return otherHeap;}
    	
    	public boolean isSigned() {return sign;}
    	
    	public void setLeft(HeapNode left) {this.left = left;}
    	
    	public void setRight(HeapNode right) {this.right = right;}
    	
    	public void setLeftAndRight(HeapNode left) {
    		this.left = left;
    		left.setRight(this);
    	}
    	
    	public void setRightAndLeft(HeapNode right) {
    		this.right = right;
    		right.setLeft(this);
    	}
    	
    	public void setChild(HeapNode child) {this.child = child;}
    	
    	public void setParent(HeapNode parent) {this.parent = parent;}
    	
    	public void setOtherHeap(HeapNode otherHeap) {this.otherHeap = otherHeap;}
    	
    	public void setKey(int key) {this.key = key;}
    	
    	public void incRank() {this.rank++;}
    	
    	public void decRank() {this.rank--;}
    	
    	public void sign() {sign = true;}
    	
    	public void unsign() {sign = false;}
    }
}
