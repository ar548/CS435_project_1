/*
 * Do NOT change this class 
 */

import java.util.ArrayList;

/**
 *
 * @author your name here
 */
public class Step1TestCase {
    public static void main(String[] args) {
  
        LongInteger A = new LongInteger("3333");
        LongInteger B = new LongInteger("99999999");
        LongInteger C = new LongInteger("-732246813575");
        LongInteger D = new LongInteger("789180270361023456789");
        LongInteger E = new LongInteger("3591423550000000010056810000054593452907711568359");
        LongInteger F = new LongInteger("-890350003274594847454317890");
        LongInteger G = new LongInteger("84729302390234702973402973420937420973420937420937234872349872934872893472893749287423847");
        LongInteger H = new LongInteger("-42398534342983742987342987339234098230498203894209928374662342342342356723423");
        LongInteger I = new LongInteger("61284364131684386183513516846948354348943643518468434351684843516846843153846843138468131538431\n" +
"35138413513843813513813138438435153454154515151513141592654543515316848613242587561516511233246174561276521672162416274123076527");
        ArrayList<LongInteger> mainList = new ArrayList<LongInteger>();
        mainList.add(A);
        mainList.add(B);
        mainList.add(C);
        mainList.add(D);
        mainList.add(E);
        mainList.add(F);
        mainList.add(G);
        mainList.add(H);
        mainList.add(I);

        System.out.println("Node Output\n");

        for ( LongInteger l : mainList ){
            Node node = l.getFirst();
            while (!l.isLast(node)){
                System.out.printf("%5d ->", node.getData());
                node = l.getNext(node);
            }
            System.out.printf("%5d\n", node.getData());
            System.out.println("Total Number of Digits: "+ l.getDigitCount());
        }

        System.out.println("\nLongInteger output() Function\n");

        A.output();
        B.output();
        C.output();
        D.output();
        E.output();
        F.output();
        G.output();
        H.output();
        I.output();
        
        System.out.println("\nUtility Operations\n");
        int a = 2222;
        int b = 99999999;
        System.out.printf("Overflow: %-1d%n", UtilityOperations.overFlow(a));
        System.out.printf("Underflow: %-8d%n", UtilityOperations.underFlow(a));
        System.out.printf("Digits: %-1d%n%n", UtilityOperations.digits(a));

        System.out.printf("Overflow: %-1d%n", UtilityOperations.overFlow(b));
        System.out.printf("Underflow: %-8d%n", UtilityOperations.underFlow(b));
        System.out.printf("Digits: %-1d%n", UtilityOperations.digits(b));
        
        System.out.println("\nComparisons");
        for(int i = 0; i < mainList.size(); i++){
            for(int j = 0; j < mainList.size(); j++){
                System.out.println("\n"+Character.toString((char)(i+65))+" compared to "+Character.toString((char)(j+65)));
                System.out.printf("Equal to:%6b   Less Than:%6b   Greater Than:%6b%n",mainList.get(i).equalTo(mainList.get(j)), mainList.get(i).lessThan(mainList.get(j)),mainList.get(i).greaterThan(mainList.get(j)));
            }
        }
    }
}
