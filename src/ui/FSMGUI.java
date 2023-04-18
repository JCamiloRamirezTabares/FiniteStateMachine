package ui;

import java.io.IOException;
import java.util.*;

import javafx.stage.Stage;
import model.FiniteStateMachine;
import model.MealyMachine;
import model.MooreMachine;

public class FSMGUI {

    private FiniteStateMachine machine;
    private MainPaneControl mainMenu;
    private MachinePaneControl machineMenu;
    
    public FSMGUI(Stage stage){
        mainMenu = new MainPaneControl(stage, this);
        machineMenu = new MachinePaneControl(stage, this);
    }

    public void createMealyMachine(Set<String> states, List<String> inputs, List<String> outputs, HashMap<String, HashMap<String, String>> ftransitions, HashMap<String, HashMap<String, String>> fouts){
        machine = new MealyMachine(states, inputs, outputs, ftransitions, fouts);
    }

    public void createMooreMachine(Set<String> states, List<String> inputs, List<String> outputs, HashMap<String, HashMap<String, String>> ftransitions, HashMap<String, String> fouts){
        machine = new MooreMachine(states, inputs, outputs, ftransitions, fouts);
    }



    //Menu's
    public void mainMenu(){
        try {
            mainMenu.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void machineMenu(){
        try {
            machineMenu.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void initializeTable(){
        machineMenu.initializeMachine();
    }

    public void initializeMinimizeTable(){
        machineMenu.initializeMachine();
        machineMenu.initializeMinimizeMachine();
    }

    public FiniteStateMachine getMachine(){
        return machine;
    }


}
