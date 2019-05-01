package nfa;

import java.util.Set;

public class NfaPrinter {

	private static final int ASCII_NUM = 128;
	private boolean start = true;
	
    private void printCCL(Set<Byte> set) {
    	System.out.print("[ ");
    	for (int i = 0; i < ASCII_NUM; i++) {
    		if (set.contains((byte)i)) {
    			if (i < ' ') {
    				System.out.print("^" + (char)(i + '@'));
    			}
    			else {
    				System.out.print((char)i);
    			}
    		}
    	}
    	
    	System.out.print(" ]");
    }
    
    public void printNfa(Nfa startNfa) {
    	if (startNfa == null || startNfa.isVisited()) {
    		return;
    	}
    	
    	if (start) {
    		System.out.println("--------NFA--------");
    	}
    	
    	startNfa.setVisited();
    	
    	printNfaNode(startNfa);
    	
    	if (start) {
    		System.out.print("  (START STATE)");
    		start = false;
    	}
    	
    	System.out.print("\n");
    	
    	printNfa(startNfa.next);
    	printNfa(startNfa.next2);
    }
    
    private void printNfaNode(Nfa node) {
    	if (node.next == null) {
    		System.out.print("TERMINAL");
    	}
    	else {
    		System.out.print("NFA state: " + node.getStateNum());
    		System.out.print("--> " + node.next.getStateNum());
    		if (node.next2 != null) {
    			System.out.print(" " + node.next2.getStateNum() );
    		}
    		
    		System.out.print(" on:");
    		switch (node.getEdge()) {
    		case Nfa.CCL:
    			printCCL(node.inputSet);
    			break;
    		case Nfa.EPSILON:
    			System.out.print("EPSILON ");
    			break;
    		default:
    			System.out.print((char)node.getEdge());
    			break;
    		}
    	}
    }
}
