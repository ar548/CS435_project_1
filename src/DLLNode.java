public class DLLNode {
	private DLLNode prev;
	private DLLNode next;
	private int val;    // the value of the hex / dec number

	public DLLNode(){
		this.prev = null;
		this.next = null;
		val = 0;
	}

	public DLLNode(DLLNode prev, DLLNode next, int val){
		this.prev = prev;
		this.next = next;
		this.val = val;
	}

	public DLLNode getPrev(){ return this.prev; }
	public DLLNode getNext(){ return this.next; }
	public void setPrev(DLLNode node){this.prev = node; }
	public void setNext(DLLNode node){this.next = node; }
	public void setVal(int v){this.val = v; }
	public int getVal(){ return this.val; }
	public int getData(){ return this.val; }

}
