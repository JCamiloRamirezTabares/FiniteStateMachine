package model;

import java.util.*;

public class MealyMachine extends FiniteStateMachine{

    /*Dado un estado (j) retorna un ? que puede ser:
     * Mealy: Un conjunto de datos (k,v) donde k es la entrada y v es la salida (Depende del estado actual y la entrada)
     * Moore: La salida respectiva del estado (Depende solo del estado actual)
    */
    protected HashMap<String, HashMap<String, String>> functionOutput;

    public MealyMachine(Set<String> statesnumber, List<String> input, List<String> output, HashMap<String, HashMap<String, String>> ftransition, HashMap<String, HashMap<String, String>> foutPut) {
        super(statesnumber, input, output, ftransition);
        functionOutput = foutPut;

    }

    public FiniteStateMachine minimize(){
        FiniteStateMachine conectedMachine = getConectedFSM();

        List<PartitionBlock> initialPartition = createPartition(conectedMachine);
        List<List<PartitionBlock>> partitions = new ArrayList<>();
        partitions.add(initialPartition);
        FiniteStateMachine currentMachine = conectedMachine;
        List<PartitionBlock> currentPartition = initialPartition;

        do{
            currentMachine = pk(currentMachine, currentPartition);
            currentPartition = createPartition(currentMachine);            
            partitions.add(currentPartition);
            
        } while(!isEqualsPartition(partitions.get(partitions.size()-1), partitions.get(partitions.size()-2)));

        return createMinimizeMachine(partitions.get(partitions.size()-1), conectedMachine);
    }


    private FiniteStateMachine pk(FiniteStateMachine fm, List<PartitionBlock> p){
        HashMap<String, HashMap<String, String>> newOutputs = new HashMap<>();

        for(String s: fm.getStates()){
            HashMap<String, String> t = fm.getTransitions().get(s);
            HashMap<String, String> newO =  new HashMap<>();
            for(String input: fm.getInputs()){
                String st = t.get(input);
                boolean sentinel = false;
                for(int i = 0; i < p.size() && !sentinel; i++){
                    if(p.get(i).getStates().contains(st)){
                        sentinel = true;
                        String nOut = i + "";
                        newO.put(input, nOut);
                    }
                }
            }

            newOutputs.put(s, newO);
        }

        return new MealyMachine(fm.getStates(), fm.getInputs(), fm.getOutputs(), fm.getTransitions(), newOutputs);
    }

    private List<PartitionBlock> createPartition(FiniteStateMachine c) {
        List<PartitionBlock> initialP = new ArrayList<>();

        List<String> states = c.getStatesList();

        for(String s: states){
            if(initialP.isEmpty()){
                List<String> st = new ArrayList<>();
                HashMap<String, String> outs = ((MealyMachine) c).getFOutputs().get(s);
                st.add(s);
                String out = "";

                for(String i: c.getInputs()){
                    out += outs.get(i);
                }

                initialP.add(new PartitionBlock(st, out));
            } else{
                boolean sentinel = false;
                HashMap<String, String> outs = ((MealyMachine) c).getFOutputs().get(s);
                String out = "";

                
                for(String i: c.getInputs()){
                    out += outs.get(i);
                }


                for(int i = 0; i < initialP.size() && !sentinel; i++){
                    if(initialP.get(i).getOutput().equals(out)){
                        initialP.get(i).addAState(s);
                        sentinel = true;
                    }
                }
                
                if(sentinel == false){
                    List<String> st = new ArrayList<>();
                    st.add(s);

                    initialP.add(new PartitionBlock(st, out));
                }

            }
        }

        return initialP;
    }

