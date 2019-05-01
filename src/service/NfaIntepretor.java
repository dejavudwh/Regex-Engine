package service;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import input.Input;
import nfa.Nfa;

public class NfaIntepretor {

	private Nfa start = null;
	private Input input = null;
	
	public NfaIntepretor(Nfa start, Input input) {
		this.start = start;
		this.input = input;
	}
	
	private Set<Nfa> e_closure(Set<Nfa> input) {
		System.out.print("¦Å-Closure( " + strFromNfaSet(input) + " ) = ");
		
		Stack<Nfa> nfaStack = new Stack<Nfa>();
		if(input == null || input.isEmpty()) {
			return null;
		}
		
		Iterator<Nfa> it = input.iterator();
		while(it.hasNext()) {
			nfaStack.add(it.next());
		}
		
		while(!nfaStack.isEmpty()) {
			Nfa n = nfaStack.pop();
			
			if(n.next != null && n.getEdge() == Nfa.EPSILON) {
				if(!input.contains(n.next)) {
					nfaStack.push(n.next);
					input.add(n.next);
				}
			}
			
			if(n.next2 != null && n.getEdge() == Nfa.EPSILON) {
				if(!input.contains(n.next2)) {
					nfaStack.push(n.next2);
					input.add(n.next2);
				}
			}
		}
		
		if (input != null) {
    		System.out.println("{ " + strFromNfaSet(input) + " }");
    	}
    	
    	return input;
	}

	private String strFromNfaSet(Set<Nfa> input) {
		String s = "";
    	Iterator<Nfa> it = input.iterator();
    	while (it.hasNext()) {
    		s += ((Nfa)it.next()).getStateNum();
    		if (it.hasNext()) {
    			s += ",";
    		}
    	}
    	
    	return s;
	}
}
