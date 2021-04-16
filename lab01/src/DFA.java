import java.util.ArrayList;
import java.util.TreeSet;

class DFA {
    ArrayList<TreeSet<Integer>> dStates;
    ArrayList<Character> alphabet;
    TreeSet<Integer> q0;
    ArrayList<TreeSet<Integer>> f;
    ArrayList<TreeSet<Integer>[]> dTran;

    ArrayList<TreeSet<Integer>> getdStates() {
        return dStates;
    }

    ArrayList<Character> getAlphabet() {
        return alphabet;
    }

    TreeSet<Integer> getQ0() {
        return q0;
    }

    ArrayList<TreeSet<Integer>> getF() {
        return f;
    }

    ArrayList<TreeSet<Integer>[]> getdTran() {
        return dTran;
    }

    DFA(ArrayList<Character> alphabet, TreeSet<Integer> q0){
        dStates = new ArrayList<>();
        dStates.add(q0);
        this.alphabet = alphabet;
        this.q0 = q0;
        f = new ArrayList<>();
        dTran = new ArrayList<>();
    }

    DFA(){

    }

    public void getDTran(ArrayList<Followpos> followpos){
        for (int i = 0; i < dStates.size(); i++){
            TreeSet[] s = new TreeSet[alphabet.size()];
            for (int j = 0; j < s.length; j++) {
                s[j] = new TreeSet<Integer>();
            }
            for (int j: dStates.get(i)) {
                char a = followpos.get(j - 1).label;
                if (a == '#'){
                    f.add(dStates.get(i));
                }
                else {
                    int k = alphabet.indexOf(a);
                    s[k].addAll(followpos.get(j - 1).followpos);
                }
            }
            for (TreeSet ts: s) {
                if (!dStates.contains(ts) && !ts.isEmpty())
                    dStates.add(ts);
            }
            dTran.add(s);
        }
    }

}
