package com.merabreak;

import com.merabreak.models.Circles;

/**
 * Created by Vinay on 7/26/2016.
 */
public class CircleRecievedEvent {

    private Circles circles;

    public CircleRecievedEvent(Circles circles) {
        this.circles = circles;
    }

    public Circles getCircles() {
        return circles;
    }

    public void setCircles(Circles circles) {
        this.circles = circles;
    }
}
