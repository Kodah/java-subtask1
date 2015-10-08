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
public class SetOfMembers extends ArrayList<Member> {
    
    void addMember(Member aMember) {
        super.add(aMember);

    }
    
    void removeMember(Member aMember){
        super.remove(aMember);
    }
}
