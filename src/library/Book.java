/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.io.Serializable;

/**
 *
 * @author Kutoma
 */
public class Book implements Serializable{

    private String title;
    private Member borrower = null;
    private static int accessionCounter = 0;
    private int accessionNumber;
    private String iSBNNumber;
    private String author;

    public Book(String name) {
        title = name;
        accessionNumber = accessionCounter++;

    }

    public Book(String name, String author, String iSBNNumber) {
        title = name;
        this.author = author;
        this.iSBNNumber = iSBNNumber;
        accessionNumber = accessionCounter++;
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
    
    public static void setAccessionCounter(int count)
    {
        accessionCounter = count;
    }
}
