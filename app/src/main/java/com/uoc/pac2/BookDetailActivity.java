package com.uoc.pac2;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uoc.pac2.model.BookContent;
import com.uoc.pac2.utils.DownloadImageTask;

/**
 * @author Ruben Carmona
 * @project TFM - PAC4
 * @date 10/2016
 */

//    Actividad de detalle de un libro en el que se recibe
//    como información dentro del Extra del intent todos las
//    atributos que conforman un objeto BookItem para ser
//    mostrados en el layout.

//    En caso de en la información venga la URL de la imagen de
//    la portada del libro, se ejecutará la tarea asíncrona
//    DownloadImageTask que será la encargada de actualizar
//    el imageView relacionado.
public class BookDetailActivity extends AppCompatActivity {

    private WebView webView;
    private FloatingActionButton fab;

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

        if (selectedBook.getTitle() != null && !selectedBook.getTitle().isEmpty()) {
            setTitle(selectedBook.getTitle());
            ((TextView) findViewById(R.id.textView_author)).setText(selectedBook.getAuthor());
            ((TextView) findViewById(R.id.textView_date)).setText(selectedBook.getPublication_date());
            ((TextView) findViewById(R.id.textView_description)).setText(selectedBook.getDescription());

            if (selectedBook.getUrl_image() != null && !selectedBook.getUrl_image().isEmpty()) {

                new DownloadImageTask((ImageView) findViewById(R.id.imageView_cover))
                        .execute(selectedBook.getUrl_image());
            }

        }
        // Get the WebView
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVisibility(View.GONE);
        webView.loadUrl("file:///android_asset/form.html");
        webView.setWebViewClient(new MyAppWebViewClient());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("file:///android_asset/form.html");
                webView.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
            }
        });
    }

    class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(Uri.parse(url).getQueryParameter("name")) || TextUtils.isEmpty(Uri.parse(url).getQueryParameter("num")) || TextUtils.isEmpty(Uri.parse(url).getQueryParameter("date"))) {
                Toast.makeText(BookDetailActivity.this, R.string.error_empty_form_fields, Toast.LENGTH_LONG).show();
            } else {
                webView.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                Toast.makeText(BookDetailActivity.this, R.string.common_message_buy_ok, Toast.LENGTH_LONG).show();
            }

            return false;
        }
    }
}
