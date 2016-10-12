package com.uoc.tfm.pac1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uoc.tfm.pac1.adapters.BookListAdapter;
import com.uoc.tfm.pac1.model.BookContent;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookListActivity extends AppCompatActivity implements BookDetailFragment.OnFragmentInteractionListener {

    BookListAdapter adapter;
    boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        adapter = new BookListAdapter(this, BookContent.ITEMS);
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

    @Override
    public void onFragmentInteraction() {
        if (adapter != null) {
            adapter.setmTwoPane(true);
        }
    }
}
