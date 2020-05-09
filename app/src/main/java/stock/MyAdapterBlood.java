package stock;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bloodon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;


public class MyAdapterBlood extends RecyclerView.Adapter<MyAdapterBlood.MyViewHolder> {


    List<Stock> mData;
    Context mContext;
    Dialog myDialog;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private DatabaseReference UsersRef, StocksRef, StocksChildRef, bloodNeeded;

    public MyAdapterBlood( Context mContext,List<Stock> mData) {
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
        View view = inflater.inflate(R.layout.item_blood, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(view);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();


        StocksRef = FirebaseDatabase.getInstance().getReference().child("Stocks");
        StocksChildRef = StocksRef.child(current_user_id);

        vHolder.item_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Firebase
                //Dialog
                myDialog =  new Dialog(mContext);
                myDialog.setContentView(R.layout.dialog_blood);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView dialog_blood_name = (TextView) myDialog.findViewById(R.id.dialog_blood_name);
                final EditText dialog_blood_edit = (EditText) myDialog.findViewById(R.id.dialog_blood_quantity);
                ImageView dialog_blood_img = (ImageView) myDialog.findViewById(R.id.dialog_blood_img);
                Button dialog_blood_button = (Button) myDialog.findViewById(R.id.dialog_blood_edit);
                dialog_blood_name.setText(mData.get(vHolder.getAdapterPosition()).getType());
                dialog_blood_edit.setText(mData.get(vHolder.getAdapterPosition()).getQte());
                dialog_blood_img.setImageResource(mData.get(vHolder.getAdapterPosition()).getImg());
                dialog_blood_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String qte = dialog_blood_edit.getText().toString();// La quantité editer par l'utilisateur
                        if (TextUtils.isEmpty(qte) || qte.equals(mData.get(vHolder.getAdapterPosition()).getQte())) {
                            Toast.makeText(mContext, "Please entre a valid quantity", Toast.LENGTH_SHORT).show();
                        } else {
                            bloodNeeded = StocksChildRef.child(mData.get(vHolder.getAdapterPosition()).getType().toString());
                            bloodNeeded.setValue(qte);
                            int index = vHolder.getAdapterPosition();
                            String key = mData.get(vHolder.getAdapterPosition()).getType();
                            //mData.set(mData.get(index),Stock.setQte(qte));// je veux que la liste aussi du fragmentBlood change,
                            // alors je mets l index ou je veux changer, et puis le 2eme argument est de type Stock, donc je sais pass s'il marche
                            // La on doit mettre la fonction qui ajoute la qte dans la base de donnée !!
                        }
                    }
                });
                //Toast.makeText(mContext,"Test Click"+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                myDialog.show();
            }
        });
        return vHolder;
    }

    @Override

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_type.setText(mData.get(position).getType());
        holder.tv_qte.setText(mData.get(position).getQte());
        holder.img_blood.setImageResource(mData.get(position).getImg());
        /*Hopital Hop = (Hopital) mData.get(position);
        System.out.println("Vname =" + Hop.getName());
        System.out.println("position=" + position);
        holder.display(Hop);*/
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private    TextView tv_type,tv_qte;
        private ImageView img_blood;
        private LinearLayout item_blood;
        /*public Hopital Hopit;*/


        public MyViewHolder(final View itemView) {
            super(itemView);
            item_blood = (LinearLayout)itemView.findViewById(R.id.blood_item_id) ;
            this.tv_type = itemView.findViewById(R.id.tv_type);
            this.tv_qte = itemView.findViewById(R.id.tv_qte);
            this.img_blood =itemView.findViewById(R.id.img_blood);
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
