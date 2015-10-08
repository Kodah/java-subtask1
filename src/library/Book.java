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

    public Book(String name){
        title = name;
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

}
