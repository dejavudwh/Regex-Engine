package service;

import java.util.HashSet;
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

	private Set<Nfa> move(Set<Nfa> input, char c) {
		Set<Nfa> outSet = new HashSet<Nfa>();
		Iterator<Nfa> it = input.iterator();
		
		while(it.hasNext()) {
			Nfa n = it.next();
			
			int stateNum = n.getStateNum();
			Set<Byte> s = n.inputSet;
			Byte cb = (byte)c;
			
			if(n.getEdge() == c || (n.getEdge() == Nfa.CCL && n.inputSet.contains(cb))) {
				outSet.add(n.next);
			}
			
			if(outSet != null) {
				System.out.print("move({ " + strFromNfaSet(input) + " }, '" + c + "')= ");
	        	System.out.println("{ " + strFromNfaSet(outSet) + " }");
			}
		}
		
		return outSet;
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
