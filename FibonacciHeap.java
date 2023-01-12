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
	private HeapNode first;
	private HeapNode last;
	private HeapNode min;
	
	public FibonacciHeap() {first = min = last = null;}
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() {return first == null;}
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key) { 
    	size++;
    	treeNum++;
    	HeapNode node = new HeapNode(key);
    	if (isEmpty()) {first = last = min = node;}
		last.setLeftAndRight(node);
		first.setRightAndLeft(node);
		last = node;
		min = min.getKey() < key ? min : node;
    	return node;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin() {
    	if (isEmpty()) {System.out.println("can't delete from an empty list.");}
    	else if (size() == 1) {
    		min = first = last = null;
    		size = 0;
    	}
    	else {
    		size--;
    		treeNum += min.getRank() - 1;
    		HeapNode leftNode = min.getLeft();
    		HeapNode rightNode = min.getRight();
    		HeapNode leftChild = min.getChild();
    		if (leftChild == null) {
    			if (first.getKey() == min.getKey()) {first = min.getLeft();}
    			if (last.getKey() == min.getKey()) {last = min.getRight();}
    			leftNode.setRightAndLeft(rightNode);
    		}
    		else {
    			HeapNode rightChild = leftChild.getLeft();
    			if (min.getKey() == first.getKey()) {first = rightChild;}
    			if (min.getKey() == last.getKey()) {last = leftChild;}
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
    		nextNode = curNode.getLeft();
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
     */
    public HeapNode combineTwoTrees(HeapNode smaller,HeapNode bigger) {
    	if (smaller.getKey() > bigger.getKey()) {return combineTwoTrees(bigger, smaller);}
    	totalLinks = totalLinks + 1;
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
     */
    public void updateHeapAccordingToTheArray(HeapNode[] rankArray) {
    	HeapNode curNode = min = first = last = null;
    	treeNum = 0;
    	for (HeapNode node: rankArray) {
    		if (node != null) {
    			treeNum++;
	    		if (last == null) {min = last = node;}
	    		else {
	    			curNode.setRightAndLeft(node);
	    			min = min.getKey() < node.getKey() ? min : node;
	    		}
    			first = curNode = node;
    		}
    	}
    	last.setLeftAndRight(first);
    	return;
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() {return this.min;} 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2) {
    	this.size += heap2.size();
    	this.treeNum += heap2.treeNum();
    	this.markedNum += heap2.markedNum();
    	last.setLeftAndRight(heap2.first);
    	first.setRightAndLeft(heap2.last);
    	return ;  		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size() {return this.size;}
    
    /**
     * public int treeNum()
     *
     * Returns the number of elements in the heap.
     *   
     */
     public int treeNum() {return this.treeNum;}
     
     /**
      * public int markedNum()
      *
      * Returns the number of elements in the heap.
      *   
      */
      public int markedNum() {return this.markedNum;}
      
    /**
    * public int[] `Rep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    */
    public int[] countersRep() {
    	int[] arr = new int[(int) Math.floor(1 + (Math.log(size()) / Math.log((1 + Math.sqrt(5)) / 2)))];
    	int maxRank = 0;
    	HeapNode curNode = this.last;
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
    */
    public void delete(HeapNode x) {
    	this.decreaseKey(x, x.getKey() - min.getKey() + 1);
    	this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) {
    	x.setKey(x.getKey() - delta);
    	HeapNode parent = x.getParent();
    	if (parent == null) {min = min.getKey() < x.getKey() ? min : x;}
    	else if (x.getKey() < parent.getKey()) {cascadingCut(x);}
    	return ;
    }
    
    /**
     * 
     * @param node
     */
    public void cascadingCut(HeapNode node) {
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
    	last.setLeftAndRight(node);
    	first.setRightAndLeft(node);
    	node.setParent(null);
    	if (node.isSigned()) {  //Makes sure the the node is unsigned as a root.
    		markedNum--;
    		node.unsign();
    	}
    	last = node;
    	this.min = this.min.getKey() < node.getKey() ? this.min : node;  //Updates minimum.
    	if (repeat) {cascadingCut(parent);}
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
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
    */
    public int potential() {return treeNum + 2 * markedNum;}

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks() {return totalLinks;}

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts() {return totalCuts;}

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k) {
    	if (k == 0 || H.size() == 0) {return new int[0];}
    	int[] arr = new int[k];
    	FibonacciHeap meanSus = new FibonacciHeap();
    	HeapNode curOrigin = H.min;
    	HeapNode curNew = meanSus.insert(curOrigin.getKey());
    	curNew.setOtherHeap(H.min);
    	for (int i = 0; i < k; i++) {
    		curNew = meanSus.findMin();
    		curOrigin = curNew.getOtherHeap();
    		arr[i] = curNew.getKey();
    		curOrigin = curOrigin.getChild();
    		if (curOrigin != null ) {
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
    
    public void printRanks() {
    	System.out.println("Ranks Hirearchy:");
    	HeapNode node = first;
    	for (int i = 0; i < treeNum; i++) {
    		System.out.print(node.getKey() + ":");
    		HeapNode next = node.getLeft();
    		while (node != null) {
    			System.out.print(" " + node.getRank());
    			node = node.getChild();
    		}
    		node = next;
    		System.out.println();
    	}
		System.out.println("End Hirearchy");
		return ;
    }
    
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
