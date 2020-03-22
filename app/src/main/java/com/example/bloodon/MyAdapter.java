package com.example.tp2;



import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    public static  ArrayList Hopitaux = new ArrayList<>(Arrays.asList(
            new Hopital("Hopital Moulay Youssef", 12, 14,15,16,17,18,19,20)

    ));
    public static String HopitSelected;

    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {

        return Hopitaux.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Hopital Hop = (Hopital) Hopitaux.get(position);
        System.out.println("Vname =" + Hop.getLabel());
        System.out.println("position=" + position);

        holder.display(Hop);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public   TextView label;
        public   TextView aplus,amoins,bplus,bmoins,abplus,abmoins,oplus,omoins;
        public String Label;
        public double aPlus,aMoins,bPlus,bMoins,abPlus,abMoins,oPlus,oMoins;
        public   Hopital Hopit;


        public MyViewHolder(final View itemView) {
            super(itemView);

            this.label = itemView.findViewById(R.id.label);
            this.aplus = itemView.findViewById(R.id.aplus);
            this.amoins = itemView.findViewById(R.id.amoins);
            this.bplus = itemView.findViewById(R.id.bplus);
            this.bmoins = itemView.findViewById(R.id.bmoins);
            this.abplus = itemView.findViewById(R.id.abplus);
            this.abmoins = itemView.findViewById(R.id.abmoins);
            this.oplus = itemView.findViewById(R.id.oplus);
            this.omoins = itemView.findViewById(R.id.omoins);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext()).setTitle(Hopit.getLabel()).show();
                }
            });
        }

        public void display(Hopital Hopit) {
            NumberFormat nm= NumberFormat.getNumberInstance();
            this.Hopit = Hopit;
            this.label.setText(Hopit.getLabel());
            this.aplus.setText(nm.format(Hopit.getAplus()));
            this.amoins.setText(nm.format(Hopit.getAmoins()));
            this.bplus.setText(nm.format(Hopit.getBplus()));
            this.bmoins.setText(nm.format(Hopit.getBmoins()));
            this.abplus.setText(nm.format(Hopit.getAbplus()));
            this.abmoins.setText(nm.format(Hopit.getAbmoins()));
            this.oplus.setText(nm.format(Hopit.getOplus()));
            this.omoins.setText(nm.format(Hopit.getOmoins()));


        }
    }

    public ArrayList getHopitaux() {
        return Hopitaux;
    }
}
