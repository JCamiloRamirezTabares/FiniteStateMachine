package model;

import java.util.*;

public class PartitionBlock {
    
    private List<String> states;
    private String output;

    public PartitionBlock(List<String> t, String o){
        states = t;
        output = o;
    }

    public void addAState(String s){
        states.add(s);
    }

    public List<String> getStates(){
        return states;
    }

    public String getOutput(){
        return output;
    }

    public void setOutput(String o){
        output = o;
    }

    public String toString(){
        return "States: " + getStates() + " Output: " + output;
    }

}
