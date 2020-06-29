package sample;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.*;

public class AFD {

    private String alfabeto;
    private final ArrayList<Map<String, Integer>> graph;
    private final Set<Integer> estadosFinais;

    public AFD(String alfabeto, ArrayList<Map<String, Integer>> graph, Set<Integer> estadosFinais) {
        this.alfabeto = alfabeto;
        this.graph = graph;
        this.estadosFinais = estadosFinais;
    }

    public void printaGrafo() {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + ": ");
            for (String s : graph.get(i).keySet()) {
                System.out.print(s + ":" + graph.get(i).get(s) + " ");
            }
            System.out.println();
        }
    }

    public boolean executa(String palavra) {
        boolean terminouPalavra = true;
        int estado = 0;
        for (int i = 0; i < palavra.length(); i++) {
            System.out.println(estado);

            String transicao = String.valueOf(palavra.charAt(i));
            if (!graph.get(estado).containsKey(transicao)) {
                terminouPalavra = false;
                break;
            }
            estado = graph.get(estado).get(transicao);
        }

        return terminouPalavra && estadosFinais.contains(estado);
    }

    public void geraPng() {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            if (estadosFinais.contains(i)) nodes.add(node("q" + i).with(Label.nodeName(), Shape.DOUBLE_CIRCLE));
            else nodes.add(node("q" + i).with(Label.nodeName()));
        }

        Graph g = graph("MdsLfa").directed();
        for (int i = 0; i < graph.size(); i++) {
            for (String k : graph.get(i).keySet()) {
                g = g.with(nodes.get(i).link(to(nodes.get(graph.get(i).get(k))).with(Label.of(k))));
            }
        }

        try {
            Graphviz.fromGraph(g).width(400).render(Format.PNG).toFile(new File("graph.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
