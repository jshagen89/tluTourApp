package com.example.joseph.tlucampustour;

/**
 * Created by Joseph on 9/8/2016.
 */
public class TourStop {

    private String name;

    public TourStop(String name)
    {
        this.name = name;
    }

    private String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
