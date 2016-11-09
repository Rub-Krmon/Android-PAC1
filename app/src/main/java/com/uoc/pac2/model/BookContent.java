package com.uoc.pac2.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

/**
 * Clase que representa los elementos libro
 * a utilizar en la aplicación
 */
public class BookContent {

    public BookContent() {
    }

    public static List<BookItem> getBooks() {
        // ============ INICI CODI A COMPLETAR ===============
        return BookItem.listAll(BookItem.class);
        // ============ FI CODI A COMPLETAR ===============
    }

    public static boolean exists(BookItem bookItem) {
        // ============ INICI CODI A COMPLETAR ===============
        List<BookItem> list = getBooks();

        if (list != null && !list.isEmpty()) {
            List<BookItem> listOfBooks = BookItem.find(BookItem.class, "title = ? ", bookItem.getTitle());
            return listOfBooks != null && !listOfBooks.isEmpty();
        } else {
            return false;
        }
        // ============ FI CODI A COMPLETAR ===============
    }

    public static class BookItem extends SugarRecord {

        public static final String BOOK_ID = "bookID";
        public static final String BOOK_TITLE = "bookTitle";
        public static final String BOOK_AUTHOR = "bookAuthor";
        public static final String BOOK_PUBLICATION_DATE = "bookPublicationDate";
        public static final String BOOK_DESCRIPTION = "bookDescription";
        public static final String BOOK_URL_IMAGE = "bookUrlImage";

        public Long id;
        public String title;
        public String author;
        public String publication_date;
        public String description;
        public String url_image;

        public BookItem() {

        }

        public BookItem(Long pId, String pTitle, String pAuthor, String pPublishedDate, String pDescription, String pUrlImage) {
            this.id = pId;
            this.title = pTitle;
            this.author = pAuthor;
            this.publication_date = pPublishedDate;
            this.description = pDescription;
            this.url_image = pUrlImage;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getPublication_date() {
            return publication_date;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl_image() {
            return url_image;
        }

        public void setPublication_date(String publication_date) {
            this.publication_date = publication_date;
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            try {
//                this.publication_date = formatter.parse(pString);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setUrl_image(String url_image) {
            this.url_image = url_image;
        }
    }
}

