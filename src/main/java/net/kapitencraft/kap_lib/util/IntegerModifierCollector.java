package net.kapitencraft.kap_lib.util;

import java.util.ArrayList;
import java.util.List;

public class IntegerModifierCollector {

    private int base;
    private final List<Integer> addition = new ArrayList<>(),
            multiplyBase = new ArrayList<>(),
            multiplyTotal = new ArrayList<>();


    public void setBase(int base) {
        this.base = base;
    }

    public int getBase() {
        return base;
    }

    public void addAddition(int d) {
        addition.add(d);
    }

    public void addBaseMultiplication(int d) {
        multiplyBase.add(d);
    }

    public void addTotalMultiplication(int d) {
        multiplyTotal.add(d);
    }

    public int calculate() {
        int base = this.base;
        for (int d : addition) base += d;
        for (int d : multiplyBase) base *= d;
        for (int d : multiplyTotal) base *= d;
        return base;
    }
}