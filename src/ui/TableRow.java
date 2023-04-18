package ui;

import java.util.*;

public class TableRow {

    private String state;
    private List<String> body;

    //Constructor
    public TableRow(String s, List<String> b){
        state = s;
        body = b;
    }

    public String getState(){
        return state;
    }

    public List<String> getBody(){
        return body;
    }

}
