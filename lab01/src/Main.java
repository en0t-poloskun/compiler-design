import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        System.out.print("Введите регулярное выражение: ");
        char[] regex = in.nextLine().toCharArray();
        TreeSet<Character> alphabet = new TreeSet<>();
        ArrayList<Character> arrayC = new ArrayList<>();
        for (char c : regex) {
            arrayC.add(c);
            if (!Regex.isOperator(c)) {
                alphabet.add(c);
            }
        }
        arrayC.add('#');
        Regex.addConcatenation(arrayC);
        arrayC = Regex.postfix(arrayC);
        System.out.println(arrayC);
        ExpressionTree et = new ExpressionTree();
        et.constructTree(arrayC);
        DFA dfa = new DFA(new ArrayList<Character>(alphabet), et.getRoot().firstpos);
        dfa.getDTran(et.getFollowpos());
        Graphiz.drawFA(dfa, "dfa.dot");
        MFA mfa = new MFA();
        mfa.minimize(dfa);
        Graphiz.drawFA(mfa, "mfa.dot");
        while (true){
            System.out.print("Введите входную цепочку:\n");
            char[] text = in.nextLine().toCharArray();
            if (mfa.match(text)){
                System.out.println("Входная цепочка допускается автоматом");
            }
            else {
                System.out.println("Входная цепочка не допускается автоматом");
            }
        }
    }
}
