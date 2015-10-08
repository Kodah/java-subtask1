/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.List;
import java.util.ArrayList;
/**
 *
 * @author Kutoma
 */
public class SetOfBooks extends ArrayList<Book> {


public SetOfBooks(){

    super();
}

public void addBook(Book aBook){
    super.add(aBook);
}

public void removeBook(Book aBook){
    super.remove(aBook);
}

}
