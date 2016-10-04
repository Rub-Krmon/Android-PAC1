package com.uoc.tfm.pac1.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */


public class BookContent {

    public static final ArrayList<BookItem> ITEMS = new ArrayList<>();

    static {
        BookItem book1 = new BookItem(0, "Title1", "Author1", new Date(), "Description", null);
        BookItem book2 = new BookItem(1, "Title2", "Author2", new Date(), "Description 2", null);
        ITEMS.add(book1);
        ITEMS.add(book2);
    }

    public static class BookItem {
        private int id;
        private String title;
        private String author;
        private Date publishedDate;
        private String description;
        private String frontImageURL;

        public BookItem(int pId, String pTitle, String pAuthor, Date pPublishedDate, String pDescription, String pFrontImageURL) {
            this.id = pId;
            this.title = pTitle;
            this.author = pAuthor;
            this.publishedDate = pPublishedDate;
            this.description = pDescription;
            this.frontImageURL = pFrontImageURL;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public Date getPublishedDate() {
            return publishedDate;
        }

        public String getDescription() {
            return description;
        }

        public String getFrontImageURL() {
            return frontImageURL;
        }
    }
}

