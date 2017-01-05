package com.uoc.pac2;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.orm.SugarContext;
import com.uoc.pac2.adapters.BookListAdapter;
import com.uoc.pac2.model.BookContent;
import com.uoc.pac2.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.content.Intent.ACTION_DELETE;
import static com.uoc.pac2.utils.Constants.ACTION_VIEW_BOOK_DETAIL;

/**
 * @author Ruben Carmona
 * @project TFM - PAC4
 * @date 10/2016
 */

public class BookListActivity extends AppCompatActivity implements BookDetailFragment.OnFragmentInteractionListener {

    private final String TAG = this.getClass().getCanonicalName();
    boolean mTwoPane = false;
    ClipboardManager clipboard;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BookListAdapter adapter;
    private ArrayList<BookContent.BookItem> bookList = new ArrayList<>();
    private FirebaseAuth mAuth = null;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CharSequence pasteData = getResources().getText(R.string.text_to_share);

        setContentView(R.layout.book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SugarContext.init(getApplicationContext());

        firebaseLogin();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            bookList.addAll(BookContent.getBooks());
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        if (findViewById(R.id.content_book_detail) != null && bookList.size() > 0) {
            mTwoPane = true;
            BookDetailFragment aBookDetailFragment = BookDetailFragment.newInstance(bookList.get(0));

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_book_detail, aBookDetailFragment)
                    .commit();

        }

        adapter = new BookListAdapter(this, bookList);
        adapter.setmTwoPane(mTwoPane);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayoutBookList);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.lightBackground,
                R.color.hintColorLight);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (firebaseUser != null) {
                            BookContent.BookItem.deleteAll(BookContent.BookItem.class);
                            bookList.clear();
                            adapter.setBooks(bookList);
                            addValueEventListenerToDatabase();
                        }
                    }
                });

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

//if you want to update the items at a later time it is recommended to keep it in a variable
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_other_apps).withTag("");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_copy_clipboard);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_whatsapp);

// Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.lightBackground)
                .addProfiles(
                        new ProfileDrawerItem().withName(getResources().getString(R.string.firebase_username)).withEmail(getResources().getString(R.string.firebase_email)).withIcon(getResources().getDrawable(R.drawable.ic_default_user))
                )
                .build();
//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch ((int) drawerItem.getIdentifier()) {
                            case 2:
                                ClipData clipData = ClipData.newPlainText(pasteData, pasteData);
                                clipboard.setPrimaryClip(clipData);
                                Toast.makeText(BookListActivity.this, clipboard.getPrimaryClip().getItemAt(0).getText(), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Uri imageUri = null;

                                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
                                File sharedDir = new File(root + File.separator + "shared_images");

                                if (!sharedDir.exists()) {
                                    sharedDir.mkdirs();
                                }

                                String sharedImageName = "sharedImage.png";
                                File sharedImageFile = new File(sharedDir, sharedImageName);

                                if (sharedImageFile.exists())
                                    sharedImageFile.delete();

                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(sharedImageFile);

                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_user);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();

                                    imageUri = Uri.fromFile(sharedImageFile);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.drawer_item_other_apps));
                                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                sendIntent.setType("image/*");

                                if (drawerItem.getIdentifier() == 3) {
                                    sendIntent.setPackage("com.whatsapp");
                                }

                                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.drawer_item_other_apps)));
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void firebaseLogin() {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(this.getString(R.string.firebase_email), this.getString(R.string.firebase_password)).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Log.e(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(BookListActivity.this, R.string.firebase_email, Toast.LENGTH_SHORT).show();
                    adapter.setBooks(BookContent.getBooks());

                } else {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        Log.i(TAG, "Logged");
                        databaseReference = FirebaseDatabase.getInstance().getReference("books");

                        addValueEventListenerToDatabase();
                    } else {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(BookListActivity.this, R.string.firebase_email, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void addValueEventListenerToDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                GenericTypeIndicator<ArrayList<BookContent.BookItem>> typeIndicator = new GenericTypeIndicator<ArrayList<BookContent.BookItem>>() {
                };

                ArrayList<BookContent.BookItem> listOfBook = dataSnapshot.getValue(typeIndicator);
                ArrayList<BookContent.BookItem> aBookList = new ArrayList<>();

                for (BookContent.BookItem book : listOfBook) {

                    if (!BookContent.exists(book)) {
                        book.save();
                    } else {
                        book.update();
                    }
                    if (book.getId() == null) {
                        book.setId(Long.valueOf(aBookList.size()));
                    }
                    aBookList.add(book);
                }

                adapter.setBooks(aBookList);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The read failed: " + databaseError.getCode());
                adapter.setBooks(BookContent.getBooks());
                databaseReference.removeEventListener(this);
            }
        });
    }

    @Override
    public void onFragmentInteraction() {
        if (adapter != null) {
            adapter.setmTwoPane(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase(ACTION_DELETE)) {
                // Eliminamos la notifcación mostrada
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.cancel(Constants.NOTIFICATION_ID);

                Integer bookPostion = Integer.valueOf(intent.getStringExtra(Constants.BOOK_POSITION));
                String toastDeleteMessage = intent.getStringExtra(Constants.TOAST_DELETE_MESSAGE);

                if (bookPostion > -1 && adapter.getItemCount() > bookPostion) {
                    BookContent.removeBookItem(bookPostion);
                    adapter.removeBookItem(bookPostion);
                    Toast.makeText(BookListActivity.this, toastDeleteMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookListActivity.this, getString(R.string.error_message_delete_book), Toast.LENGTH_LONG).show();
                }

            } else if (intent.getAction().equalsIgnoreCase(ACTION_VIEW_BOOK_DETAIL)) {

                // Eliminamos la notifcación mostrada
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.cancel(Constants.NOTIFICATION_ID);

                // Acción de visualizar un libro recibida
                Integer bookPostion = Integer.valueOf(intent.getStringExtra(Constants.BOOK_POSITION));

                if (bookPostion > -1 && adapter.getItemCount() > bookPostion) {

                    if (mTwoPane) {
                        BookDetailFragment aBookDetailFragment = BookDetailFragment.newInstance(bookList.get(bookPostion));

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_book_detail, aBookDetailFragment)
                                .commit();
                    } else {
                        BookContent.BookItem aBookItem = bookList.get(bookPostion);
                        Intent intentBookDetail = new Intent(getApplicationContext(), BookDetailActivity.class);
                        intentBookDetail.putExtra(BookContent.BookItem.BOOK_ID, aBookItem.getId());
                        intentBookDetail.putExtra(BookContent.BookItem.BOOK_AUTHOR, aBookItem.getAuthor());
                        intentBookDetail.putExtra(BookContent.BookItem.BOOK_TITLE, aBookItem.getTitle());
                        intentBookDetail.putExtra(BookContent.BookItem.BOOK_DESCRIPTION, aBookItem.getDescription());
                        intentBookDetail.putExtra(BookContent.BookItem.BOOK_PUBLICATION_DATE, aBookItem.getPublication_date());
                        intentBookDetail.putExtra(BookContent.BookItem.BOOK_URL_IMAGE, aBookItem.getUrl_image());

                        startActivity(intentBookDetail);
                    }
                } else {
                    Toast.makeText(BookListActivity.this, getString(R.string.error_message_delete_book), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
