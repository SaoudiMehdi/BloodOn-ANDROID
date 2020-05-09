package com.example.bloodon;

import android.app.AlertDialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapter extends RecyclerView.Adapter {
    ArrayList Hospitals;
    Context context;


    // for getting the data from Activity "Mylist"
    public MyAdapter(Context context, ArrayList Etab) {
        this.context = context;
        this.Hospitals = Etab;

    }


    @Override

    //inflate the layout item xml and pass it to View Holder
    //associer notre adapter à notre vu
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemhos, parent, false);
        return new MyViewHolder(view);

    }

    //
    @Override
    //set the data in the view’s by way of ViewHolder.
    //affecte les données aux widgets de la vue
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final findHospital Etab= (findHospital) Hospitals.get(position);

        ((MyViewHolder)    holder).name.setText(Etab.getname());
        ((MyViewHolder)    holder).label.setText(Etab.getlabel());
        ((MyViewHolder)    holder).img.setImageResource(Etab.getimage());


        ((MyViewHolder)    holder).display(Etab);


    }

    @Override
    public int getItemCount() {

        return Hospitals.size();
    }


    //get the reference of item view's
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView label;
        TextView name;
        ImageView img;
        private findHospital currentHosp;
        public MyViewHolder(final View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            name = itemView.findViewById(R.id.name);
            img=  itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(itemView.getContext()).setTitle(currentHosp.getlabel()).show();
                    Intent intent = new Intent(context,MapsActivity.class);
                    intent.putExtra("Longitude", currentHosp.getLng());
                    intent.putExtra("Latitude", currentHosp.getLat());
                    intent.putExtra("Label", currentHosp.getlabel());
                    intent.putExtra("Name", currentHosp.getname());
                    context.startActivity(intent);
                }
            });


        }

        public void display(findHospital Hosp) {
            currentHosp = Hosp;
            name.setText(Hosp.getname());
            label.setText(Hosp.getlabel());
            img.setImageResource(Hosp.getimage());



        }



    }



}