    @Override
    public FiniteStateMachine getConectedFSM() {
        Set<String> newStates = new HashSet<>();
        HashMap<String, HashMap<String, String>> newTransitions = new HashMap<>();
        HashMap<String, HashMap<String, String>> newOutputs = new HashMap<>();
        PriorityQueue<String> q = new PriorityQueue<>();

        q.add("q(0)");
        
        while(q.isEmpty() == false){
            String from = q.poll();
            if(newStates.contains(from) == false){
                newStates.add(from);
                HashMap<String, String> transitions = functionTransition.get(from);
                newTransitions.put(from, transitions);
                newOutputs.put(from, functionOutput.get(from));
                q.addAll(transitions.values());
            }
        }

        FiniteStateMachine connectedFSM = new MealyMachine(newStates, alphabetInput, alphabetOutput, newTransitions, newOutputs);
        return connectedFSM;
    }

    private FiniteStateMachine createMinimizeMachine(List<PartitionBlock> p, FiniteStateMachine currentM){

        Set<String> newStates = new HashSet<>();
        Set<String> newAlphOutputs = new HashSet<>();
        HashMap<String, HashMap<String, String>> newTransitions = new HashMap<>();
        HashMap<String, HashMap<String, String>> newOutputs = new HashMap<>();

        for(String state: currentM.getStates()){
            HashMap<String, String> transitions = new HashMap<>();
            HashMap<String, String> outputs = new HashMap<>();
            String newState = createNameState(p, state);
            String newOut = "";

            for(String input: alphabetInput){
                String t = currentM.getTransitions().get(state).get(input);
                String nt = createNameState(p, t);
                transitions.put(input, nt);

                boolean sentinel = false;
                for(int i = 0; i < p.size() && !sentinel; i++){
                if(p.get(i).getStates().contains(state)){
                    sentinel = true;
                    newOut = createOutput(p.get(i), currentM);
                    outputs.put(input, newOut);
                } 
            }
            }
            
            newStates.add(newState);
            newAlphOutputs.add(newOut);
            newTransitions.put(newState, transitions);
            newOutputs.put(newState, outputs);
        }

        List<String> nAlphOutputs = new ArrayList<>();
        for(String nO: newAlphOutputs){
            nAlphOutputs.add(nO);
        }

        return new MealyMachine(newStates, alphabetInput, nAlphOutputs, newTransitions, newOutputs);
    }

    private String createOutput(PartitionBlock pb, FiniteStateMachine fsm){

        List<String> listO = new ArrayList<>();
        String s = "";

        for(String state: pb.getStates()){
            HashMap<String, String> outputs = ((MealyMachine)fsm).getFOutputs().get(state);
            for(String input: fsm.getInputs()){
                if(!listO.contains(outputs.get(input))){
                    listO.add(outputs.get(input));
                }
            }
        }

        for(String v: listO){
            s+= v;
        }

        return s;

    }


    private String createNameState(List<PartitionBlock> p, String state){

        String newState = "";
        boolean sentinel = false;
        for(int i = 0; i < p.size() && !sentinel; i++){
            if(p.get(i).getStates().contains(state)){
                sentinel = true;
                for(String st: p.get(i).getStates()){
                    newState+=st;
                }
            }
        }


        return newState;
    }

    private boolean isEqualsPartition(List<PartitionBlock> p1, List<PartitionBlock> p2){

        if(p1.size() == p2.size()){
            for(int i = 0; i < p1.size(); i++){
                if(!p1.get(i).toString().equals(p2.get(i).toString())){
                    return false;
                }
            }
        } else{
            return false;
        }

        return true;

    }


    //Getters & Setters
    public HashMap<String, HashMap<String, String>> getFOutputs(){
        return functionOutput;
    }
 
    public String toString(){
		String s = "| states |";

        for(String i: alphabetInput){
            s+= " input(" + i + ") |";
        }

        s+= "\n";

		for(String state: states){
            s += "|" + state + "|";
            HashMap<String, String> h = getTransitions().get(state);
            for(String i: alphabetInput){
                s+= h.get(i) + " | ";
                s+= getFOutputs().get(state).get(i) + " | ";
            }
            s+="\n";
            
        }

		return s;
	}

}
