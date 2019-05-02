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
	
	public boolean debug = true;
	
	public NfaIntepretor(Nfa start, Input input) {
		this.start = start;
		this.input = input;
	}
	
	public void intepretNfa() {
		System.out.println("Input string: ");
		input.newInput(null);
		input.advance();
    	input.pushback(1);
    	
    	Set<Nfa> next = new HashSet<Nfa>();
    	next.add(start);
    	e_closure(next);
    	
    	Set<Nfa> current = null;
    	char c;
    	String inputStr = "";
    	boolean lastAccepted = false;
    	
    	while((c = (char)input.advance()) != Input.EOF) {
    		current = move(next, c);
    		next = e_closure(current);
    		
    		if(next != null) {
    			if(hasAcceptedState(next)) {
    				lastAccepted = true;
    			}
    		}
    		else {
    			break;
    		}
    		
    		inputStr += c;
    	}
    	
    	if (lastAccepted) {
    		System.out.println("The Nfa Machine can recognize string: " + inputStr);
    	}
	}
	
	private boolean hasAcceptedState(Set<Nfa> next) {
		boolean isAccepted = false;
		
		if(next == null || next.isEmpty()) {
			return false;
		}
		
		String acceptedStatement = "Accept State: ";
    	Iterator<Nfa> it = next.iterator();
    	while (it.hasNext()) {
    		Nfa p = (Nfa)it.next();
    		if (p.next == null && p.next2 == null) {
    			isAccepted = true;
    			acceptedStatement += p.getStateNum() + " ";
    		}
    	}

    	if (isAccepted) {
    		System.out.println(acceptedStatement);
    	}

    	return isAccepted;
    }

	public Set<Nfa> e_closure(Set<Nfa> input) {
		if (debug)
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
		
		if (input != null && debug) {
    		System.out.println("{ " + strFromNfaSet(input) + " }");
    	}
    	
    	return input;
	}

	public Set<Nfa> move(Set<Nfa> input, char c) {
		Set<Nfa> outSet = new HashSet<Nfa>();
		Iterator<Nfa> it = input.iterator();
		
		while(it.hasNext()) {
			Nfa n = it.next();
			
			Byte cb = (byte)c;
			
			if(n.getEdge() == c || (n.getEdge() == Nfa.CCL && n.inputSet.contains(cb))) {
				outSet.add(n.next);
			}
			
			if(outSet != null && debug) {
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
