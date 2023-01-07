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
    	size = size + 1;
    	treeNum = treeNum + 1;
    	HeapNode node = new HeapNode(key);
    	if (isEmpty()) {first = last = min = node;}
    	else {
    		last.setLeft(node);
    		node.setRight(this.last);
    		node.setLeft(this.first);
    		last = node;
    		min = min.getKey() < key ? min : node;
    	}
    	return node;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if (isEmpty()) {System.out.println("can't delete from an empty list.");}
    	else {
    		this.size = this.size -1;
    		this.treeNum = this.treeNum - 1 + this.min.getRank();
    		HeapNode leftChild = this.min.getChild();
    		HeapNode rightChild = leftChild.getLeft();
    		HeapNode leftNode = this.min.getLeft();
    		HeapNode rightNode = this.min.getRight();
    		leftNode.setRight(leftChild);
    		rightNode.setLeft(rightChild);
    		leftChild.setParent(null);
    		updatesMinAndConsolidate();
    		
    	}
     	return; // should be replaced by student code
     	
    }
    /**
     * public void findNewMin()
     * 
     * updates the new minimum after delete the current.
     * makes full consolidation.
     * @return the new minimum node.
     */
    public void updatesMinAndConsolidate() {
    	int arrayLength = (int)Math.ceil(Math.log(this.size)/Math.log(2));
    	HeapNode[] rankTrees = new HeapNode[arrayLength];
    	HeapNode curNode = this.last;
    	HeapNode nextNode;
    	HeapNode sameRankNode;
    	HeapNode combineNode;
    	for (int i = 0; i < this.treeNum; i++) {
    		nextNode = curNode.getRight();
    		sameRankNode = rankTrees[curNode.getRank()];
    		while (sameRankNode != null) {
    			combineNode = combineTwoTrees(curNode, sameRankNode);
    			rankTrees[curNode.getRank()] = null;
    			curNode = combineNode;
    		}
    		rankTrees[curNode.getRank()] = curNode;
    		curNode = nextNode;
    	}
    	updateHeapAccordingTheArray(rankTrees);
    	return;
    }
    
    /**
     * public HeapNode combineTwoTrees(t1, t2)
     * combine two trees with the same degree.
     * @type t1, t2 - HeapNode. 
     * @param t1, t2 - the roots of those trees.
     * @return the root of the combined tree. (HeapNode)
     */
    public HeapNode combineTwoTrees(HeapNode t1,HeapNode t2) {
    	totalLinks = totalLinks + 1;
    	HeapNode smaller = t1;
    	HeapNode bigger = t2;
    	if (t1.getKey() > t2.getKey()) {
    		smaller = t2;
    		bigger = t1;
    	}
    	HeapNode smallerLeftChild = smaller.getLeft();
    	HeapNode smallerRightChild = smaller.getRight();
    	smaller.setChild(bigger);
    	smaller.addToRank();
    	bigger.setParent(smaller);
    	bigger.setLeft(smallerRightChild);
    	bigger.setRight(smallerLeftChild);
    	
    	return smaller;
    }
    /**
     * public void updateHeapAccordingTheArray(HeapNode[] rankArray)
     * gets the heap and the ranked tree array, and updates the heap
     * according to the array (including updates minimum).
     * @param rankArray - the array from the findNewMin() process.
     */
    public void updateHeapAccordingTheArray(HeapNode[] rankArray) {
    	this.min = null;
    	this.last = null;
    	HeapNode curNode = null;
    	this.treeNum = 0;
    	for (HeapNode n: rankArray) {
    		if (n == null) {
    			continue;
    		}
    		else {
    			this.treeNum = this.treeNum + 1;
	    		if (this.last == null) {
	    			this.last = n;
	    			this.min = n;
	    		}
	    		else {
	    			curNode.setRight(n);
	    			n.setLeft(curNode);
	    			this.min = this.min.getKey() < n.getKey() ? this.min : n;
	    		}
    			this.first = n;
    			curNode = n;
    		}
    		
    	}
    	this.last.setLeft(this.first);
    	this.first.setRight(this.last);
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
    public void meld (FibonacciHeap heap2)
    {	this.size = this.size() + heap2.size();
    	this.treeNum = this.treeNum() + heap2.treeNum();
    	this.markedNum = this.markedNum() + heap2.markedNum();
    	HeapNode originLeft = this.last;
    	HeapNode originRight = this.first;
    	HeapNode newLeft = heap2.last;
    	HeapNode newRight = heap2.first;
    	originLeft.setLeft(newRight);
    	newRight.setRight(originLeft);
    	originRight.setRight(newLeft);
    	newLeft.setLeft(originRight);
    	
    	return;  		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return this.size;
    }
    /**
     * public int treeNum()
     *
     * Returns the number of elements in the heap.
     *   
     */
     public int treeNum()
     {
     	return this.treeNum;
     }
     
     /**
      * public int markedNum()
      *
      * Returns the number of elements in the heap.
      *   
      */
      public int markedNum()
      {
      	return this.markedNum;
      }
      
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    */
    public int[] countersRep()
    {
    	int arrayLength = (int)Math.ceil(Math.log(this.size)/Math.log(2));
    	int[] arr = new int[arrayLength];
    	int maxRank = 0;
    	HeapNode curNode = this.last;
    	for (int i = 0; i < this.treeNum ; i++) {
    		arr[curNode.getRank()] = arr[curNode.getRank()] + 1;
    		maxRank = curNode.getRank() > maxRank ? curNode.getRank() : maxRank;
    		curNode = curNode.getRight();
    	}
    	int[] finalArr = new int[maxRank+1];
    	for (int j = 0; j < maxRank + 1; j++) {
    		finalArr[j] = arr[j];
    	}
        return finalArr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	this.decreaseKey(x, x.getKey()-this.min.getKey()-1);
    	this.deleteMin();
    	return;
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.setKey(x.getKey()-delta);
    	if (x.getKey() < x.getParent().getKey()) {
    		cascadingCut(x);
    	}
    	return;
    }
    
    /**
     * 
     * @param x
     */
    public void cascadingCut(HeapNode x) {
    	totalCuts = totalCuts + 1;
    	HeapNode parent = x.getParent();
    	Boolean repeat = parent.isSigned(); //checks if we need to do it again.
    	if (repeat == false) { 
    		parent.sign();
    		this.markedNum = this.markedNum + 1;
    	}
    	parent.minusToRank();
    	///checks if the node was only child.
    	if (parent.getRank() > 0) { 
        	if (parent.getChild() == x) {
        		parent.setChild(x.getRight());
        		x.getRight().setLeft(x.getLeft());
        		x.getLeft().setRight(x.getRight());
        	}
    	}
    	else {
    		parent.setChild(null);
    	}
    	///Puts the node in the left of the tree list.
    	this.treeNum = this.treeNum + 1;
    	HeapNode originLeft = this.last;
    	this.last = x;
    	x.setRight(originLeft);
    	originLeft.setLeft(x);
    	x.setLeft(this.first);
    	this.first.setRight(x);
    	x.setParent(null);
    	///Makes sure the the node is unsigned a a root.
    	if (x.isSigned()) {
    		this.markedNum = this.markedNum - 1;
    		x.unsign();
    	}
    	///Updates minimum.
    	this.min = this.min.getKey() < x.getKey() ? this.min : x;
    	if (repeat) {
    		cascadingCut(parent);
    	}
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() 
    {    
        return size-markedNum; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
        return treeNum + 2*markedNum; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
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

    	public HeapNode(int key) {
    		this.key = key;
    		this.rank = 0;
    		sign = false;
    		right = left = this;
    		child = parent = null;
    	}

    	public int getKey() {return this.key;}
    	
    	public int getRank() {return this.rank;}
    	
    	public HeapNode getLeft() {return left;}
    	
    	public HeapNode getRight() {return right;}
    	
    	public HeapNode getChild() {return child;}
    	
    	public HeapNode getParent() {return parent;}
    	
    	public boolean isSigned() {return sign;}
    	
    	public void setLeft(HeapNode left) {this.left = left;}
    	
    	public void setRight(HeapNode right) {this.right = right;}
    	
    	public void setChild(HeapNode child) {this.child = child;}
    	
    	public void setParent(HeapNode parent) {this.parent = parent;}
    	
    	public void setKey(int key) {this.key = key;}
    	
    	public void addToRank() {this.rank = this.rank+1;}
    	
    	public void minusToRank() {this.rank = this.rank-1;}
    	
    	public void sign() {sign = true;}
    	
    	public void unsign() {sign = false;}
    }
}
