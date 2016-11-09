package com.uoc.pac2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uoc.pac2.BookDetailActivity;
import com.uoc.pac2.BookDetailFragment;
import com.uoc.pac2.R;
import com.uoc.pac2.model.BookContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private final static int EVEN = 0;
    private final static int ODD = 1;
    private ArrayList<BookContent.BookItem> mBookItems;

    private Context mContext;
    private boolean mTwoPane = false;

    // Constructor donde se pasan los items y el contexto
    public BookListAdapter(Context context, ArrayList<BookContent.BookItem> pBooksItems) {
        this.mContext = context;
        this.mBookItems = pBooksItems;
    }

    public void setBooks(List<BookContent.BookItem> pNewBooks) {
        this.mBookItems.addAll(pNewBooks);
        this.notifyDataSetChanged();
    }

    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        // En función del tipo de vista se muestra
        // un layout u otro
        switch (viewType) {
            case EVEN:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_book_list_detail_even, parent, false);
                break;
            case ODD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_book_list_detail_odd, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_book_list_detail_even, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookListAdapter.ViewHolder pHolder, final int pPosition) {

        pHolder.getmView().setTag(pPosition);

        pHolder.setmItem(mBookItems.get(pPosition));
        pHolder.getmTitleView().setText(mBookItems.get(pPosition).getTitle());
        pHolder.getmAuthorView().setText(mBookItems.get(pPosition).getAuthor());

        pHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();

                if (mTwoPane) {
                    BookDetailFragment aBookDetailFragment = BookDetailFragment.newInstance(currentPos);
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_book_detail, aBookDetailFragment)
                            .commit();
                } else {
                    BookContent.BookItem aBookItem = mBookItems.get(currentPos);
                    Intent intent = new Intent(mContext, BookDetailActivity.class);
                    intent.putExtra(BookContent.BookItem.BOOK_ID, aBookItem.getId());
                    intent.putExtra(BookContent.BookItem.BOOK_AUTHOR, aBookItem.getAuthor());
                    intent.putExtra(BookContent.BookItem.BOOK_TITLE, aBookItem.getTitle());
                    intent.putExtra(BookContent.BookItem.BOOK_DESCRIPTION, aBookItem.getDescription());
                    intent.putExtra(BookContent.BookItem.BOOK_PUBLICATION_DATE, aBookItem.getPublication_date());
                    intent.putExtra(BookContent.BookItem.BOOK_URL_IMAGE, aBookItem.getUrl_image());

                    mContext.startActivity(intent);
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

    public void setmTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }

    /*
    * Contiene la referencia a los elementos que se muestran en
    * cada una de las filas que componen la lista.
    */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private BookContent.BookItem mItem;
        private TextView mTitleView, mAuthorView;
        private View mView;

        public ViewHolder(View pView) {
            super(pView);
            pView.setClickable(true);
            mTitleView = (TextView) pView.findViewById(R.id.book_title);
            mAuthorView = (TextView) pView.findViewById(R.id.book_author);
            mView = pView;
        }

        public void setmItem(BookContent.BookItem mItem) {
            this.mItem = mItem;
        }

        public TextView getmTitleView() {
            return mTitleView;
        }

        public TextView getmAuthorView() {
            return mAuthorView;
        }

        public View getmView() {
            return mView;
        }

        public void setmAuthorView(TextView mAuthorView) {
            this.mAuthorView = mAuthorView;
        }
    }
}
