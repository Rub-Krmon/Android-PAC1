package com.uoc.pac2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uoc.pac2.model.BookContent;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookDetailFragment extends Fragment {

    public static final String BOOK_POSITION = "bookPosition";

    private BookContent.BookItem selectedItem = null;

    private OnFragmentInteractionListener mListener;

    /*
    * Variable temporal para esta entrega que sirve para
    * mostrar una portada u otra en función de si es par o
    * impar la posición del elemento en la lista
    */
    private int mBookCoverEvenOrOdd = 0;

    public BookDetailFragment() {
    }

    public static BookDetailFragment newInstance(int pParam) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putInt(BOOK_POSITION, pParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(BOOK_POSITION)) {
            selectedItem = BookContent.ITEMS.get(getArguments().getInt(BOOK_POSITION));
            mBookCoverEvenOrOdd = getArguments().getInt(BOOK_POSITION) % 2;
        } else {
            selectedItem = null;
        }
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);

        if (selectedItem != null) {
            ((TextView) v.findViewById(R.id.textView_author)).setText(selectedItem.getAuthor());
            ((TextView) v.findViewById(R.id.textView_date)).setText(selectedItem.getPublication_date().toString());
            ((TextView) v.findViewById(R.id.textView_description)).setText(selectedItem.getDescription());
            ((ImageView) v.findViewById(R.id.imageView_cover)).setImageResource(mBookCoverEvenOrOdd == 0 ? R.drawable.even_cover : R.drawable.odd_cover);
        }

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
}
