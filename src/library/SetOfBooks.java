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
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
    
    SetOfBooks findBookByTitle (String title)
    {
        SetOfBooks filteredBooks = new SetOfBooks();
        for (Book book : this) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
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
            if (book.getISBNNumber().toLowerCase().contains(ISBN.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public int maxAccessionNumber()
    {
        int maxNum = 0;        
        for (Book book : this) 
        {
            int bookNum = book.getAccessionNumber();
            if (bookNum >= maxNum) 
            {
                maxNum = bookNum;
            }
        }
        return maxNum;
    }
}
