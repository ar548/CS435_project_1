/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author hl395
 */
public class Step3TestCase {
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
            DLLNode node = l.getFirst();
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
        
        for(int i = 0; i <mainList.size(); i++){
            for(int j = 0; j <mainList.size(); j++) {
                if(i==j) continue;
                System.out.println("\n" + Character.toString((char) (i + 65)) + " added to " + Character.toString((char) (j + 65)));
                mainList.get(i).add(mainList.get(j)).output();
            }
        }

        for(int i = 0; i <mainList.size(); i++){
            for(int j = 0; j <mainList.size(); j++) {
                if(i==j) continue;
                System.out.println("\n" + Character.toString((char) (i + 65)) + " subtracted from " + Character.toString((char) (j + 65)));
                mainList.get(j).subtract(mainList.get(i)).output();
            }
        }

        LongInteger J = B.add(C);
        mainList.add(J);

        LongInteger K = C.add(D);
        mainList.add(K);

        LongInteger L = I.add(I);
        mainList.add(L);

        LongInteger M = A.add(I);
        mainList.add(M);

        LongInteger N = B.add(K);
        mainList.add(N);

        LongInteger O = A.subtract(D);
        mainList.add(O);

        LongInteger P = C.subtract(D);
        mainList.add(P);

        LongInteger Q = D.subtract(C);
        mainList.add(Q);

        LongInteger R = L.subtract(L);
        mainList.add(R);

        LongInteger S = P.subtract(O);
        mainList.add(S);

        LongInteger T = N.subtract(Q);
        mainList.add(T);
        
        for(int i = 9; i<mainList.size(); i++){
            System.out.println("\n" + Character.toString((char) (i + 65)));
            mainList.get(i).output();
        }
        
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++) {
                if(i==j) continue;
                System.out.println("\n" + Character.toString((char) (i + 65)) + " multiplied by " + Character.toString((char) (j + 65)));
                mainList.get(i).multiply(mainList.get(j)).output();
            }
        }
        
        for(int i = 0; i < 8; i++){
            System.out.println("\n" + Character.toString((char) (i + 65)) + " to 5th Power");
            mainList.get(i).power(5).output();
            System.out.println("\n" + Character.toString((char) (i + 65)) + " to 10th Power");
            mainList.get(i).power(10).output();
            System.out.println("\n" + Character.toString((char) (i + 65)) + " to 25th Power");
            mainList.get(i).power(25).output();
            System.out.println("\n" + Character.toString((char) (i + 65)) + " to 31th Power");
            mainList.get(i).power(31).output();
        }
        
        LongInteger U = A.multiply(D);
        mainList.add(U);

        LongInteger V = B.multiply(C);
        mainList.add(V);

        LongInteger W = D.multiply(D);
        mainList.add(W);

        LongInteger X = O.multiply(I);
        mainList.add(X);

        LongInteger Y = J.multiply(P);
        mainList.add(Y);

        LongInteger Z = M.multiply(N);
        mainList.add(Z);
        
        for(int i = 20; i<mainList.size(); i++){
            System.out.println("\n" + Character.toString((char) (i + 65)));
            mainList.get(i).output();
        }
    }
}
