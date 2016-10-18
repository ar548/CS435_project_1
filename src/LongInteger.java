/**
 * Created by Alex Rosen on 10/6/2016.
 */
public class LongInteger implements Comparable<LongInteger>{
	public LongIntegerHeader head;
	public Node first;  // the most significant "digit".  To be used to start division and work down
	public Node last;   // the least significant "digit". To be used to start addition | subtraction | multiplication

	public LongInteger() {
		this.head = new LongIntegerHeader();
		this.first = new Node( null, null, 0 );
		this.last = this.first;
	}

	public LongInteger(Node first, int sign) {
		// first is assumed to be non-null, and first.prev is assumed to be null
		if(first != null) {
			while(first.getPrev() != null) {
				// for me this should never be needed but it is just book-keeping
				first = first.getPrev();
			}
			while(first.getVal() == 0) {
				// remove leading zeros in case the user is trying to break my program.  not that they should affect anything
				Node d = first;
				first = first.getNext();
				if(first != null) {
					first.setPrev( null );
				}
				else {
					break;
				}
				d = null;   // delete d (this really doesnt do anything in java
			}
		}
		this.first = first;

		// find the length and the last node
		int numNodes = 1;
		if(first != null) {
			while(first.getNext() != null) {
				first = first.getNext();
				numNodes++;
			}
		}
		this.last = first;
		head = new LongIntegerHeader( numNodes, sign );
	}

	public static final LongInteger readInt(String str) {
		// this function reads in a string of (hexi)decimal text and converts it to the corresponding number.

		// empty string case
		if(str.length() == 0) { return null; }
		// String is all 0s case
		if(str.matches( "[+-]?[0]+" )) { return new LongInteger(); }

		// check for a minus(45) or plus(43) sign.  Positive for default and remove all invalid characters (aka any non hex characters)
		int sign;
		String s;
		if(str.charAt( 0 ) == '-') {
			sign = -1;
			s = str.substring( 1 ).replaceAll( "[^0-9]", "" );        // for dec
			//s = str.substring( 1 ).replaceAll( "[^0-9a-fA-F]", "" );    // for hex

		}
		else if(str.charAt( 0 ) == '+') {
			sign = 1;
			s = str.substring( 1 ).replaceAll( "[^0-9]", "" );        // for dec
			//s = str.substring( 1 ).replaceAll( "[^0-9a-fA-F]", "" );    // for hex
		}
		else {
			sign = 1;
			s = str.replaceAll( "[^0-9]", "" );       // for dec
			//s = str.replaceAll( "[^0-9a-fA-F]", "" );   // for hex
		}

		// the case where the int is only one "digit"
		int val;
		if(s.length() <= 4) {
			val = Integer.parseUnsignedInt( s, 10 );
			Node node = new Node( null, null, val );
			return new LongInteger( node, sign );
		}

		// general case: get the first digit of length 1-4 then the infinite following digits of length 4
		int p = ((s.length() - 1) & 3) + 1;   // this crazy math is to get the first 1-4 digits instead 0-3 digits
		String sub = s.substring( 0, p );
		val = Integer.parseUnsignedInt( sub, 10 );
		Node node1 = null, node2 = null, firstNode;
		node1 = new Node( node2, null, val );
		if(node2 != null) { node2.setNext( node1 ); }
		node2 = node1;
		firstNode = node1;

		for(int i = p + 4; i < s.length() + 1; i += 4, p += 4) {
			sub = s.substring( p, i );
			val = Integer.parseUnsignedInt( sub, 10 );
			node1 = new Node( node2, null, val );
			node2.setNext( node1 );
			node2 = node1;
		}
		return new LongInteger( firstNode, sign );
	}

	public static final LongInteger add(LongInteger a, LongInteger b) {
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
			LongInteger c = a.subtract( b );
			b.head.setSign( -1 * b.head.getSign() );
			return c;
		}

