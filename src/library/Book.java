/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

/**
 *
 * @author Kutoma
 */
public class Book {

    private String title;
    private Member borrower = null;
    private static int bookCount = 0;
    private int accessionNumber;
    private String iSBNNumber;
    private String author;

    public Book(String name) {
        title = name;
        accessionNumber = bookCount++;

    }

    public Book(String name, String author, String iSBNNumber) {
        title = name;
        this.author = author;
        this.iSBNNumber = iSBNNumber;
        accessionNumber = bookCount++;
    }

    void setBorrower(Member theBorrower) {
        borrower = theBorrower;
    }

    Member getBorrower() {
        return borrower;
    }

    String getTitle() {
        return title;
    }
    
    String getAuthor()
    {
        return author;
    }
    
    String getISBNNumber()
    {
        return iSBNNumber;
    }
    
    int getAccessionNumber()
    {
        return accessionNumber;
    }
    

}
