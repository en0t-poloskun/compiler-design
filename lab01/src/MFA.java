import java.util.*;

public class MFA extends DFA{

    MFA(){
        super();

    }

    public void minimize(DFA dfa){
        ArrayList[][] backEges = getBackEges(dfa.dStates, dfa.alphabet, dfa.dTran);
        int[] reachable = new int[dfa.getdStates().size()];
        reachable = bfs(dfa.dStates, dfa.dTran, dfa.q0, reachable);
        boolean[][] marked = buildTable(dfa.dStates, dfa.f, backEges, dfa.alphabet);
        int[] component = getEquivalenceClasses(reachable, marked);
        initializeMFA(component, dfa);
    }

    public void initializeMFA(int[] component, DFA dfa){
        this.dStates = new ArrayList<>();
        this.alphabet = dfa.alphabet;
        this.dTran = new ArrayList<>();
        int startState = 0;
        TreeSet<Integer> finalStates = new TreeSet<>();
        for (int i = 0; i <= component[component.length - 1]; i++){
            this.dStates.add(new TreeSet<>());
            this.dTran.add(new TreeSet[this.alphabet.size()]);
        }

        for (int i = 0; i < component.length; i++){
            this.dStates.get(component[i]).addAll(dfa.dStates.get(i));
            if (dfa.dStates.get(i).equals(dfa.q0)){
                startState = component[i];
            }
            if (dfa.f.contains(dfa.dStates.get(i))){
                finalStates.add(component[i]);
            }
            for (int j = 0; j < this.alphabet.size(); j++){
                if (this.dTran.get(component[i])[j] == null){
                    dTran.get(component[i])[j] = new TreeSet<>();
                }
                if (!dfa.dTran.get(i)[j].isEmpty()) {
                    int c = component[dfa.dStates.indexOf(dfa.dTran.get(i)[j])];
                    this.dTran.get(component[i])[j] = this.dStates.get(c);
                }
            }

        }
        this.q0 = this.dStates.get(startState);
        this.f = new ArrayList<>();
        for (int i: finalStates){
            this.f.add(this.dStates.get(i));
        }
    }


    public int[] getEquivalenceClasses(int[] reachable, boolean[][] marked){
        int[] component = new int[reachable.length];
        Arrays.fill(component, -1);
        for (int i = 0; i < reachable.length; i++){
            if (!marked[0][i]){
                component[i] = 0;
            }
        }
        int componentsCount = 0;
        for (int i = 0; i < reachable.length; i++){
            if (reachable[i] == 0){
                continue;
            }
            if (component[i] == -1){
                componentsCount++;
                component[i] = componentsCount;
                for (int j = i+1; j < reachable.length; j++){
                    if (!marked[i][j]){
                        component[j] = componentsCount;
                    }
                }
            }
        }
        return component;
    }


    public boolean[][] buildTable(ArrayList<TreeSet<Integer>> dStates, ArrayList<TreeSet<Integer>> f,
                           ArrayList<TreeSet>[][] backEges, ArrayList<Character> alphabet){
        Queue queue = new LinkedList();
        boolean[][] marked = new boolean[dStates.size()][dStates.size()];
        for (int i = 0; i < dStates.size(); i++){
            for (int j = 0; j < dStates.size(); j++){
                if (!marked[i][j] && (f.contains(dStates.get(i)) != f.contains(dStates.get(j)))){
                    marked[i][j] = true;
                    marked[j][i] = true;
                    ArrayList pair = new ArrayList();
                    pair.add(dStates.get(i));
                    pair.add(dStates.get(j));
                    queue.add(pair);
                }
            }
        }
        while (!queue.isEmpty()){
            ArrayList pair = (ArrayList) queue.poll();
            for (int i = 0; i < alphabet.size(); i++){
                int u = dStates.indexOf(pair.get(0));
                int v = dStates.indexOf(pair.get(1));
                if (backEges[u][i] != null && backEges[v][i] != null){
                    for (TreeSet r: backEges[u][i]){
                        for (TreeSet s: backEges[v][i]){
                            if (!marked[dStates.indexOf(r)][dStates.indexOf(s)]){
                                marked[dStates.indexOf(r)][dStates.indexOf(s)] = true;
                                marked[dStates.indexOf(s)][dStates.indexOf(r)] = true;
                                pair = new ArrayList();
                                pair.add(r);
                                pair.add(s);
                                queue.add(pair);
                            }
                        }

                    }
                }
            }

        }
        return marked;
    }

    public int[] bfs(ArrayList<TreeSet<Integer>> dStates, ArrayList<TreeSet<Integer>[]> dTran, TreeSet<Integer> q0,
                    int[] reachable){
        Queue queue = new LinkedList();
        queue.add(q0);
        reachable[dStates.indexOf(q0)] = 1;
        while (!queue.isEmpty()){
            TreeSet<Integer> v = (TreeSet<Integer>) queue.poll();
            TreeSet<Integer>[] neighbors = dTran.get(dStates.indexOf(v));
            for (int i = 0; i < neighbors.length; i++){
                if (!neighbors[i].isEmpty()){
                    if (reachable[dStates.indexOf(neighbors[i])] == 0){
                        queue.add(neighbors[i]);
                        reachable[dStates.indexOf(neighbors[i])] = 1;
                    }
                }
            }
        }
        return reachable;
    }

    public ArrayList[][] getBackEges(ArrayList<TreeSet<Integer>> dStates, ArrayList<Character> alphabet,
                            ArrayList<TreeSet<Integer>[]> dTran){
        ArrayList[][] backEges = new ArrayList[dStates.size()][alphabet.size()];
        int k = 0;
        for (TreeSet<Integer>[] s: dTran){
            for (int i = 0; i < s.length; i++){
                if (!s[i].isEmpty()) {
                    int n = dStates.indexOf(s[i]);
                    if (backEges[n][i] == null) {
                        backEges[n][i] = new ArrayList();
                    }
                    backEges[n][i].add(dStates.get(k));
                }
            }
            k++;
        }
        return backEges;

    }

    public boolean match(char[] text){
        TreeSet<Integer> state = this.q0;
        for (char c: text){
            if ((this.alphabet.indexOf(c) == -1) || (this.dStates.indexOf(state) == -1)){
                return false;
            }
            state = this.dTran.get(this.dStates.indexOf(state))[this.alphabet.indexOf(c)];
        }
        return this.f.contains(state);
    }

}
