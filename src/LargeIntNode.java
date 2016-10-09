/**
 * Created by Alex Rosen on 10/6/2016.
 */
public class LargeIntNode {
	private LargeIntNode prev;
	private LargeIntNode next;
	private int val;    // the value of the hex number

	public LargeIntNode(LargeIntNode prev, LargeIntNode next, int val){
		this.prev = prev;
		this.next = next;
		this.val = val;
	}

	public LargeIntNode getPrev(){ return this.prev; }
	public LargeIntNode getNext(){ return this.next; }
	public void setPrev(LargeIntNode node){this.prev = node; }
	public void setNext(LargeIntNode node){this.next = node; }
	public int getVal(){ return this.val; }

}
