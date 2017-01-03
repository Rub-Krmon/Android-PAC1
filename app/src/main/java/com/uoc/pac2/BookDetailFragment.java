package com.uoc.pac2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

//    Fragment que se utiliza para mostrar el detalle de un BookItem
//    recibe como parámetro un BookItem en su constructora, y añade
//    sus atributos como argumentos para el frament y ser mostrados.

public class BookDetailFragment extends Fragment {

    private BookContent.BookItem selectedBook = null;

    private OnFragmentInteractionListener mListener;
    private WebView webView;
    private FloatingActionButton fab;

    public BookDetailFragment() {
    }

    public static BookDetailFragment newInstance(BookContent.BookItem pBookItem) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();

        args.putLong(BookContent.BookItem.BOOK_ID, pBookItem.getId() != null ? pBookItem.getId() : 0);
        args.putString(BookContent.BookItem.BOOK_AUTHOR, pBookItem.getAuthor());
        args.putString(BookContent.BookItem.BOOK_TITLE, pBookItem.getTitle());
        args.putString(BookContent.BookItem.BOOK_DESCRIPTION, pBookItem.getDescription());
        args.putString(BookContent.BookItem.BOOK_PUBLICATION_DATE, pBookItem.getPublication_date());
        args.putString(BookContent.BookItem.BOOK_URL_IMAGE, pBookItem.getUrl_image());

        fragment.setArguments(args);
        return fragment;
    }

//    En caso de que se reciban argumetos, estos se incluyen en el objeto BookItem
//    selectedBook para su posterior consulta.

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BookContent.BookItem.BOOK_ID)) {
            selectedBook = new BookContent.BookItem();
            selectedBook.setId(getArguments().getLong(BookContent.BookItem.BOOK_ID, 0));
            selectedBook.setAuthor(getArguments().getString(BookContent.BookItem.BOOK_AUTHOR));
            selectedBook.setTitle(getArguments().getString(BookContent.BookItem.BOOK_TITLE));
            selectedBook.setDescription(getArguments().getString(BookContent.BookItem.BOOK_DESCRIPTION));
            selectedBook.setPublication_date(getArguments().getString(BookContent.BookItem.BOOK_PUBLICATION_DATE));
            selectedBook.setUrl_image(getArguments().getString(BookContent.BookItem.BOOK_URL_IMAGE));

        } else {
            selectedBook = null;
        }
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }

    }

    //    En caso de que la URL de la imagen esté informada, se hace uso de la tarea
//    asíncrona DonwloadImageTask para realizar la descarga de la imagen asociada
//    en paralelo.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);

        if (selectedBook != null) {
            ((TextView) v.findViewById(R.id.textView_author)).setText(selectedBook.getAuthor());
            ((TextView) v.findViewById(R.id.textView_date)).setText(selectedBook.getPublication_date());
            ((TextView) v.findViewById(R.id.textView_description)).setText(selectedBook.getDescription());

            if (selectedBook.getUrl_image() != null && !selectedBook.getUrl_image().isEmpty()) {

                new DownloadImageTask((ImageView) v.findViewById(R.id.imageView_cover))
                        .execute(selectedBook.getUrl_image());
            }
        }
        // Get the WebView
        webView = (WebView) v.findViewById(R.id.fragment_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVisibility(View.GONE);
        webView.loadUrl("file:///android_asset/form.html");
        webView.setWebViewClient(new MyAppWebViewClient());

        fab = (FloatingActionButton) v.findViewById(R.id.fragment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("file:///android_asset/form.html");
                webView.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
    * Intefaz Listener para informar de la presencia
    * del Fragment al RecyclerView BookListAdapter
    */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(Uri.parse(url).getQueryParameter("name")) || TextUtils.isEmpty(Uri.parse(url).getQueryParameter("num")) || TextUtils.isEmpty(Uri.parse(url).getQueryParameter("date"))) {
                Toast.makeText(BookDetailFragment.this.getActivity(), R.string.error_empty_form_fields, Toast.LENGTH_LONG).show();
            } else {
                webView.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                Toast.makeText(BookDetailFragment.this.getActivity(), R.string.common_message_buy_ok, Toast.LENGTH_LONG).show();
            }

            return false;
        }
    }
}
