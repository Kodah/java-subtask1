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
    
    SetOfBooks findBookByAuthor (String author)
    {
        SetOfBooks filteredBooks = new SetOfBooks();
        for (Book book : this) {
            if (book.getAuthor().contains(author)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
    
    SetOfBooks findBookByTitle (String title)
    {
        SetOfBooks filteredBooks = new SetOfBooks();
        for (Book book : this) {
            if (book.getTitle().contains(title)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
    
    SetOfBooks findBookByAccNum (String accNum)
    {
        SetOfBooks filteredBooks = new SetOfBooks();
        for (Book book : this) {
            if (Integer.toString(book.getAccessionNumber()).contains(accNum)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
    
    SetOfBooks findBookByISBN (String ISBN)
    {
        SetOfBooks filteredBooks = new SetOfBooks();
        for (Book book : this) {
            if (book.getISBNNumber().contains(ISBN)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

}
