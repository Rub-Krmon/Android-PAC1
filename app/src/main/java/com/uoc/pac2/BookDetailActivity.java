package com.uoc.pac2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uoc.pac2.model.BookContent;
import com.uoc.pac2.utils.DownloadImageTask;

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


        BookContent.BookItem selectedBook = new BookContent.BookItem();

        selectedBook.setId(getIntent().getLongExtra(BookContent.BookItem.BOOK_ID, 0));
        selectedBook.setAuthor(getIntent().getStringExtra(BookContent.BookItem.BOOK_AUTHOR));
        selectedBook.setTitle(getIntent().getStringExtra(BookContent.BookItem.BOOK_TITLE));
        selectedBook.setDescription(getIntent().getStringExtra(BookContent.BookItem.BOOK_DESCRIPTION));
        selectedBook.setPublication_date(getIntent().getStringExtra(BookContent.BookItem.BOOK_PUBLICATION_DATE));
        selectedBook.setUrl_image(getIntent().getStringExtra(BookContent.BookItem.BOOK_URL_IMAGE));

        if (selectedBook != null && selectedBook.getTitle() != null && !selectedBook.getTitle().isEmpty()) {
            setTitle(selectedBook.getTitle());
            ((TextView) findViewById(R.id.textView_author)).setText(selectedBook.getAuthor());
            ((TextView) findViewById(R.id.textView_date)).setText(selectedBook.getPublication_date().toString());
            ((TextView) findViewById(R.id.textView_description)).setText(selectedBook.getDescription());

            if ( selectedBook.getUrl_image() != null && !selectedBook.getUrl_image().isEmpty()) {

                new DownloadImageTask((ImageView) findViewById(R.id.imageView_cover))
                        .execute(selectedBook.getUrl_image());
            }

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
