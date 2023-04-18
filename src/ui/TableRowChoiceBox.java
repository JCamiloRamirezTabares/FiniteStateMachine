package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ComboBox;

public class TableRowChoiceBox {
    private String state;
    private List<ComboBox<String>> transitions;
    private List<ComboBox<String>> outputs;

    public TableRowChoiceBox(String s, List<ComboBox<String>> t, List<ComboBox<String>> o) {
        state = s;
        transitions = t;
        outputs = o;
    }

    public TableRowChoiceBox(String s) {
        state = s;
        transitions = new ArrayList<>();
        outputs =  new ArrayList<>();
    }



    public String getState() {
        return state;
    }

    public void setState(String s) {
        state = s;
    }

    public List<ComboBox<String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<ComboBox<String>> f) {
        transitions = f;
    }

    public List<ComboBox<String>> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ComboBox<String>> o) {
        outputs = o;
    }

}
