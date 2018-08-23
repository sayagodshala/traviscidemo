package com.merabreak;

import com.merabreak.models.Operators;

/**
 * Created by Vinay on 7/26/2016.
 */
public class OpratorRecievedEvent {
    private Operators operators;

    public OpratorRecievedEvent(Operators operators) {
        this.operators = operators;
    }

    public Operators getOperators() {
        return operators;
    }

    public void setOperators(Operators operators) {
        this.operators = operators;
    }
}
