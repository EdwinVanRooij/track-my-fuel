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

    public Type getType() {
        return type;
    }

    public Record(int km, Type type) {
        this.km = km;
        this.type = type;
    }

    public enum Type {
        INSIDE, AVERAGE, OUTSIDE
    }
//
//    public enum Type {
//        INSIDE(0),
//        AVERAGE(1),
//        OUTSIDE(2);
//
//        private final int value;
//
//        Type(final int newValue) {
//            value = newValue;
//        }
//
//        public int getValue() {
//            return value;
//        }
//    }
}
