/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author your name here
 */
public class UtilityOperations {

/**
 * Implement the following methods
 */
    public static int overFlow(int t) {
		return t / 10000;
    }

    public static int underFlow(int t) {
	    return t%10000;
    }

    public static int digits(int t) {
    	return (int)Math.ceil( Math.log10( t ));
    }

}
