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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MainPaneControl {
    
    @FXML
    private TextField alphInput_txtField;
    @FXML
    private TextField alphOutput_txtField;
    @FXML
    private ChoiceBox<String> typeMachine_chbox;
    @FXML
    private Spinner<Integer> statesnumber_spinner;
    @FXML
    private Button create_bttn;
    @FXML
    private TableView<TableRowChoiceBox> fsm_table;

    private Stage mainStage;
	private FSMGUI api;

    //Contructor
    public MainPaneControl(Stage m, FSMGUI a){
        mainStage = m;
		api = a;
    }

    public void initialize() {
        typeMachine_chbox.getItems().addAll("Mealy", "Moore");
        SpinnerValueFactory<Integer> valueFactory= new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 21, 1);
		statesnumber_spinner.setValueFactory(valueFactory);
    }

    public void start() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPane.fxml"));
		fxmlLoader.setController(this);
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);

		mainStage.setScene(scene);
		mainStage.setTitle("FINITE STATE MACHINE GENERATOR");
		mainStage.show();
    }

    @FXML
    void designMachine(ActionEvent event) {
		//Lectura de la interfaz grafica
        String tm = typeMachine_chbox.getValue();
        List<String> alphI = Arrays.asList(alphInput_txtField.getText().split(","));
        List<String> alphO = Arrays.asList(alphOutput_txtField.getText().split(","));
        int numberStates = statesnumber_spinner.getValue();
        
		List<String> statesTransitions = new ArrayList<>();
		List<TableRowChoiceBox> fsmRows = new ArrayList<>();

        //Se crean los distintos estados en formato String
        for(int i = 0; i < numberStates; i++){
            statesTransitions.add("q(" + i + ")");
        }

		//Se crea el numero de filas que tendra la tabla
		for(String s: statesTransitions){
			
			List<ComboBox<String>> transitions = new ArrayList<>();
			List<ComboBox<String>> outputs = new ArrayList<>();
			for(int i = 0; i < alphI.size(); i++){
				transitions.add(new ComboBox<>(FXCollections.observableArrayList(statesTransitions)));
			}

			if(tm.equals("Mealy")){
				for(int i = 0; i < alphI.size(); i++){
					outputs.add(new ComboBox<>(FXCollections.observableArrayList(alphO)));
				}
			} else{
				outputs.add(new ComboBox<>(FXCollections.observableArrayList(alphO)));
			}
			
			fsmRows.add(new TableRowChoiceBox(s, transitions, outputs));
		}

		//Se limpia la tabla por si habian columnas anteriormente
        fsm_table.getColumns().clear();

		//Crea las columnas o encabezados de cada columna
		addColumnsMachine(tm, alphI, fsm_table);

		//Agrega cada una de las filas:
		ObservableList<TableRowChoiceBox> observableList= FXCollections.observableArrayList(fsmRows);
		fsm_table.setItems(observableList);

		//Muestra la tabla (Por defecto esta no visible)
        fsm_table.setVisible(true);
		create_bttn.setVisible(true);
    }

    @FXML
    void createMachine(ActionEvent event) {
		if(isMissingValue()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error de validacion");
			alert.setHeaderText(null);
			alert.setContentText("Por favor, llene todos los campos para crear la maquina");
			alert.showAndWait();
		} else{
			createMachine();
			api.machineMenu();
			api.initializeTable();
		}
    }

	//Metodos Auxiliares
	//Se encarga de poner los encabezados
	private void addColumnsMachine(String type, List<String> inputs, TableView<TableRowChoiceBox> t) {

		TableColumn<TableRowChoiceBox, ComboBox<String>> statesCol = new TableColumn<>("state (q)");
		statesCol.setCellValueFactory(new PropertyValueFactory<TableRowChoiceBox, ComboBox<String>>("state"));
		t.getColumns().add(statesCol);

		if(type.equals("Mealy")) {
			addColumnsMealy(inputs, t);
		}else {
			addColumnsMoore(inputs, t);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addColumnsMealy(List<String> inputs, TableView<TableRowChoiceBox> table) {	
		//Agregar columnas de las funciones f y g 
		
		for(int i = 0; i < inputs.size(); i++) {
			final int j = i;
			TableColumn<TableRowChoiceBox, ComboBox<String>> f = new TableColumn<>("f(q,"+inputs.get(i)+")");
			TableColumn<TableRowChoiceBox, ComboBox<String>> g = new TableColumn<>("g(q,"+inputs.get(i)+")");
			f.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getTransitions().get(j))));
			g.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getOutputs().get(j))));

			table.getColumns().add(f);
			table.getColumns().add(g);
		}		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addColumnsMoore(List<String> inputs, TableView<TableRowChoiceBox> table) {		
		//Agregar columnas de las funciones f
		for(int i=0; i < inputs.size();i++) {
			final int j = i;
			TableColumn<TableRowChoiceBox, ComboBox<String>> f = new TableColumn<>("f(q,"+inputs.get(i)+")");
			f.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getTransitions().get(j))));

			table.getColumns().add(f);
		}		

		//Agregar columna de la funcion h
		TableColumn<TableRowChoiceBox, ComboBox<String>> h = new TableColumn<>("h(q)");
		h.setCellValueFactory(cellData -> new SimpleObjectProperty((cellData.getValue().getOutputs().get(0))));
		table.getColumns().add(h);
	}

	private boolean isMissingValue(){

		boolean isMissing = false;

		for(TableRowChoiceBox r: fsm_table.getItems()){
			for(ComboBox<String> c: r.getTransitions()){
				if(c.getValue() == null){
					return true;
				}
			}
			for(ComboBox<String> c: r.getOutputs()){
				if(c.getValue() == null){
					return true;
				}
			}
		}

		return isMissing;
	}

	private void createMachine(){
		//Lectura de la interfaz grafica
        String tm = typeMachine_chbox.getValue();
        List<String> alphI = Arrays.asList(alphInput_txtField.getText().split(","));
        List<String> alphO = Arrays.asList(alphOutput_txtField.getText().split(","));
        int numberStates = statesnumber_spinner.getValue();
        
		Set<String> states = new HashSet<>();

        //Se crean los distintos estados en formato String
        for(int i = 0; i < numberStates; i++){
            states.add("q(" + i + ")");
        }

		//Se crea la funcion transicion y salida segun el caso
		HashMap<String, HashMap<String, String>> ftransition = new HashMap<>(); 
		HashMap<String, HashMap<String, String>> foutputMealy = new HashMap<>();
		HashMap<String, String> foutputMoore = new HashMap<>();

		//Se contruye la funcion de transicion y salida
		for(TableRowChoiceBox r: fsm_table.getItems()){

			HashMap<String, String> t = new HashMap<>();
			for(int i = 0; i < alphI.size(); i++){
				t.put(alphI.get(i), r.getTransitions().get(i).getValue());
			}

			if(tm.equals("Mealy")){
				HashMap<String, String> outMealy = new HashMap<>();
				for(int i = 0; i < alphI.size(); i++){
					outMealy.put(alphI.get(i), r.getOutputs().get(i).getValue());
				}
				foutputMealy.put(r.getState(), outMealy);
			} else{
				foutputMoore.put(r.getState(), r.getOutputs().get(0).getValue());
			}

			ftransition.put(r.getState(), t);
		}

		//Se crea la maquina en la api segun el tipo
		if(tm.equals("Mealy")){
			api.createMealyMachine(states, alphI, alphO, ftransition, foutputMealy);
		} else{
			api.createMooreMachine(states, alphI, alphO, ftransition, foutputMoore);
		}
	}

}