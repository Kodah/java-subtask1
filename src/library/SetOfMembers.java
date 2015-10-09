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
    
    SetOfMembers getMemberFromName(String name)
    {
        SetOfMembers filteredMembers = new SetOfMembers();
        for (Member member : this) {
            if (member.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredMembers.add(member);
            }
        }
        return filteredMembers;
    }

    SetOfMembers getMemberFromNumber(String number)
    {
        SetOfMembers filteredMembers = new SetOfMembers();
        for (Member member : this){
            if (Integer.toString(member.getMemberNumber()).contains(number)) {
                filteredMembers.add(member);
            }
        }
        return filteredMembers;
    }
}
