import java.util.ArrayList;
import java.util.Stack;

public class ExpressionTree {

    public ArrayList<Followpos> getFollowpos() {
        return followpos;
    }

    private ArrayList<Followpos> followpos;

    public Node getRoot() {
        return root;
    }

    private Node root;

    ExpressionTree(){
        followpos = new ArrayList<Followpos>();
    }

    public void constructTree(ArrayList<Character> regex){
        Stack<Node> st = new Stack<Node>();
        Node t, t1, t2;
        int i = 0;

        for (Character c: regex) {

            if (!Regex.isOperator(c)) {
                t = new Node(c);
                t.nullable = false;
                i++;
                t.firstpos.add(i);
                t.lastpos.add(i);
                st.push(t);
                followpos.add(new Followpos(c));
            }

            else if (Regex.isUnary(c)){
                t = new Node(c);
                t1 = st.pop();
                t.left = t1;
                t.nullable = nullable(t);
                firstpos(t);
                lastpos(t);
                followpos(t);
                st.push(t);
            }

            else {
                t = new Node(c);

                t1 = st.pop();
                t2 = st.pop();
                t.right = t1;
                t.left = t2;
                t.nullable = nullable(t);
                firstpos(t);
                lastpos(t);
                followpos(t);
                st.push(t);
            }
        }

        root = st.peek();
        st.pop();
    }

    boolean nullable(Node node){
        if (node.value == '|'){
            return node.left.nullable || node.right.nullable;
        }
        else if (node.value == '.'){
            return node.left.nullable && node.right.nullable;
        }
        else
            return true;
    }

    void firstpos(Node node){
        if (node.value == '|'){
            node.firstpos.addAll(node.left.firstpos);
            node.firstpos.addAll(node.right.firstpos);
        }
        else if (node.value == '.'){
            if (node.left.nullable){
                node.firstpos.addAll(node.left.firstpos);
                node.firstpos.addAll(node.right.firstpos);
            }
            else
                node.firstpos.addAll(node.left.firstpos);
        }
        else
            node.firstpos.addAll(node.left.firstpos);
    }

    void lastpos (Node node){
        if (node.value == '|'){
            node.lastpos.addAll(node.left.lastpos);
            node.lastpos.addAll(node.right.lastpos);
        }
        else if (node.value == '.'){
            if (node.right.nullable){
                node.lastpos.addAll(node.left.lastpos);
                node.lastpos.addAll(node.right.lastpos);
            }
            else
                node.lastpos.addAll(node.right.lastpos);
        }
        else
            node.lastpos.addAll(node.left.lastpos);
    }

    void followpos(Node node){
        if (node.value == '.'){
            for (int i: node.left.lastpos) {
                followpos.get(i-1).followpos.addAll(node.right.firstpos);
            }
        }
        else if (node.value == '*'){
            for (int i: node.lastpos) {
                followpos.get(i-1).followpos.addAll(node.firstpos);
            }
        }
    }

}

