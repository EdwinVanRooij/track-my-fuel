package io.github.edwinvanrooij.trackmyfuel;

/**
 * Created by eddy on 5/16/17.
 */

public class Record {
    private int km;
    private Type type;

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Record(int km, Type type) {
        this.km = km;
        this.type = type;
    }

    enum Type {
        Inside,
        Average,
        Outside,;
    }

    @Override
    public String toString() {
        return "Record{" +
                "km=" + km +
                ", type=" + type +
                '}';
    }
}
