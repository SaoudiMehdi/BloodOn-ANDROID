package stock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bloodon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentBlood extends Fragment {

    View view;
    private RecyclerView  rvb;
    List<Stock> lstBlood;

    private FirebaseAuth mAuth;
    private String current_user_id;
    private DatabaseReference UsersRef, StocksRef, StocksChildRef, bloodNeeded;

    public  String a_plus;
    public String b_plus;
    public String a_moins;
    public String b_moins;
    public String ab_plus;
    public String ab_moins;
    public String o_plus;
    public String o_moins;
    private String fullname;

    public FragmentBlood(String a_plus, String b_plus, String a_moins, String b_moins, String ab_plus, String ab_moins, String o_plus, String o_moins, String fullname) {
        this.a_plus = a_plus;
        this.b_plus = b_plus;
        this.a_moins = a_moins;
        this.b_moins = b_moins;
        this.ab_plus = ab_plus;
        this.ab_moins = ab_moins;
        this.o_plus = o_plus;
        this.o_moins = o_moins;
        this.fullname = fullname;
    }


    public FragmentBlood() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blood_fragment, container, false);
        rvb = view.findViewById(R.id.blood_recyclerview);
        MyAdapterBlood rvAdapter = new MyAdapterBlood(getContext(),lstBlood);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rvb.setLayoutManager(layoutManager);
        rvb.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        rvb.setAdapter(rvAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstBlood = new ArrayList<>();

        lstBlood.add(new Stock(fullname,"a_plus",a_plus,R.drawable.aplus));
        lstBlood.add(new Stock(fullname,"a_moins",a_moins,R.drawable.amoins));
        lstBlood.add(new Stock(fullname,"b_plus",b_plus,R.drawable.bplus));
        lstBlood.add(new Stock(fullname,"b_moins",b_moins,R.drawable.bmoins));
        lstBlood.add(new Stock(fullname,"ab_plus",ab_plus,R.drawable.abplus));
        lstBlood.add(new Stock(fullname,"ab_moins",ab_moins,R.drawable.abmoins));
        lstBlood.add(new Stock(fullname,"o_plus",o_plus,R.drawable.omoins));
        lstBlood.add(new Stock(fullname,"o_moins",o_moins,R.drawable.oplus));
    }


}
