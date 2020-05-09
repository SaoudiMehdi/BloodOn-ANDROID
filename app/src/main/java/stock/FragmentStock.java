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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FragmentStock extends Fragment {

    View view;
    private RecyclerView  rv;
    List<Hopital> lstStock;


    private FirebaseAuth mAuth;
    private String current_user_id;
    private DatabaseReference UsersRef, StocksRef;


    public FragmentStock() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stock_fragment, container, false);
        rv = view.findViewById(R.id.stock_recyclerview);
        MyAdapterStock rvAdapter = new MyAdapterStock(getContext(),lstStock);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        rv.setAdapter(rvAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstStock = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");



        lstStock.add(new Hopital("CHU Hassan II Fes","Avenue Hassan II, Fes 30050",R.mipmap.chu_tanger_tetouan_al_hoceima));

    }
}
