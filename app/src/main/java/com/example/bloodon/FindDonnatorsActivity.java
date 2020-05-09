package com.example.bloodon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindDonnatorsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;

    private FirebaseAuth mAuth;

    private DatabaseReference allUsersDatabaseRef, PointsRaf, UserRef;
    Boolean PointsChecker = false;

    private RecyclerView SearchResultList;

    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donnators);

        mToolbar = findViewById(R.id.find_friends_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        mAuth = FirebaseAuth.getInstance();

        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        PointsRaf = FirebaseDatabase.getInstance().getReference().child("point");

        SearchResultList = findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = findViewById(R.id.search_people_frieds_button);
        SearchInputText = findViewById(R.id.search_box_input);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String SearchBoxInput= SearchInputText.getText().toString();
                SearchPeopleAndDonnators(SearchBoxInput);
            }
        });


        if (mAuth.getCurrentUser()!=null) {
            currentUserID = mAuth.getCurrentUser().getUid();

        }

        /*UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String bonusValue = dataSnapshot.child("point").getValue().toString();
                    Log.w("POINTS: ",bonusValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    private void SearchPeopleAndDonnators(String searchBoxInput)
    {
        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput.toLowerCase()).endAt(searchBoxInput.toLowerCase() +"\uf8ff");

        FirebaseRecyclerAdapter<FindDonnators,FindDonnatorsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindDonnators, FindDonnatorsViewHolder>
                (
                        FindDonnators.class,
                        R.layout.all_users_display_layout,
                        FindDonnatorsViewHolder.class,
                        searchPeopleandFriendsQuery
                )
        {
            @Override
            protected void populateViewHolder(FindDonnatorsViewHolder viewHolder, FindDonnators model, int position)
            {
                final String PostKey= getRef(position).getKey();
             viewHolder.setFullname(model.getFullname());
             //viewHolder.setUid(model.getEmail());
             viewHolder.setProfileimage(model.getProfileimage());

             //Log.w("USERID",model.getEmail());
             viewHolder.setPointsButtonStatus(PostKey);

                viewHolder.AddPointsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        PointsChecker = true;
                        PointsRaf.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(PointsChecker.equals(true)){

                                    if(dataSnapshot.child(PostKey).hasChild(currentUserID))
                                    {
                                        PointsRaf.child(PostKey).child(currentUserID).removeValue();
                                        PointsChecker = false;
                                    }
                                    else
                                    {
                                        PointsRaf.child(PostKey).child(currentUserID).setValue(true);
                                        PointsChecker=false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };
        SearchResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FindDonnatorsViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton AddPointsButton;
        String currentUserId;
        DatabaseReference PointsRaf, UserRef;
        TextView DisplayNoOfPoints;
        String test;
        int countPoints;

        View mView;


        public FindDonnatorsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            AddPointsButton = mView.findViewById(R.id.points_button);

            DisplayNoOfPoints= mView.findViewById(R.id.display_no_of_points);

            PointsRaf = FirebaseDatabase.getInstance().getReference().child("point");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mView = itemView;
        }

        public void setPointsButtonStatus(final String PostKey)
        {
            PointsRaf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    //UserRef =FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    if(dataSnapshot.child(PostKey).hasChild(currentUserId)){
                        countPoints= (int) dataSnapshot.child(PostKey).getChildrenCount();
                        test = dataSnapshot.child(PostKey).getKey();
                        AddPointsButton.setImageResource(R.drawable.ic_like_24dp);
                        DisplayNoOfPoints.setText(Integer.toString(countPoints)+(" points"));
                    }
                    else{
                        countPoints= (int) dataSnapshot.child(PostKey).getChildrenCount();
                        test = dataSnapshot.child(PostKey).getKey();
                        AddPointsButton.setImageResource(R.drawable.ic_deslike_24dp);
                        DisplayNoOfPoints.setText(  Integer.toString(countPoints)+(" points"));

                    }
                    UserRef =FirebaseDatabase.getInstance().getReference().child("Users").child(test);
                    UserRef.child("point").setValue(countPoints);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setProfileimage(String profileimage)
        {
            CircleImageView myImage = mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(myImage);
        }

        public void setFullname(String fullname)
        {
            TextView myName = mView.findViewById(R.id.all_users_profile_full_name);
            myName.setText(fullname);
        }

        /*public void setUid(String uid)
        {
            TextView myUid = mView.findViewById(R.id.all_users_status);
            myUid.setText(uid);
            myUid.setVisibility(View.INVISIBLE);
        }*/
    }
}
