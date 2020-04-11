package com.company;

import java.util.Objects;

public class Innovation {
    private int innovation_number;
    private int inNodeNumber;
    private int outNodeNumber;

    public Innovation(int innovation_number, int inNodeNumber, int outNodeNumber){
        this.innovation_number = innovation_number;
        this.inNodeNumber = inNodeNumber;
        this.outNodeNumber = outNodeNumber;
    }

    public int getInnovation_number() {
        return innovation_number;
    }

    public int getInNodeNumber() {
        return inNodeNumber;
    }

    public int getOutNodeNumber() {
        return outNodeNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Innovation that = (Innovation) o;
        return inNodeNumber == that.inNodeNumber &&
                outNodeNumber == that.outNodeNumber;
    }
}
