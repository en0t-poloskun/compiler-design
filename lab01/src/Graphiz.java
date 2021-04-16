import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

public class Graphiz {
    public static void drawFA(DFA dfa, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("digraph state_machine {\n    rankdir = LR;\n    node [shape = doublecircle];");
        String text = "";
        for (TreeSet ts : dfa.getF()) {
            text = text + " " + ts.toString().replaceAll("[ ,\\[\\]]+", "");
        }
        text = text + ";\n";
        writer.write(text);
        writer.write("    node [shape = oval];\n");
        text = "    start -> " + dfa.getQ0().toString().replaceAll("[ ,\\[\\]]+", "") + ";\n";
        writer.write(text);
        for (int i = 0; i < dfa.getdStates().size(); i++) {
            for (int j = 0; j < dfa.getAlphabet().size(); j++) {
                if (!dfa.getdTran().get(i)[j].isEmpty()) {
                    text = "    ";
                    text = text + dfa.getdStates().get(i).toString().replaceAll("[ ,\\[\\]]+", "") + " -> "
                            + dfa.getdTran().get(i)[j].toString().replaceAll("[ ,\\[\\]]+", "") +
                            " [label = \"" + dfa.getAlphabet().get(j) + "\"];\n";
                    writer.write(text);
                }
            }
        }
        writer.write("}");
        writer.flush();
    }
}
