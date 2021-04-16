import java.util.ArrayList;
import java.util.Stack;

public class Regex {

    public static boolean isOperator(char c) {
        return c == '(' || c == ')' || c == '*' || c == '.' || c == '|';
    }

    public static boolean isUnary(char c) {
        return c == '*';
    }

    public static int getPriority(char c){
        if (c == '*'){
            return 1;
        }
        else if (c == '.'){
            return 2;
        }
        else if (c == '|'){
            return 3;
        }
        return 4;
    }

    public static void addConcatenation(ArrayList<Character> regex){
        for (int i = 0; i < regex.size() - 1; i++) {
            if (!isOperator(regex.get(i)) && (!isOperator(regex.get(i + 1)) || regex.get(i + 1) == '(')) {
                regex.add(i + 1, '.');
            } else if ((regex.get(i) == ')' || regex.get(i) == '*')
                    && !isOperator(regex.get(i + 1))) {
                regex.add(i + 1, '.');
            } else if ((regex.get(i) == ')') && (regex.get(i + 1) == '(')) {
                regex.add(i + 1, '.');
            }
        }
    }

    public static ArrayList<Character> postfix(ArrayList<Character> regex){
        Stack<Character> s = new Stack<>();
        ArrayList<Character> result = new ArrayList<>();
        for (Character c : regex){
            if (!isOperator(c)){
                result.add(c);
            }
            else if (c == '('){
                s.push(c);
            }
            else if (c == ')'){
                while (s.peek() != '('){
                    result.add(s.pop());
                }
                s.pop();
            }
            else if (isOperator(c)){
                while (!s.empty() && getPriority(c) >= getPriority(s.peek())){
                    result.add(s.pop());
                }
                s.push(c);
            }
        }
        while (!s.empty()){
            result.add(s.pop());
        }
        return result;
    }
}
