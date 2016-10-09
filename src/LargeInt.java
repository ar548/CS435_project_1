/**
 * Created by Alex Rosen on 10/6/2016.
 */
public class LargeInt implements Comparable<LargeInt>{
	public LargeIntHeader head;
	public LargeIntNode first;  // the largest "digit".  To be used to start division and work down
	public LargeIntNode last;   // the least significant "digit". To be used to start addition | subtraction | multiplication

	public LargeInt() {
		this.head = new LargeIntHeader();
		this.first = new LargeIntNode( null, null, 0 );
		this.last = this.first;
	}

	public LargeInt(LargeIntNode first, int sign) {
		// first is assumed to be non-null, and first.prev is assumed to be null
		while(first.getPrev() != null){
			// for me this should never be needed but it is just book-keeping
			first = first.getPrev();
		}
		while(first.getVal() == 0){
			// remove leading zeros caused in subtraction
			// TODO figure out how to fix this so that it does not cause an issue
			LargeIntNode d = first;
			first = first.getNext();
			first.setPrev( null );
			d = null;   // delete d (this really doesnt do anything in java
		}
		this.first = first;

		// find the length and the last node
		int numNodes = 1;
		while(first.getNext() != null) {
			first = first.getNext();
			numNodes++;
		}
		this.last = first;
		head = new LargeIntHeader( numNodes, sign );
	}

	public static LargeInt readInt(String str) {
		// this function reads in a string of hexidecimal text and converts it to the corresponding number.

		// empty string case
		if(str.length() == 0) { return null; }
		// String is all 0s case
		if(str.matches( "[+-]?[0]+" )) { return new LargeInt(); }

		// check for a minus(45) or plus(43) sign.  Positive for default
		int sign;
		String s;
		if(str.charAt( 0 ) == '-') {
			sign = -1;
			s = str.substring( 1 );
		}
		else if(str.charAt( 0 ) == '+') {
			sign = 1;
			s = str.substring( 1 );
		}
		else {
			sign = 1;
			s = str;
		}

		// the case where the int is only one "digit"
		int val;
		if(s.length() <= 4) {
			//System.out.println("this is a small int: " + s.length());
			val = Integer.parseUnsignedInt( s, 16 );
			LargeIntNode node = new LargeIntNode( null, null, val );
			return new LargeInt( node, sign );
		}

		// general case: get the first digit of length 1-4 then the infinite following digits of length 4
		int p = ((s.length() - 1) & 3) + 1;   // this crazy math is to get the first 1-4 digits instead 0-3 digits
		String sub = s.substring( 0, p );
		val = Integer.parseUnsignedInt( sub, 16 );
		LargeIntNode node1 = null, node2 = null, firstNode;
		node1 = new LargeIntNode( node2, null, val );
		if(node2 != null) { node2.setNext( node1 ); }
		node2 = node1;
		firstNode = node1;

		for(int i = p + 4; i < s.length() + 1; i += 4, p += 4) {
			sub = s.substring( p, i );
			val = Integer.parseUnsignedInt( sub, 16 );
			node1 = new LargeIntNode( node2, null, val );
			node2.setNext( node1 );
			node2 = node1;
		}
		return new LargeInt( firstNode, sign );
	}

