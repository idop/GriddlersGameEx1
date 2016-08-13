/**
 * Created by ido on 12/08/2016.
 */
public class Program {
    private static final String xmlFilePath = "C:\\Temp\\gridler-mini-example-master.xml";

    public static void main(String[] args) {
        doSomething();
    }

    private static void doSomething() {
        Boolean[] isRowDefined = new Boolean[15];
        for (int i = 0; i < isRowDefined.length; i++) {
            System.out.println(isRowDefined[i]);
            isRowDefined[i] = true;
            System.out.println(isRowDefined[i]);
        }
    }
}
