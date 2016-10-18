/**
 * Created by Alex Rosen on 10/6/2016.
 */
public class LongIntegerHeader {
	private int sign;       // 1 is pos, 0 is 0, -1 is neg
	private long length;    // length of the largeInt | default: 1

	public LongIntegerHeader(){
		this.length = 1;
		this.sign = 0;
	}

	public LongIntegerHeader(long length, int sign){
		this.length = length;
		this.sign = sign;
	}

	public void setLength(long length){ this.length = length; }
	public void increaseLength(){ this.length++; }
	public void decreaseLength(){ this.length--; }
	public void setSign(int sign){ this.sign = sign; }
	public int getSign(){ return this.sign; }
	public long getLength(){ return this.length; }

}
