package Dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nfa.Nfa;
import nfa.NfaPair;
import service.NfaIntepretor;

public class DfaConstructor {

	private NfaPair nfaMachine = null;
	private NfaIntepretor nfaIntepretor = null;
	
	private ArrayList<Dfa> dfaList = new ArrayList<Dfa>();
	
	private static final int MAX_DFA_STATE_COUNT = 254;
	private static final int ASCII_COUNT = 127;
	private static final int STATE_FAILURE = -1;
	
	private int[][] dfaStateTransformTable = new int[MAX_DFA_STATE_COUNT][ASCII_COUNT + 1];
	
	public DfaConstructor(NfaPair pair, NfaIntepretor nfaIntepretor) {
		this.nfaIntepretor = nfaIntepretor;
		this.nfaMachine = pair;
		
		this.nfaIntepretor.debug = false;
		
		initTransformTable();
	}

	private void initTransformTable() {
		for(int i = 0;i < MAX_DFA_STATE_COUNT;i++) {
			for (int j = 0; j <= ASCII_COUNT; j++) {
    			dfaStateTransformTable[i][j] = STATE_FAILURE;
    		}
		}
	}
	
	public int[][] convertNfaToDfa() {
		Set<Nfa> input = new HashSet<Nfa>();
		input.add(nfaMachine.startNode);
		Set<Nfa> nfaStartClosure = nfaIntepretor.e_closure(input);
		Dfa start = Dfa.getDfaFromNfaSet(nfaStartClosure);
		dfaList.add(start);
		
		System.out.println("Create DFA start node:");
		printDfa(start);
		
		int nextState = STATE_FAILURE;
		int currentDfaIndex = 0;
		while(currentDfaIndex < dfaList.size()) {
			Dfa currentDfa = dfaList.get(currentDfaIndex);
			
			for(char c = 0;c <= ASCII_COUNT;c++) {
				Set<Nfa> move = nfaIntepretor.move(currentDfa.nfaStates, c);
				
				if(move.isEmpty()) {
					nextState = STATE_FAILURE;
				}
				else {
					Set<Nfa> closure = nfaIntepretor.e_closure(move);
					
					Dfa dfa = isNfaStatesExistInDfa(closure);
					
					if(dfa == null) {
						System.out.println("Create DFA node:");
						
						Dfa newDfa = Dfa.getDfaFromNfaSet(closure);
						printDfa(newDfa);
        				
        				dfaList.add(newDfa);
        				nextState = newDfa.stateNum;
					}
					else {
						System.out.println("Get a existed dfa node:");
        				printDfa(dfa);
        				nextState = dfa.stateNum;
					}
				}
				if (nextState != STATE_FAILURE) {
    				System.out.println("DFA from state: " + currentDfa.stateNum + " to state:" + nextState + " on char: " + c);
    			}
    			dfaStateTransformTable[currentDfa.stateNum][c] = nextState;
			}
			System.out.print("\n");
    		currentDfaIndex++;
		}
		return dfaStateTransformTable;
	}

	private Dfa isNfaStatesExistInDfa(Set<Nfa> closure) {
		Iterator<Dfa> it = dfaList.iterator();
    	while (it.hasNext()) {
    		Dfa dfa = it.next();
    		if (dfa.hasNfaStates(closure)) {
    			return dfa;
    		}
    	}
    	
    	return null;
	}

	private void printDfa(Dfa dfa) {
    	
    	System.out.print("Dfa state: " + dfa.stateNum + " its nfa states are: ");
    	Iterator<Nfa> it = dfa.nfaStates.iterator();
    	while (it.hasNext()) {
    		System.out.print(it.next().getStateNum());
    		if (it.hasNext()) {
    			System.out.print(",");
    		}
    	}
    	
    	System.out.print("\n");
    }
    
    public void printDFA() {
    	int dfaNum = dfaList.size();
    	for (int i = 0; i < dfaNum; i++)
    		for (int j = 0; j < dfaNum; j++) {
    			if (isOnNumberClass(i,j)) {
    				System.out.println("From state " + i + " to state " + j + " on D");
    			}
    			
    			if (isOnDot(i, j)) {
    				System.out.println("From state " + i + " to state " + j + " on dot");
    			}
    		}
    }
    
    private boolean isOnNumberClass(int from, int to) {
    	char c = '0';
    	for (c = '0'; c <= '9'; c++) {
    		if (dfaStateTransformTable[from][c] != to) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    private boolean isOnDot(int from, int to) {
    	if (dfaStateTransformTable[from]['.'] != to) {
    		return false;
    	}
    	
    	return true;
    }
	
	
	
	
	
	
	
}
