package io.github.edwinvanrooij.trackmyfuel;

import org.parceler.Parcel;

/**
 * Created by eddy on 5/16/17.
 */

@Parcel
public class Record {
    private int id;
    private int km;
    private Type type;

    public long getId() {
        return id;
    }

    public int getKm() {
        return km;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Record() {
    }

    public Record(int km, Type type) {
        this.km = km;
        this.type = type;
    }

    public Record(int id, int km, Type type) {
        this.id = id;
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
