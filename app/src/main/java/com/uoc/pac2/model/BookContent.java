package com.uoc.pac2.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public static final ArrayList<BookItem> ITEMS = new ArrayList<>();

    /*
    * Variable estática de una lista de elementos
    * para falsear los datos a mostrar
    */
    static {
        BookItem book1 = new BookItem(0, "Title1", "Author1", new Date(), "Description", null);
        BookItem book2 = new BookItem(1, "Title2", "Author2", new Date(), "Description 2", null);
        ITEMS.add(book1);
        ITEMS.add(book2);
    }

    public static class BookItem {
        public int id;
        public String title;
        public String author;
        public Date publication_date;
        public String description;
        public String url_image;

        public BookItem() {

        }

        public BookItem(int pId, String pTitle, String pAuthor, Date pPublishedDate, String pDescription, String pUrlImage) {
            this.id = pId;
            this.title = pTitle;
            this.author = pAuthor;
            this.publication_date = pPublishedDate;
            this.description = pDescription;
            this.url_image = pUrlImage;
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

        public Date getPublication_date() {
            return publication_date;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl_image() {
            return url_image;
        }

        public void setPublication_date(String pString) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                this.publication_date = formatter.parse(pString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

