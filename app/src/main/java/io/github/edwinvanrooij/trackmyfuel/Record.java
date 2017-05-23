package io.github.edwinvanrooij.trackmyfuel;

import org.parceler.Parcel;

/**
 * Created by eddy on 5/16/17.
 */

@Parcel
public class Record {
    private int id;
    private double km;
    private Type type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Record() {
    }

    public Record(int id, double km, Type type) {
        this.id = id;
        this.km = km;
        this.type = type;
    }

    public Record(double km, Type type) {
        this.km = km;
        this.type = type;
    }

    public enum Type {
        INSIDE, AVERAGE, OUTSIDE
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", km=" + km +
                ", type=" + type +
                '}';
    }
}
