package com.uoc.tfm.pac1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uoc.tfm.pac1.model.BookContent;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookDetailFragment extends Fragment {

    public static final String BOOK_POSITION = "bookPosition";

    private BookContent.BookItem selectedItem = null;

    private OnFragmentInteractionListener mListener;

    public BookDetailFragment() {
    }

    public static BookDetailFragment newInstance(int pParam1) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putInt(BOOK_POSITION, pParam1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(BOOK_POSITION)) {
            selectedItem = BookContent.ITEMS.get(getArguments().getInt(BOOK_POSITION));
        } else {
            selectedItem = null;
        }
        if (mListener != null) {
            mListener.onFragmentInteraction("Hello world");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);

        if (selectedItem != null) {
            ((TextView) v.findViewById(R.id.textView_author)).setText(selectedItem.getAuthor());
            ((TextView) v.findViewById(R.id.textView_date)).setText(selectedItem.getPublishedDate().toString());
            ((TextView) v.findViewById(R.id.textView_description)).setText(selectedItem.getDescription());
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String message);
    }
}
