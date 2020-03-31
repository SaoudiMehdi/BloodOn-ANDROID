package com.example.bloodon;

import android.content.Intent;
import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;


import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.bloodon.R.layout.activity_demandes;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView  postListNav;
    private Toolbar toolBarNav;
    private CircleImageView imageProfilNav;
    private TextView profileUserNameNav;
    private ImageButton addNewPostNav;
    private FirebaseAuth mAuth;
    private DatabaseReference userNav,postsNav,likesNav;
    String currentUserID;
    Boolean likeCheckNav = false;



    private void afficherUsersDemandes() {


        FirebaseRecyclerOptions<demandes> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<demandes>()
                .setQuery(postsNav, demandes.class)
                .build();

        FirebaseRecyclerAdapter<demandes, PostsViewHolder> firebaseRecyclerNav = new FirebaseRecyclerAdapter<demandes, PostsViewHolder>
                (firebaseRecyclerOptions) {
            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder vH, int position, @NonNull demandes model) {
                final String initial= getRef(position).getKey();
                vH.setFullname(model.getFullname());
                vH.setTime(model.getTime());
                vH.setDate(model.getDate());
                vH.setDescription(model.getDescription());
                vH.setProfileimage(model.getProfileimage());
                vH.setPostimage(model.getPostimage());
                vH.setLikeButton(initial);
                vH.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent = new Intent(MainActivity.this,clickPostNav.class);
                        clickPostIntent.putExtra("initial",initial);
                        startActivity(clickPostIntent);
                    }
                });

                vH.commentButtonNav.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        Intent commentsIntent = new Intent(MainActivity.this,commentNav.class);
                        commentsIntent.putExtra("initial",initial);
                        startActivity(commentsIntent);

                    }
                });



                vH.likeButtonNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        likeCheckNav = true;
                        likesNav.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(likeCheckNav.equals(true)){

                                    if(dataSnapshot.child(initial).hasChild(currentUserID))
                                    {
                                        likesNav.child(initial).child(currentUserID).removeValue();
                                        likeCheckNav = false;
                                    }
                                    else
                                    {
                                        likesNav.child(initial).child(currentUserID).setValue(true);
                                        likeCheckNav=false;
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
        postListNav.setAdapter(firebaseRecyclerNav);

            }



    }






   class PostsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton likeButtonNav,commentButtonNav;
        TextView nombreLikesNav;
        int countLikes;
        String currentUserId;
        DatabaseReference LikesNav;

        public PostsViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            likeButtonNav = mView.findViewById(R.id.likeButton);
            commentButtonNav = mView.findViewById(R.id.commentButton);
            nombreLikesNav= mView.findViewById(R.id.numberOfLikes);

            LikesNav = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

        public void setLikeButton(final String initial)
        {
            LikesNav.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(initial).hasChild(currentUserId)){
                        countLikes= (int) dataSnapshot.child(initial).getChildrenCount();
                        likeButtonNav.setImageResource(R.drawable.dislike);
                        nombreLikesNav.setText(Integer.toString(countLikes)+(" Likes"));
                    }
                    else{
                        countLikes= (int) dataSnapshot.child(initial).getChildrenCount();
                        likeButtonNav.setImageResource(R.drawable.dislike);
                        nombreLikesNav.setText(  Integer.toString(countLikes)+(" Likes"));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




        public void setFullname(String fullname) {
            TextView username = mView.findViewById(R.id.userNameNav);
            username.setText(fullname);
        }

        public void setProfileimage(String profileimage) {
            CircleImageView image = mView.findViewById(R.id.profileImageNav);
            Picasso.get().load(profileimage).into(image);
        }

        public void setTime(String time) {
            TextView PostTime = mView.findViewById(R.id.postTimeNav);
            PostTime.setText("   " + time);
        }

        public void setDate(String date) {
            TextView PostDate = mView.findViewById(R.id.postDateNav);
            PostDate.setText("   " + date);
        }

        public void setDescription(String description) {
            TextView PostDescription = mView.findViewById(R.id.descriptionPostNav);
            PostDescription.setText(description);
        }

        public void setPostimage(String postimage) {
            ImageView PostImage = mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(PostImage);
        }
    }







