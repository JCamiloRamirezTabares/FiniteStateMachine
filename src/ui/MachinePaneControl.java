package ui;

import java.io.IOException;
import java.util.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.FiniteStateMachine;
import model.MealyMachine;
import model.MooreMachine;

public class MachinePaneControl {

    @FXML
    private TableView<TableRow> fsm_table;
    @FXML
    private TableView<TableRow> fsminimize_table;

    private Stage mainStage;
    private FSMGUI api;

    //Contructor
    public MachinePaneControl(Stage m, FSMGUI a){
        mainStage = m;
		api = a;
    }

    public void start() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MachinePane.fxml"));
		fxmlLoader.setController(this);
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);

		mainStage.setScene(scene);
		mainStage.setTitle("FINITE STATE MACHINE");
		mainStage.show();
    }

    @FXML
    public void minimize(ActionEvent event) {
		initializeMinimizeMachine();
    }

    @FXML
    void connectedMachine(ActionEvent event) {
        initializeConnectedMachine();
    }

    public void initializeConnectedMachine(){
		FiniteStateMachine fsm = api.getMachine().getConectedFSM();

		String type = "";
        if(fsm instanceof MealyMachine){type = "Mealy";} else{type = "Moore";};
        List<String> states = fsm.getStatesList();
        List<String> inputs = fsm.getInputs();
        List<TableRow> rows = new ArrayList<>();

        HashMap<String, HashMap<String, String>> ftransition = fsm.getTransitions();
        
        HashMap<String, HashMap<String, String>> outMealy = new HashMap<>();
        HashMap<String, String> outMoore = new HashMap<>();
        
        if(type.equals("Mealy")){outMealy = ((MealyMachine) fsm).getFOutputs();} else{outMoore = ((MooreMachine) fsm).getFOutputs();}

        for(String st: states){
            List<String> body = new ArrayList<>();
            
            HashMap<String, String> tr = ftransition.get(st);
            HashMap<String, String> outM = outMealy.get(st);


            for(int i = 0; i < inputs.size(); i++){
                body.add(tr.get(inputs.get(i)));
                if(type.equals("Mealy")){
                    body.add(outM.get(inputs.get(i)));
                }
            }
            if(type.equals("Moore")){
                body.add(outMoore.get(st));
            }

            TableRow tbr = new TableRow(st, body);
            rows.add(tbr);
        }

		//Se limpia la tabla por si habian columnas anteriormente
        fsminimize_table.getColumns().clear();

		//Crea las columnas o encabezados de cada columna
		addColumnsMachine(type, inputs, fsminimize_table);

		//Agrega cada una de las filas:
		ObservableList<TableRow> observableList= FXCollections.observableArrayList(rows);
		fsminimize_table.setItems(observableList);
	}


	public void initializeMinimizeMachine(){
		FiniteStateMachine fsm = api.getMachine().minimize();

		String type = "";
        if(fsm instanceof MealyMachine){type = "Mealy";} else{type = "Moore";};
        List<String> states = fsm.getStatesList();
        List<String> inputs = fsm.getInputs();
        List<TableRow> rows = new ArrayList<>();

        HashMap<String, HashMap<String, String>> ftransition = fsm.getTransitions();
        
        HashMap<String, HashMap<String, String>> outMealy = new HashMap<>();
        HashMap<String, String> outMoore = new HashMap<>();
        
        if(type.equals("Mealy")){outMealy = ((MealyMachine) fsm).getFOutputs();} else{outMoore = ((MooreMachine) fsm).getFOutputs();}

        for(String st: states){
            List<String> body = new ArrayList<>();
            
            HashMap<String, String> tr = ftransition.get(st);
            HashMap<String, String> outM = outMealy.get(st);


            for(int i = 0; i < inputs.size(); i++){
                body.add(tr.get(inputs.get(i)));
                if(type.equals("Mealy")){
                    body.add(outM.get(inputs.get(i)));
                }
            }
            if(type.equals("Moore")){
                body.add(outMoore.get(st));
            }

            TableRow tbr = new TableRow(st, body);
            rows.add(tbr);
        }

		//Se limpia la tabla por si habian columnas anteriormente
        fsminimize_table.getColumns().clear();

		//Crea las columnas o encabezados de cada columna
		addColumnsMachine(type, inputs, fsminimize_table);

		//Agrega cada una de las filas:
		ObservableList<TableRow> observableList= FXCollections.observableArrayList(rows);
		fsminimize_table.setItems(observableList);
	}

    public void initializeMachine(){
        //Lectura de la interfaz grafica
        FiniteStateMachine fsm = api.getMachine();
        String type = "";
        if(fsm instanceof MealyMachine){type = "Mealy";} else{type = "Moore";};
        List<String> states = fsm.getStatesList();
        List<String> inputs = fsm.getInputs();
        List<TableRow> rows = new ArrayList<>();

        HashMap<String, HashMap<String, String>> ftransition = fsm.getTransitions();
        
        HashMap<String, HashMap<String, String>> outMealy = new HashMap<>();
        HashMap<String, String> outMoore = new HashMap<>();
        
        if(type.equals("Mealy")){outMealy = ((MealyMachine) fsm).getFOutputs();} else{outMoore = ((MooreMachine) fsm).getFOutputs();}

        for(String st: states){
            List<String> body = new ArrayList<>();
            
            HashMap<String, String> tr = ftransition.get(st);
            HashMap<String, String> outM = outMealy.get(st);


            for(int i = 0; i < inputs.size(); i++){
                body.add(tr.get(inputs.get(i)));
                if(type.equals("Mealy")){
                    body.add(outM.get(inputs.get(i)));
                }
            }
            if(type.equals("Moore")){
                body.add(outMoore.get(st));
            }

            TableRow tbr = new TableRow(st, body);
            rows.add(tbr);
        }

		//Se limpia la tabla por si habian columnas anteriormente
        fsm_table.getColumns().clear();

		//Crea las columnas o encabezados de cada columna
		addColumnsMachine(type, inputs, fsm_table);

		//Agrega cada una de las filas:
		ObservableList<TableRow> observableList= FXCollections.observableArrayList(rows);
		fsm_table.setItems(observableList);

    }

    //Metodos Auxiliares
	//Se encarga de poner los encabezados
	private void addColumnsMachine(String type, List<String> inputs, TableView<TableRow> t) {

		TableColumn<TableRow, String> statesCol = new TableColumn<>("state (q)");
		statesCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("state"));
		t.getColumns().add(statesCol);

		if(type.equals("Mealy")) {
			addColumnsMealy(inputs, t);
		}else {
			addColumnsMoore(inputs, t);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addColumnsMealy(List<String> inputs, TableView<TableRow> table) {	
		//Agregar columnas de las funciones f y g 
		
		for(int i = 0; i < inputs.size(); i++) {
			final int j = (i%2==0)?i:i+1;
            final int k = j+1;
			TableColumn<TableRow, String> f = new TableColumn<>("f(q,"+inputs.get(i)+")");
			TableColumn<TableRow, String> g = new TableColumn<>("g(q,"+inputs.get(i)+")");
			f.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getBody().get(j))));
			g.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getBody().get(k))));

			table.getColumns().add(f);
			table.getColumns().add(g);
		}		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addColumnsMoore(List<String> inputs, TableView<TableRow> table) {		
		//Agregar columnas de las funciones f
		for(int i=0; i < inputs.size();i++) {
			final int j = i;
			TableColumn<TableRow, String> f = new TableColumn<>("f(q,"+inputs.get(i)+")");
			f.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getBody().get(j))));

			table.getColumns().add(f);
		}		

		//Agregar columna de la funcion h
		TableColumn<TableRow, String> h = new TableColumn<>("h(q)");
		h.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getBody().get(cellData.getValue().getBody().size()-1))));
		table.getColumns().add(h);
	}


}
