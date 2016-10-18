/**
 * Created by Alex Rosen on 10/13/2016.
 */
public class tester {
	public static void main(String args[]){
		LongInteger A = new LongInteger( "12500000000" );
		LongInteger B = new LongInteger( "50" );
		LongInteger C = LongInteger.divide( A, B );

//		LongInteger D = new LongInteger( "+500000" );
//		LongInteger E = new LongInteger( "00025000" );
//		LongInteger C = LongInteger.multiply( D, E );
		System.out.println(C);
	}
}
