package com.cir.models;

public class Dataset {

    protected Algorithms algos;

    public Dataset(Algorithms algos) {
        this.algos = algos;
    }

    public Algorithms getAlgos() {
        return algos;
    }

    public void setAlgos(Algorithms algos) {
        this.algos = algos;
    }



}