		// start the actual addition here
		int val;
		Node Aval = a.last;
		Node Bval = b.last;
		Node Rval1 = null;
		Node Rval2 = null;

		while(Aval != null && Bval != null) {
			// add
			val = Aval.getVal() + Bval.getVal() + carryVal;

			// check for overflow
			/*/
			// carryVal for hex
			if(val > 0x0000ffff) {
				carryVal = (val & 0xffff0000) >> 16;
				val = (val & 0x0000ffff);
			}
			else {
				carryVal = 0;
			}
			//*/
			/**/
			// carryVal for decimal
			if(val >= 10000){
				carryVal = val / 10000;
				val = val % 10000;
			}
			else{
				carryVal = 0;
			}
			//*/

			// store the val and move to the next pair
			Rval1 = new Node( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Aval = Aval.getPrev();
			Bval = Bval.getPrev();
		}
		if(Aval == null && Bval == null) {
			val = carryVal;
			Rval1 = new Node( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
		}
		while(Aval != null) {
			val = Aval.getVal() + carryVal;
			carryVal = 0;
			Rval1 = new Node( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Aval = Aval.getPrev();
		}
		while(Bval != null) {
			val = Bval.getVal() + carryVal;
			carryVal = 0;
			Rval1 = new Node( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Bval = Bval.getPrev();
		}


		return new LongInteger( Rval1, a.head.getSign() );   // sign never changes during addition
	}

	public static final LongInteger subtract(LongInteger a, LongInteger b) {
		// Performs a-b assuming that a and b have the same sign.  If they dont then add instead

		// 0 cases and non-subtraction case
		if(b.head.getSign() == 0) { return copy(a); }
		else if(a.head.getSign() == 0) {
			LongInteger r =  copy(b);
			r.head.setSign( -1*b.head.getSign() );
			return r;
		}
		else if(a.head.getSign() != b.head.getSign()) {
			b.head.setSign( -1 * b.head.getSign() );
			LongInteger c = a.add( b );
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
			return new LongInteger();
		}

		// do actual subtraction here
		Node Aval = a.last;
		Node Bval = b.last;
		Node Rval1 = null;
		Node Rval2 = null;

		int val;
		int carryVal = 0;
		while(Aval != null && Bval != null){
			if(Aval.getVal() < Bval.getVal()){
				/* // carryVal for Hex
				 * val = (Aval.getVal() + 0x00010000) - Bval.getVal() + carryVal;
				 */
				// carryVal for decimal
				val = (Aval.getVal() + 10000) - Bval.getVal() + carryVal;
				carryVal = -1;
			}
			else{
				val = Aval.getVal() - Bval.getVal() + carryVal;
				carryVal = 0;
			}
			Rval1 = new Node( null, Rval2, val );
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
			Rval1 = new Node( null, Rval2, val );
			if(Rval2 != null) {
				Rval2.setPrev( Rval1 );
			}
			Rval2 = Rval1;
			Aval = Aval.getPrev();
		}
		while(Bval != null){
			// when the second val is larger you need the opposite of the values
			/*/
			// carryVal for Hex
			val = 0x0000ffff - Bval.getVal() + carryVal;
			carryVal = (Bval.getVal() == 0xffff) ? -1 : 0;
			//*/
			/**/
			// carryVal for decimal
			// val = 9999 - Bval.getVal() + carryVal;
			val = Bval.getVal() + carryVal;
			//carryVal = (Bval.getVal() == 9999 ? -1 : 0;
			carryVal = 0;
			//*/
			Rval1 = new Node( null, Rval2, val );
			Rval2.setPrev( Rval1 );
			Rval2 = Rval1;
			Bval = Bval.getPrev();
		}

		return new LongInteger( Rval1, sign );
	}

	public static final LongInteger multiply(LongInteger a, LongInteger b){
		long m, n, o, p;
		// this function uses b.length < a.length for efficency.  if this is not true they are swapped
		//if(a.head.getLength() < b.head.getLength()){ return multiply( b, a ); }
		n = a.head.getLength();
		m = b.head.getLength();
		if( n<m ){ return multiply( b, a ); }
		// if either is 0 return 0
		if(a.head.getSign() == 0 || b.head.getSign() == 0){ return new LongInteger(); }

		// in multiplication if the signs are the same then the sign of the answer is the same
		int sign;
		if (a.head.getSign() == b.head.getSign()){ sign = 1; }
		else { sign = -1; }

		// do the actual multiplication here
		Node Aval = a.last;
		Node Bval = b.last;
		Node Rval1 = null;
		Node Rval2 = null;

		LongInteger product = new LongInteger();
		LongInteger partialProduct;
		int val;
		int carryVal = 0;
		for(long i = 0; i < a.head.getLength(); i++){

			// no need to do any work if Bval is 0 because this digit doesnt add anything
			//if(Bval.getVal() == 0){ continue; }

			for(long j = 0; j < i; j++) {
				Rval1 = new Node( null, Rval2, 0 );
				if(Rval2 != null) {
					Rval2.setPrev( Rval1 );
				}
				Rval2 = Rval1;
			}
			for(long j = 1; j <= b.head.getLength(); j++) {
				val = Aval.getVal() * Bval.getVal() + carryVal;
				/*/
				// carryVal for hex
				if(val > 0x0000ffff){
					carryVal = (val & 0xffff0000) >> 16;
					val = val & 0x0000ffff;
				}
				else{
					carryVal = 0;
				}
				//*/
				/**/
				// carryVal for decimal
				if(val >= 10000){
					carryVal = val / 10000;
					val = val % 10000;
				}
				else{
					carryVal = 0;
				}
				//*/

				// store the value and shift the pointer for b
				Rval1 = new Node( null, Rval2, val );
				if(Rval2 != null) {
					Rval2.setPrev( Rval1 );
				}
				Rval2 = Rval1;
				Bval = Bval.getPrev();
			}
			if(carryVal != 0){
				// if there is a final carry value
				Rval1 = new Node( null, Rval2, carryVal );
				Rval2.setPrev( Rval1 );
				carryVal = 0;
			}
			partialProduct = new LongInteger( Rval1, 1 );
			product = add( product, partialProduct );
			Rval1 = null;
			Rval2 = null;
			Aval = Aval.getPrev();
			Bval = b.last;
		}

		product.head.setSign( sign );
		return product;
	}

	public static final LongInteger divide(LongInteger a, LongInteger b) throws ArithmeticException{
		// this means a / b so b must be smaller than a
		if(b.head.getSign() == 0){ throw new ArithmeticException("Divide By Zero Error!"); }
		if(a.head.getSign() == 0){ return new LongInteger(  ); }
		if(a.compareTo( b ) == -1){ return new LongInteger(  ); }
		if(a.compareTo( b ) ==  0){ return new LongInteger( new Node( null, null, 1 ), 1 ); }

		int a_sign = a.head.getSign();
		int b_sign = b.head.getSign();
		a.head.setSign( 1 );
		b.head.setSign( 1 );
		int sign = (a_sign == b_sign) ? 1 : -1;

		LongInteger c = copy(b);
		LongInteger d = copy(a);

		a.head.setSign( a_sign );
		b.head.setSign( b_sign );

		// make a and b the same size
		Node Cval = c.last;
		Node n;
		for(long i = b.head.getLength(); i < a.head.getLength(); i++) {
			n = new Node( Cval, null, 0 );
			Cval.setNext( n );
			Cval = n;
			c.head.increaseLength();
		}
		c.last = Cval;

		Node Rval1 = null, Rval2 = null, temp;

		boolean eq = false;
		int val = 0;
		Integer divTry = 0;
		LongInteger BigDivTry;
		for(long i = b.head.getLength(); i < a.head.getLength(); i++) {
			// shorten c
			temp = Cval;
			Cval = Cval.getPrev();
			Cval.setNext( null );
			temp.setPrev( null );
			c.last = Cval;
			c.head.decreaseLength();

			// get the value of this node
			if(!eq) {
				// the break case where you just add 0 from here on out
				int comp1 = d.compareTo( c );
				if(comp1 == 0) {
					eq = true;
					val = 1;
				}
				else if(comp1 == -1){
					val = 0;
				}
				else{
					// TODO get divTry here
					divTry = (d.first.getVal()*10000 + d.first.getNext().getVal()) / c.first.getVal();
					BigDivTry = new LongInteger( divTry.toString() );

					if( divTry != 0) {
						LongInteger q = multiply( c, BigDivTry );
						// TODO change this
						boolean l = q.lessThan( d );
						boolean e = q.equalTo( d );
						if(l) {
							d = subtract( d, q );
						}
						else if(e) {
							eq = true;
							d = subtract( d, q );
						}
						else {
							divTry -= 1;
							d = subtract( d, subtract( q, BigDivTry ) );
						}
					}
					val = divTry;
				}
			}
			else{
				val = 0;
			}

			Rval1 = new Node( Rval2, null, val );
			if(Rval2 != null){
				Rval2.setNext( Rval1 );
			}
			Rval2 = Rval1;
		}

		// System.out.println("The remainder is: " + d);
		return new LongInteger( Rval1, sign );
	}

	public static final LongInteger exponent(LongInteger b, long e){
		// Recursive definition for exponentiation.  e is assumed to be positive for this method

		// if e is 0 return 1, if e is 1 return b
		LongInteger c = copy(b);
		LongInteger result = new LongInteger( new Node( null, null, 1 ), 1 );

		while(e > 0){
			if((e&1) == 1){
				result = multiply( result, c );
			}
			c = multiply( c, c );
			e /= 2;
		}
		return result;
	}

	private static final LongInteger copy(LongInteger a){
		// s simple function to copy the value of a LargeInt to another storage container
		Node node1 = null;
		Node node2 = null;
		Node Aval = a.first;
		LongInteger r;

		while(Aval != null) {
			node1 = new Node( node2, null, Aval.getVal() );
			if(node2 != null) {
				node2.setNext( node1 );
			}
			node2 = node1;
			Aval = Aval.getNext();
		}

		r = new LongInteger( node1, a.head.getSign() );
		return r;
	}

	public void MAKENULL(){
		this.head = null;
		Node curr = first.getNext();
		Node del = first;

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
		String s = "";
		String sign = "";
		if(this.head.getSign() == 0){ return "0"; }
		else if (this.head.getSign() == -1){ sign = "-"; }
		else{ sign = "+"; }    // this doesnt have to be here because no sign is assumed to be positive but i put it here anyways to make everything line up nicely

		Node node = first;
		while(node != null) {
			//String d = Integer.toHexString( node.getVal() );      // for hex
			String d = Integer.toString( node.getVal() );           // for dec
			// make sure that leading 0s are included
			while(d.length() < 4) {
				d = '0' + d;
			}
			s += d;
			node = node.getNext();
		}
		int i = 0;
		while(s.charAt( i++ ) == '0'){}     // remove leading 0s
		return sign + s.substring( i-1 );
	}

	@Override public int compareTo(LongInteger that){
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
		Node thi = this.first;
		Node tha  = that.first;
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

	// functions for the implementation of this project for the class's test cases
	public LongInteger(String str){

		// empty string case
		if(str.length() == 0) {
			this.head = null;
			this.first = null;
			this.last = null;
			return;
		}

		// String is all 0s case
		if(str.matches( "[+-]?[0]+" )) {
			this.head = new LongIntegerHeader( 1, 0 );
			this.first = new Node( null, null, 0 );
			this.last = this.first;
			return;
		}

		// check for a minus(45) or plus(43) sign.  Positive for default and remove all invalid characters (aka any non hex characters)
		int sign = 1;
		String s;
		if(str.charAt( 0 ) == '-') {
			sign = -1;
			s = str.substring( 1 ).replaceAll( "[^0-9]", "" );        // for dec
			//s = str.substring( 1 ).replaceAll( "[^0-9a-fA-F]", "" );    // for hex

		}
		else if(str.charAt( 0 ) == '+') {
			sign = 1;
			s = str.substring( 1 ).replaceAll( "[^0-9]", "" );        // for dec
			//s = str.substring( 1 ).replaceAll( "[^0-9a-fA-F]", "" );    // for hex
		}
		else {
			sign = 1;
			s = str.replaceAll( "[^0-9]", "" );       // for dec
			//s = str.replaceAll( "[^0-9a-fA-F]", "" );   // for hex
		}

		// the case where the int is only one "digit"
		int val;
		if(s.length() <= 4) {
			val = Integer.parseUnsignedInt( s, 10 );
			this.first = new Node( null, null, val );
			this.last = first;
			this.head = new LongIntegerHeader( 1, sign );
			return;
		}

		// general case: get the first digit of length 1-4 then the infinite following digits of length 4
		int p = ((s.length() - 1) & 3) + 1;   // this crazy math is to get the first 1-4 digits instead 0-3 digits
		int length = 1;
		String sub = s.substring( 0, p );
		val = Integer.parseUnsignedInt( sub, 10 );
		Node node1, node2 = null;
		node1 = new Node( node2, null, val );
		if(node2 != null) { node2.setNext( node1 ); }
		node2 = node1;
		this.first = node1;

		for(int i = p + 4; i < s.length() + 1; i += 4, p += 4) {
			sub = s.substring( p, i );
			val = Integer.parseUnsignedInt( sub, 10 );
			node1 = new Node( node2, null, val );
			node2.setNext( node1 );
			node2 = node1;
			length++;
		}

		this.last = node1;
		this.head = new LongIntegerHeader( length, sign );
	}
	public void output(){ System.out.println(this); }
	public void insertFirst(int v){
		Node n = new Node( null, first, v );
		first.setPrev( n );
		first = n;
	}
	public void insertLast(int v){
		Node n = new Node( last, null, v );
		last.setNext( n );
		last = n;
	}
	public long getDigitCount(){ return ((int)Math.ceil( Math.log10( this.first.getVal() )) + 4*(this.head.getLength() - 1)); }
	public long size(){ return this.head.getLength(); }
	public boolean getSign(){ return (this.head.getSign() == -1);}
	public boolean lessThan(LongInteger that){ return (compareTo( that ) == -1); }
	public boolean equalTo(LongInteger that){ return (compareTo( that ) == 0); }
	public boolean greaterThan(LongInteger that){ return (compareTo( that ) == 1); }
	public boolean isFirst(Node p){ return (p.equals( first )); }   // this should just check the previous node to see if its null but i dont think that how the TA wants it*/
	public boolean isLast(Node p){ return (p.equals( last )); }     // this should just check the next node to see if its null but i dont think that how the TA wants it
	public boolean isEmpty(){ return (this.first == null); }  // my lists will never be empty
	public LongInteger add(LongInteger i){return add(this, i);}
	public LongInteger subtract(LongInteger i){ return subtract( this, i ) ; }
	public LongInteger multiply(LongInteger i){ return multiply(this, i); }
	public LongInteger divide(LongInteger i){ return divide(this, i); }
	public LongInteger power(int p){ return exponent( this, p ); }
	public Node getLast(){ return this.last; }
	public Node getFirst(){ return this.first; }
	public Node getPrev(Node p) throws NullPointerException{
		if(p.getPrev() == null) throw new NullPointerException( " This node has no previous node.  It is first. " );
		else return p.getPrev();
	}
	public Node getNext(Node p) throws NullPointerException{
		if(p.getNext() == null) throw new NullPointerException( " This node has no next node.  It is last. " );
		else return p.getNext();
	}
}