/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.util.ArrayList;

/**
 *
 * @author Kutoma
 */
public class SetOfBooks extends ArrayList<Book> {

    public SetOfBooks() {

        super();
    }

    public void addBook(Book aBook) {
        super.add(aBook);
    }

    public void removeBook(Book aBook) {
        super.remove(aBook);
    }
    
    ArrayList<Book> availableBooks()
    {
        ArrayList<Book> availableBooks = new ArrayList<Book>();
        for (Book book : this) 
        {
            if (book.getBorrower() == null) 
            {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

}
