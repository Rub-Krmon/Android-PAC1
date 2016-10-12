package com.uoc.tfm.pac1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uoc.tfm.pac1.model.BookContent;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BookContent.BookItem selectedItem = BookContent.ITEMS.get(getIntent().getIntExtra(BookDetailFragment.BOOK_POSITION, 0));

        int mBookCoverEvenOrOdd = getIntent().getIntExtra(BookDetailFragment.BOOK_POSITION, 0) % 2;

        if (selectedItem != null) {
            setTitle(selectedItem.getTitle());
            ((TextView) findViewById(R.id.textView_author)).setText(selectedItem.getAuthor());
            ((TextView) findViewById(R.id.textView_date)).setText(selectedItem.getPublishedDate().toString());
            ((TextView) findViewById(R.id.textView_description)).setText(selectedItem.getDescription());
            ((ImageView) findViewById(R.id.imageView_cover)).setImageResource(mBookCoverEvenOrOdd == 0 ? R.drawable.even_cover : R.drawable.odd_cover);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
