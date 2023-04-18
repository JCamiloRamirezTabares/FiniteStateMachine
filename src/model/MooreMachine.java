package model;

import java.util.*;

public class MooreMachine extends FiniteStateMachine{
    /*Dado un estado (j) retorna un ? que puede ser:
     * Mealy: Un conjunto de datos (k,v) donde k es la entrada y v es la salida (Depende del estado actual y la entrada)
     * Moore: La salida respectiva del estado (Depende solo del estado actual)
    */
    protected HashMap<String, String> functionOutput;

    public MooreMachine(Set<String> statesnumber, List<String> input, List<String> output, HashMap<String, HashMap<String, String>> ftransition, HashMap<String, String> foutPut) {
        super(statesnumber, input, output, ftransition);
        functionOutput = foutPut;

    }

    @Override
    public FiniteStateMachine minimize(){
        FiniteStateMachine conectedMachine = getConectedFSM();

        List<PartitionBlock> initialPartition = createPartition(conectedMachine);
        for(PartitionBlock pb: initialPartition){
            System.out.println(pb);
        }


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
        HashMap<String, String> newO = new HashMap<>();

        for(String s: fm.getStates()){
            HashMap<String, String> t = fm.getTransitions().get(s);
            String nOut = "";
            for(String input: alphabetInput){
                String st = t.get(input);
                boolean sentinel = false;
                for(int i = 0; i < p.size() && !sentinel; i++){
                    if(p.get(i).getStates().contains(st)){
                        sentinel = true;
                        nOut+= i;
                    }
                }
            }

            newO.put(s, nOut);
        }

        return new MooreMachine(fm.getStates(), fm.getInputs(), fm.getOutputs(), fm.getTransitions(), newO);
    }

    private List<PartitionBlock> createPartition(FiniteStateMachine c) {
        List<PartitionBlock> initialP = new ArrayList<>();

        List<String> states = c.getStatesList();

        for(String s: states){
            if(initialP.isEmpty()){
                List<String> st = new ArrayList<>();
                String out = ((MooreMachine) c).getFOutputs().get(s);
                st.add(s);

                initialP.add(new PartitionBlock(st, out));
            } else{
                boolean sentinel = false;
                String o = ((MooreMachine) c).getFOutputs().get(s);
                for(int i = 0; i < initialP.size() && !sentinel; i++){
                    if(initialP.get(i).getOutput().equals(o)){
                        initialP.get(i).addAState(s);
                        sentinel = true;
                    }
                }
                
                if(sentinel == false){
                    List<String> st = new ArrayList<>();
                    st.add(s);
                    initialP.add(new PartitionBlock(st, o));
                }

            }
        }

        return initialP;
    }

    @Override
    public FiniteStateMachine getConectedFSM() {
        Set<String> newStates = new HashSet<>();
        HashMap<String, HashMap<String, String>> newTransitions = new HashMap<>();
        HashMap<String, String> newOutputs = new HashMap<>();
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

        FiniteStateMachine connectedFSM = new MooreMachine(newStates, alphabetInput, alphabetOutput, newTransitions, newOutputs);
        return connectedFSM;
    }



    private FiniteStateMachine createMinimizeMachine(List<PartitionBlock> p, FiniteStateMachine currentM){

        Set<String> newStates = new HashSet<>();
        Set<String> newAlphOutputs = new HashSet<>();
        HashMap<String, HashMap<String, String>> newTransitions = new HashMap<>();
        HashMap<String, String> newOutputs = new HashMap<>();

        for(String state: currentM.getStates()){
            
            HashMap<String, String> transitions = new HashMap<>();
            String newState = createNameState(p, state);
            String newOut = "";

            for(String i: alphabetInput){
                String t = currentM.getTransitions().get(state).get(i);
                String nt = createNameState(p, t);
                transitions.put(i, nt);
            }

            boolean sentinel = false;
            for(int i = 0; i < p.size() && !sentinel; i++){
                if(p.get(i).getStates().contains(state)){
                    sentinel = true;
                    newOut = createOutput(p.get(i), currentM);
                } 
            }
            
            
            newStates.add(newState);
            newAlphOutputs.add(newOut);
            newTransitions.put(newState, transitions);
            newOutputs.put(newState, newOut);
        }

        List<String> nAlphOutputs = new ArrayList<>();
        for(String nO: newAlphOutputs){
            nAlphOutputs.add(nO);
        }

        return new MooreMachine(newStates, alphabetInput, nAlphOutputs, newTransitions, newOutputs);
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

    private String createOutput(PartitionBlock pb, FiniteStateMachine fsm){

        List<String> listO = new ArrayList<>();
        String s = "";

        for(String state: pb.getStates()){
            String sa = ((MooreMachine)fsm).getFOutputs().get(state);
            if(!listO.contains(sa)){
                listO.add(sa);
            }
        }

        for(String v: listO){
            s+= v;
        }

        return s;

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


    public HashMap<String, String> getFOutputs(){
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
            }
            s+= getFOutputs().get(state) + "| \n";
        }

		return s;
	}
}
