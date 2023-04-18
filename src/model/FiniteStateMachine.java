package model;

import java.util.*;

public abstract class FiniteStateMachine{

    /*Una maquina de estado se compone por
     * Un conjunto finito de estados { q(0), q(1), q(2), ..., q(n) }
     * Un alfabeto finito de simbolos de entrada
     * Un alfabeto finito de simbolos de salida
     * Una funcion de transicion de estados
     * Una funcion de salida
     * Un estado inicial { q(0) }
    */

    protected Set<String> states;
    protected List<String> alphabetInput;
    protected List<String> alphabetOutput;

    protected int initialState;
    //Dado un estado (j) retorna un conjunto de datos (k, v) donde k es la entrada y v es el estado transicion.
    protected HashMap<String, HashMap<String, String>> functionTransition;

    private ArrayList<ArrayList<ArrayList<String>>> partitions;
    private ArrayList<ArrayList<String>> finalPartition;

    //Constructor
    public FiniteStateMachine(Set<String> statesnumber, List<String> input, List<String> output, HashMap<String, HashMap<String, String>> ftransition){
        states = statesnumber;
        alphabetInput = input;
        alphabetOutput = output;
        functionTransition = ftransition;
        initialState = 0;
    }


    public FiniteStateMachine minimize() {
        return null;
    }

    public FiniteStateMachine getConectedFSM(){
        return null;
    }

    public List<PartitionBlock> initialPartition() { return null;}

    //Getters & Setters
    public List<String> getInputs(){
        return alphabetInput;
    }

    public List<String> getOutputs(){
        return alphabetOutput;
    }

    public List<String> getStatesList(){
        List<String> st = new ArrayList<>();
        for(String s: states){
            st.add(s);
        }

        return st;
    }

    public Set<String> getStates(){
        return states;
    }

    public HashMap<String, HashMap<String, String>> getTransitions(){
        return functionTransition;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getPartitions() {
		return partitions;
	}
	
	public void setPartitions(ArrayList<ArrayList<ArrayList<String>>> partitions) {
		this.partitions = partitions;
	}
	
	public ArrayList<ArrayList<String>> getFinalPartition() {
		return finalPartition;
	}

	public void setFinalPartition(ArrayList<ArrayList<String>> finalPartition) {
		this.finalPartition = finalPartition;
	}


}
