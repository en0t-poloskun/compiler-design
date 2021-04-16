import java.util.TreeSet;

public class Followpos {
    char label;
    TreeSet<Integer> followpos;

    Followpos(char label){
        this.label = label;
        followpos = new TreeSet<Integer>();
    }
}
