package nfa;

import java.util.HashSet;
import java.util.Set;

public class Nfa {

	public enum ANCHOR{
		START,
		END,
		NONE,
		BOTH,
	}
	
	public static final int EPSILON = -1;
	public static final int CCL = -2;
	public static final int EMPTY = -3;
	private static final int ASCII_COUNT = 127;
	
	public Set<Byte> inputSet = null;
	public Nfa next = null;
	public Nfa next2 = null;
	private ANCHOR anchor;
	private int stateNum;
	private boolean visited = false;
	
	private int edge;

	public Nfa() {
		inputSet = new HashSet<Byte>();
		clearState();
	}
	
	public void clearState() {
		inputSet.clear();
    	next = next2 = null;
    	anchor = ANCHOR.NONE;
    	stateNum = -1;
	}
	
	public void addToSet(Byte b) {
		inputSet.add(b);
	}
	
	public void setComplement() {
		Set<Byte> newSet = new HashSet<Byte>();
    	
    	for (byte b = 0; b < ASCII_COUNT; b++) {
    		if (inputSet.contains(b) == false) {
    			newSet.add(b);
    		}
    	}
    	
    	inputSet = null;
    	inputSet = newSet;
	}

	public int getEdge() {
		return edge;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getStateNum() {
		return stateNum;
	}

	public void setStateNum(int stateNum) {
		this.stateNum = stateNum;
	}

	public ANCHOR getAnchor() {
		return anchor;
	}

	public void setAnchor(ANCHOR anchor) {
		this.anchor = anchor;
	}
	
	
}
