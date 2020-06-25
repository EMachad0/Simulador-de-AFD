package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML public TextField fieldAlfabeto;
    @FXML public TextField fieldNumState;
    @FXML public TextField fieldPalavra;
    @FXML public TextField fieldEstadosFinais;
    @FXML public Button btnTabela;
    @FXML public Button btnExecuta;
    @FXML public TableView<Integer> myTable;
    @FXML public TableColumn<Integer, String> indices;
    @FXML public ImageView img;
    @FXML public Label labelPalavra;


    private String alfa;
    private int numEstados;

    ArrayList<Map<String, Integer>> m;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnTabela.setOnAction(event -> {
            alfa = fieldAlfabeto.getText();
            numEstados = Integer.parseInt(fieldNumState.getText());

            m = new ArrayList<>();
            for (int i = 0; i < numEstados; i++) m.add(new TreeMap<>());

            System.out.println(numEstados);
            System.out.println(alfa);

            myTable.setEditable(true);

            for (int i = 0; i < numEstados; i++) myTable.getItems().add(i);

            List<String> est = new ArrayList<>();
            for (int j = 0; j < numEstados; j++) est.add("q" + j);
            indices.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(est.get(rowIndex));
            });

            for (int i = 0; i < alfa.length(); i++) {
                TableColumn<Integer, String> column = new TableColumn<>(String.valueOf(alfa.charAt(i)));
                column.setCellValueFactory(cellData -> new ReadOnlyStringWrapper());
                column.setCellFactory(TextFieldTableCell.forTableColumn());

                column.setOnEditCommit(evt -> {
                    m.get(evt.getRowValue()).put(evt.getTableColumn().getText(), Integer.parseInt(evt.getNewValue()));
                });

                myTable.getColumns().add(column);
            }
        });

        btnExecuta.setOnAction(event -> {
            String palavra = fieldPalavra.getText();

            char[] s = fieldEstadosFinais.getText().toCharArray();

            Set<Integer> estadosFinais = new TreeSet<>();
            for (char c : s) {
                estadosFinais.add((int) c - '0');
                System.out.println((int) c - '0');
            }

            AFD afd = new AFD(alfa, m, estadosFinais);
            if (afd.executa(palavra)) labelPalavra.setText("Palavra Aceita");
            else labelPalavra.setText("Palavra Não Aceita");

            afd.geraPng();

//            Image image = new Image("https://images.unsplash.com/photo-1579546929518-9e396f3cc809?ixlib=rb-1.2.1&w=1000&q=80");
            Image image = new Image("file:graph.png");
            img.setImage(image);
            img.setFitWidth(700);
            img.setPreserveRatio(true);
            img.setSmooth(true);
            img.setCache(true);
        });
    }
}
