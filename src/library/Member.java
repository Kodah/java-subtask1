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
public class Member implements Serializable {

    private String name;
    private int memberNumber;
    private SetOfBooks currentLoans = new SetOfBooks();
    private static int memberNumberCounter = 0;

    public Member(String aName) {
        name = aName;
        memberNumber = memberNumberCounter++;
        currentLoans = new SetOfBooks();
    }

    @Override
    public String toString() {
        return Integer.toString(memberNumber) + " " + name;

    }

    public void borrowBook(Book aBook) {
        if (currentLoans.size() < 3) {
            currentLoans.addBook(aBook);
            aBook.setBorrower(this);
        }
    }

    public void returnBook(Book aBook) {
        currentLoans.removeBook(aBook);
        aBook.setBorrower(null);
    }

    public Book[] getBooksOnLoan() {
        if (currentLoans.size() > 0) {
            return currentLoans.toArray(new Book[currentLoans.size()]);
        } else {
            return new Book[0];
        }
    }

    public String getName() {
        return name;
    }

    public int getMemberNumber() {
        return memberNumber;
    }

    public static void setMemberNumberCounter(int count) {
        memberNumberCounter = count + 1;
    }
}