	public static LargeInt add(LargeInt a, LargeInt b) {
		// this function assumes that the sign of the two opperators is the same.  If it is not then subtract the negative instead.
		int carryVal = 0;
		if(a.head.getSign() == 0) {
			return copy(b);
		}
		else if(b.head.getSign() == 0) {
			return copy(a);
		}
		else if(a.head.getSign() != b.head.getSign()) {
			b.head.setSign( -1 * b.head.getSign() );
			LargeInt c = subtract( a, b );
			b.head.setSign( -1 * b.head.getSign() );
			return c;
		}

		// start the actual addition here
		int val;
		LargeIntNode Aval = a.last;
		LargeIntNode Bval = b.last;
		LargeIntNode Rval1 = null;
		LargeIntNode Rval2 = null;

		while(Aval != null && Bval != null) {
			// add
			val = Aval.getVal() + Bval.getVal() + carryVal;

			// check for overflow
			if(val > 0x0000ffff) {
				carryVal = (val & 0xffff0000) >> 16;
				val = (val & 0x0000ffff);
			}
			else {
				carryVal = 0;
			}

			// store the val and move to the next pair
			Rval1 = new LargeIntNode( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Aval = Aval.getPrev();
			Bval = Bval.getPrev();
		}
		if(Aval == null && Bval == null) {
			val = carryVal;
			Rval1 = new LargeIntNode( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
		}
		while(Aval != null) {
			val = Aval.getVal() + carryVal;
			carryVal = 0;
			Rval1 = new LargeIntNode( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Aval = Aval.getPrev();
		}
		while(Bval != null) {
			val = Bval.getVal() + carryVal;
			carryVal = 0;
			Rval1 = new LargeIntNode( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Bval = Bval.getPrev();
		}


		return new LargeInt( Rval1, a.head.getSign() );   // sign never changes during addition
	}

	public static LargeInt subtract(LargeInt a, LargeInt b) {
		// Performs a-b assuming that a and b have the same sign.  If they dont then add instead

		// 0 cases and non-subtraction case
		if(b.head.getSign() == 0) { return copy(a); }
		else if(a.head.getSign() == 0) {
			LargeInt r =  copy(b);
			r.head.setSign( -1*b.head.getSign() );
			return r;
		}
		else if(a.head.getSign() != b.head.getSign()) {
			b.head.setSign( -1 * b.head.getSign() );
			LargeInt c = add( a, b );
			b.head.setSign( -1 * b.head.getSign() );
			return c;
		}

		// checks if a > b or not to determine the sign
		int sign = 0;
		if(a.compareTo( b ) == 1){
			sign = a.head.getSign();
		}
		else if(a.compareTo( b ) == -1){
			sign = -1*a.head.getSign();
		}
		else{
			return new LargeInt();
		}

		// do actual subtraction here
		LargeIntNode Aval = a.last;
		LargeIntNode Bval = b.last;
		LargeIntNode Rval1 = null;
		LargeIntNode Rval2 = null;

		int val;
		int carryVal = 0;
		while(Aval != null && Bval != null){
			if(Aval.getVal() < Bval.getVal()){
				val = (Aval.getVal() + 0x00010000) - Bval.getVal() + carryVal;
				carryVal = -1;
			}
			else{
				val = Aval.getVal() - Bval.getVal() + carryVal;
				carryVal = 0;
			}
			Rval1 = new LargeIntNode( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Aval = Aval.getPrev();
			Bval = Bval.getPrev();
		}
		while(Aval != null) {
			// handle leftovers in a
			if(Aval.getVal() != 0) {
				val = Aval.getVal() + carryVal;
				carryVal = 0;
			}
			else{
				val = 0;
			}
			Rval1 = new LargeIntNode( null, Rval2, val );
			Rval2.setPrev( Rval1 );
			Rval2 = Rval1;
			Aval = Aval.getPrev();
		}
		while(Bval != null){
			// when the second val is larger you need the opposite of the values
			val = 0x0000ffff - Bval.getVal() + carryVal;
			carryVal = (Bval.getVal() == 0xffff) ? -1 : 0;
			Rval1 = new LargeIntNode( null, Rval2, val );
			Rval2.setPrev( Rval1 );
			Rval2 = Rval1;
			Bval = Bval.getPrev();
		}

		return new LargeInt( Rval1, sign );
	}

	public static LargeInt multiply(LargeInt a, LargeInt b){
		// this function uses b.length < a.length for efficency.  if this is not true they are swapped
		if(a.head.getLength() < b.head.getLength()){ return multiply( b, a ); }
		// if either is 0 return 0
		if(a.head.getSign() == 0 || b.head.getSign() == 0){ return new LargeInt(); }

		// in multiplication if the signs are the same then the sign of the answer is the same
		int sign;
		if (a.head.getSign() == b.head.getSign()){ sign = 1; }
		else { sign = -1; }

		// do the actual multiplication here
		LargeIntNode Aval = a.last;
		LargeIntNode Bval = b.last;
		LargeIntNode Rval1 = null;
		LargeIntNode Rval2 = null;

		LargeInt product = new LargeInt();
		LargeInt partialProduct;
		int val;
		int carryVal = 0;
		for(long i = 0; i < a.head.getLength(); i++){
			for(long j = 0; j < i; j++) {
				Rval1 = new LargeIntNode( null, Rval2, 0 );
				if(Rval2 != null) {
					Rval2.setPrev( Rval1 );
				}
				Rval2 = Rval1;
			}
			for(long j = 1; j <= b.head.getLength(); j++) {
				val = Aval.getVal() * Bval.getVal() + carryVal;
				if(val > 0x0000ffff){
					carryVal = (val & 0xffff0000) >> 16;
					val = val & 0x0000ffff;
				}
				else{
					carryVal = 0;
				}

				// store the value and shift the pointer for b
				Rval1 = new LargeIntNode( null, Rval2, val );
				if(Rval2 != null) {
					Rval2.setPrev( Rval1 );
				}
				Rval2 = Rval1;
				Bval = Bval.getPrev();
			}
			if(carryVal != 0){
				// if there is a final carry value
				Rval1 = new LargeIntNode( null, Rval2, carryVal );
				Rval2.setPrev( Rval1 );
			}
			partialProduct = new LargeInt( Rval1, 1 );
			product = add( product, partialProduct );
			Rval1 = null;
			Rval2 = null;
			Aval = Aval.getPrev();
			Bval = b.last;
			carryVal = 0;
		}

		product.head.setSign( sign );
		return product;
	}

	public static LargeInt exponent(LargeInt b, long e){
		// Recursive definition for exponentiation.  e is assumed to be positive for this method

		// if e is 0 return 1, if e is 1 return b
		LargeInt c;
		if(e == 0){
			LargeIntNode Rval = new LargeIntNode( null, null, 1 );
			return new LargeInt( Rval, 1 );
		}
		else if( e == 1 ){
			return copy(b);
		}
		else if( e == 2 ){
			return multiply( b, b );
		}
		else if( (e&1) == 0){
			// if e is even
			c = exponent( b, e/2 );
			return multiply( c, c );
		}
		else{
			// if e is odd
			c = exponent( b, e/2 );
			return multiply( b, multiply( c, c ) );
		}
	}

	private static LargeInt copy(LargeInt a){
		// s simple function to copy the value of a LargeInt to another storage container
		LargeIntNode node1 = null;
		LargeIntNode node2 = null;
		LargeIntNode Aval = a.first;
		LargeInt r;

		while(Aval != null) {
			node1 = new LargeIntNode( node2, null, Aval.getVal() );
			if(node2 != null) {
				node2.setNext( node1 );
			}
			node2 = node1;
			Aval = Aval.getNext();
		}

		r = new LargeInt( node1, a.head.getSign() );
		return r;
	}

	public void MAKENULL(){
		this.head = null;
		LargeIntNode curr = first.getNext();
		LargeIntNode del = first;

		while(curr != null){
			del.setNext( null );    // delete pointers between the nodes
			curr.setPrev( null );   // delete pointers between the nodes
			del = curr;
			curr = curr.getNext();
		}
		del = null;                 // ready the lasy value for garbage collection.  Also setting an object
									// to null to "save space" queues up the garbage collector sooner
	}

	@Override public String toString() {
		// the toString method for printing
		String s;
		if(this.head.getSign() == 0){ return "0"; }
		else if (this.head.getSign() == -1){ s = "-"; }
		else{ s = "+"; }    // this doesnt have to be here because no sign is assumed to be positive but i put it here anyways to make everything line up nicely

		LargeIntNode node = first;
		while(node != null) {
			String d = Integer.toHexString( node.getVal() );
			// make sure that leading 0s are included
			while(d.length() < 4) {
				d = '0' + d;
			}
			s += d;
			node = node.getNext();
		}
		return s;
	}

	@Override public int compareTo(LargeInt that){
		// the compareTo function so that I can determine the sign for subtraction
		final int LESS = -1;
		final int EQUAL = 0;
		final int GREATER = 1;

		if(this.head.getSign() == 0){
			if(that.head.getSign() == 1){ return LESS; }
			if(that.head.getSign() == 0){ return EQUAL; }
			if(that.head.getSign() == -1){ return GREATER; }
		}
		if(this.head.getSign() == 1){
			if(that.head.getSign() == 0){ return LESS; }
			if(that.head.getSign() == -1){ return LESS; }
			if(this.head.getLength() < that.head.getLength()){ return LESS; }
			if(this.head.getLength() > that.head.getLength()){ return GREATER; }
		}
		if(this.head.getSign() == -1){
			if(that.head.getSign() == 0){ return GREATER; }
			if(that.head.getSign() == 1){ return GREATER; }
			if(this.head.getLength() < that.head.getLength()){ return GREATER; }
			if(this.head.getLength() > that.head.getLength()){ return LESS; }
		}

		// at this point they must be equal length and the same sign
		LargeIntNode thi = this.first;
		LargeIntNode tha  = that.first;
		for(long length = this.head.getLength();length > 0; length--){
			// check each "digit" to until you find one that is less than the other
			if(thi.getVal() < tha.getVal()){
				return LESS;
			}
			else if(thi.getVal() > tha.getVal()){
				return GREATER;
			}
			else{
				thi = thi.getNext();
				tha = tha.getNext();
			}
		}
		return EQUAL;
	}
}
