package io.github.edwinvanrooij.trackmyfuel;

import org.parceler.Parcel;

/**
 * Created by eddy on 5/16/17.
 */

@Parcel
public class Payer {
    private String name;
    private double ownCosts;
    private double extra;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOwnCosts() {
        return ownCosts;
    }

    public void setOwnCosts(double ownCosts) {
        this.ownCosts = ownCosts;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Payer{" +
                "name='" + name + '\'' +
                ", ownCosts=" + ownCosts +
                ", extra=" + extra +
                '}';
    }

    public Payer(String name, double ownCosts, double extra) {
        this.name = name;
        this.ownCosts = ownCosts;
        this.extra = extra;
    }

    public Payer() {
    }
}
