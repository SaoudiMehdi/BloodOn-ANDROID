package com.example.tp2;

public class Hopital {
    String label;
    double aplus,amoins,bplus,bmoins,abplus,abmoins,omoins,oplus;
    public Hopital(String label, double aplus, double amoins,double bplus,double bmoins,double abplus,double abmoins,double oplus,double omoins){
        this.label=label;
        this.aplus=aplus;
        this.amoins=amoins;
        this.bplus=bplus;
        this.bmoins=bmoins;
        this.abplus=abplus;
        this.abmoins=abmoins;
        this.oplus=oplus;
        this.omoins=omoins;
    }

    public String getLabel() {
        return label;
    }

    public double getAplus() {
        return aplus;
    }

    public double getAmoins() {
        return amoins;
    }

    public double getBplus() {
        return bplus;
    }

    public double getBmoins() {
        return bmoins;
    }

    public double getAbplus() {
        return abplus;
    }

    public double getAbmoins() {
        return abmoins;
    }

    public double getOmoins() {
        return omoins;
    }

    public double getOplus() {
        return oplus;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAplus(double aplus) {
        this.aplus = aplus;
    }

    public void setAmoins(double amoins) {
        this.amoins = amoins;
    }

    public void setBplus(double bplus) {
        this.bplus = bplus;
    }

    public void setBmoins(double bmoins) {
        this.bmoins = bmoins;
    }

    public void setAbplus(double abplus) {
        this.abplus = abplus;
    }

    public void setAbmoins(double abmoins) {
        this.abmoins = abmoins;
    }

    public void setOmoins(double omoins) {
        this.omoins = omoins;
    }

    public void setOplus(double oplus) {
        this.oplus = oplus;
    }
}
