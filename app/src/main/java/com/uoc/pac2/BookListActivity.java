package com.uoc.pac2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uoc.pac2.adapters.BookListAdapter;
import com.uoc.pac2.model.BookContent;

import java.util.ArrayList;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookListActivity extends AppCompatActivity implements BookDetailFragment.OnFragmentInteractionListener {

    private final String TAG = this.getClass().getCanonicalName();

    BookListAdapter adapter;
    boolean mTwoPane = false;
    FirebaseAuth mAuth = null;
    ArrayList<BookContent.BookItem> bookList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseLogin();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (findViewById(R.id.content_book_detail) != null) {
            mTwoPane = true;
            BookDetailFragment aBookDetailFragment = BookDetailFragment.newInstance(getIntent().getIntExtra(BookDetailFragment.BOOK_POSITION, 0));

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_book_detail, aBookDetailFragment)
                    .commit();
        }

        adapter = new BookListAdapter(this, bookList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void firebaseLogin() {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(this.getString(R.string.firebase_username), this.getString(R.string.firebase_password)).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(BookListActivity.this, R.string.firebase_username, Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        System.out.println("Logged");
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                int id = 0;
                                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                                    BookContent.BookItem book = bookSnapshot.getValue(BookContent.BookItem.class);
                                    book.setId(id);
                                    bookList.add(book);
                                    id++;
                                }
                                adapter.notifyDataSetChanged();
                                System.out.println(dataSnapshot.getValue());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    } else {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(BookListActivity.this, R.string.firebase_username, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void onFragmentInteraction() {
        if (adapter != null) {
            adapter.setmTwoPane(true);
        }
    }
}
