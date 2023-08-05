package com.example.gameofdeath;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Pixel extends Rectangle {
    public boolean active = false;

    public int size = 5;
    public int population = 70;
    int i, j;

    public Pixel(int posi, int posj) {
        i = posi;
        j = posj;
        setWidth(size);
        setHeight(size);
        Random PRNG = new Random();
        active = PRNG.nextInt(100) > population;
        if (active)
            activate();
        else
            deactivate();

    }
    public void activate(){
        active = true;
        setFill(Color.GREENYELLOW);
    }
    public void deactivate(){
        active = false;
        setFill(Color.BLACK);
    }
    public void toggle(){
        if(active)
            deactivate();
        else
            activate();
    }
}
