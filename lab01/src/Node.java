import java.util.TreeSet;

class Node {
    char value;
    Node left, right;
    boolean nullable;
    TreeSet<Integer> firstpos, lastpos;

    Node(char item) {
        value = item;
        left = right = null;
        firstpos = new TreeSet<Integer>();
        lastpos = new TreeSet<Integer>();
    }
}
