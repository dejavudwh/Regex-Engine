package Dfa;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nfa.Nfa;

public class Dfa {

	private static int STATE_NUM = 0;
	
	int stateNum = 0;
	public Set<Nfa> nfaStates = new HashSet<Nfa>();
	boolean accepted = false;
	
	public static Dfa getDfaFromNfaSet(Set<Nfa> input) {
		Dfa dfa = new Dfa();
		Iterator<Nfa> it = input.iterator();
		
		while(it.hasNext()) {
			Nfa nfa = it.next();
			dfa.nfaStates.add(nfa);
		
			if(nfa.next == null && nfa.next2 == null) {
				dfa.accepted = true;
			}
		}
		
		dfa.stateNum = STATE_NUM;
    	STATE_NUM++;
    	return dfa;
	}
	
	public boolean hasNfaStates(Set<Nfa> set) {
		if(this.nfaStates.equals(set)) {
			return true;
		}
		
		return false;
	}
}
