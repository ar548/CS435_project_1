public class UtilityOperations {
    public static int overFlow(int t) { return t / 10000; }

    public static int underFlow(int t) { return t%10000; }

    public static int digits(Integer t) { return t.toString().length(); }
}
