package com.example.bloodon.com.example.Fragment;



import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodon.Hopital;
import com.example.bloodon.R;

import java.util.List;




public class MyAdapterStock extends RecyclerView.Adapter<MyAdapterStock.MyViewHolder> {




    List<Hopital> mData;
    Context mContext;

    public MyAdapterStock(Context mContext, List<Hopital> mData) {
        this.mContext=mContext;
        this.mData = mData;
    }

    /*public static ArrayList<Hopital> Hopitaux = new ArrayList<>(Arrays.asList(
            new Hopital("Stock Moulay Youssef",new Stock("Type A+ :",12, aplus)),
            new Hopital("Stock Moulay Youssef",new Stock("Type A- :",12,R.drawable.amoins)),
            new Hopital("Stock Moulay Youssef", new Stock("Type B+ :",12,R.drawable.bplus)),
            new Hopital("Stock Moulay Youssef", new Stock("Type B- :",12,R.drawable.bmoins)),
            new Hopital("Stock Moulay Youssef",new Stock("Type AB+ :",12,R.drawable.abplus)),
            new Hopital("Stock Moulay Youssef", new Stock("Type AB- :",12,R.drawable.abmoins)),
            new Hopital("Stock Moulay Youssef", new Stock("Type O+ :",12,R.drawable.oplus)),
            new Hopital("Stock Moulay Youssef", new Stock("Type O- :",12,R.drawable.omoins))

    ));*/

    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_stock, parent, false);
        MyViewHolder vHolder = new MyViewHolder(view);
        return vHolder;
    }

    @Override

    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_address.setText(mData.get(position).getAddress());
        holder.img_hop.setImageResource(mData.get(position).getImg());


        /*Hopital Hop = (Hopital) mData.get(position);
        System.out.println("Vname =" + Hop.getName());
        System.out.println("position=" + position);
        holder.display(Hop);*/
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private    TextView tv_name,tv_address;
        private ImageView img_hop;
        /*public Hopital Hopit;*/


        public MyViewHolder(final View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_address = itemView.findViewById(R.id.tv_address);
            this.img_hop =itemView.findViewById(R.id.img_hop);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext()).setTitle(Hopit.getName()).show();
                }
            });*/
        }

       /* public void display(Hopital Hopit) {
            NumberFormat nm= NumberFormat.getNumberInstance();
            this.Hopit = Hopit;
            this.tv_name.setText(Hopit.getName());
            this.tv_address.setText(Hopit.getAddress());
            this.img_hop.setText(nm.format(Hopit.getImg()));

        }*/
    }


}
