package com.uoc.tfm.pac1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uoc.tfm.pac1.R;
import com.uoc.tfm.pac1.model.BookContent;

import java.util.ArrayList;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private final ArrayList<BookContent.BookItem> mBookItems;

    Context mContext;
    private final static int EVEN = 0;
    private final static int ODD = 1;

    private boolean mTwoPane = false;

    // Constructor donde se pasan los items y el contexto
    public BookListAdapter(Context context, ArrayList<BookContent.BookItem> pBooksItems) {
        this.mContext = context;
        this.mBookItems = pBooksItems;
    }

    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_list_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookListAdapter.ViewHolder holder, int position) {
        holder.mItem = mBookItems.get(position);
        holder.mTitleView.setText(mBookItems.get(position).getTitle());
        holder.mAuthorView.setText(mBookItems.get(position).getAuthor());
        // ============ INICI CODI A COMPLETAR =============== // Guarda la posició actual a la vista del holder
// ============ FI CODI A COMPLETAR =================
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos;
// ============ INICI CODI A COMPLETAR =============== // Obtindre la posició del llibre on s’ha fet click de la vista
// ============ FI CODI A COMPLETAR =================
                if (mTwoPane) {
// ============ INICI CODI A COMPLETAR ===============
// Iniciar el fragment corresponent a tablet, enviant l’argument de la posició seleccionada // ============ FI CODI A COMPLETAR =================
                } else {
                    // ============ INICI CODI A COMPLETAR ===============
// Iniciar l’activitat corresponent a mòbil, enviant l’argument de la posició seleccionada // ============ FI CODI A COMPLETAR =================
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mBookItems != null ? this.mBookItems.size() : 0;
    }

    @Override
    public int getItemViewType(int pPosition) {
        return pPosition % 2 == 0 ? EVEN : ODD;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BookContent.BookItem mItem;
        private TextView mTitleView, mAuthorView;
        private View mView;

        public ViewHolder(View pView) {
            super(pView);
            mTitleView = (TextView) pView.findViewById(R.id.book_title);
            mAuthorView = (TextView) pView.findViewById(R.id.book_author);
            mView = pView;
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // obtiene la posición del item
        }

        public BookContent.BookItem getmItem() {
            return mItem;
        }

        public void setmItem(BookContent.BookItem mItem) {
            this.mItem = mItem;
        }

        public TextView getmTitleView() {
            return mTitleView;
        }

        public void setmTitleView(TextView mTitleView) {
            this.mTitleView = mTitleView;
        }

        public TextView getmAuthorView() {
            return mAuthorView;
        }

        public void setmAuthorView(TextView mAuthorView) {
            this.mAuthorView = mAuthorView;
        }
    }
}