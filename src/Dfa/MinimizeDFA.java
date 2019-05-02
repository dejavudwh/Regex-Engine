package Dfa;

import java.util.Iterator;
import java.util.List;

public class MinimizeDFA {

	private DfaConstructor dfaConstructor = null;
	private DfaGroupManager groupManager = new DfaGroupManager();
	private static final int ASCII_NUM = 128;
	private int[][] dfaTransTable = null;
	private int[][] minDfa = null;
	private List<Dfa> dfaList = null;
	private DfaGroup newGroup = null;
	private boolean addNewGroup = false;
	
	private static final int STATE_FAILURE = -1;
	
	public MinimizeDFA(DfaConstructor theConstructor) {
		this.dfaConstructor = theConstructor;
		this.dfaList = dfaConstructor.getDfaList();
		this.dfaTransTable = dfaConstructor.getDfaStateTransformTable();
	}
	
	public int[][] minimize() {
		addNoAcceptingDfaToGroup();
    	addAcceptingDfaToGroup();
    	
    	do {
    		addNewGroup = false;
    		doGroupSeperationOnNumber();
    		doGroupSeperationOnCharacter();
    	}while (addNewGroup);
    	
    	createMiniDfaTransTable();
    	printMiniDfaTable();
    	
    	return minDfa;
	}

	private void createMiniDfaTransTable() {
		initMiniDfaTransTable();
    	Iterator<Dfa> it = dfaList.iterator();
    	while (it.hasNext()) {
    		int from = ((Dfa)it.next()).stateNum;
    		for (int i = 0; i < ASCII_NUM; i++) {
    			if (dfaTransTable[from][i] != STATE_FAILURE) {
    				int to = dfaTransTable[from][i];

    				DfaGroup fromGroup = groupManager.getContainingGroup(from);
    				DfaGroup toGroup = groupManager.getContainingGroup(to);

    				minDfa[fromGroup.groupNum()][i] = toGroup.groupNum();
    			}
    		}
    	}
	}

	private void printMiniDfaTable() {
    	for (int i = 0; i < groupManager.size(); i++)
    		for (int j = 0; j < groupManager.size(); j++) {
    		    if (isOnNumberClass(i,j)) {
    		    	System.out.println("from " + i + " to " + j + " on D");
    		    }
    		    if (isOnDot(i,j)) {
    		    	System.out.println("from " + i + " to " + j + " on .");
    		    }
    		}
    }
    
    private boolean isOnNumberClass(int from, int to) {
    	char c = '0';
    	for (c = '0'; c <= '9'; c++) {
    		if (minDfa[from][c] != to) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    private boolean isOnDot(int from, int to) {
    	if (minDfa[from]['.'] != to) {
    		return false;
    	}
    	
    	return true;
    }

    
    private void initMiniDfaTransTable() {
    	minDfa = new int[groupManager.size()][ASCII_NUM];
        for (int i = 0; i <groupManager.size(); i++)
        	for (int j = 0; j < ASCII_NUM; j++) {
        		minDfa[i][j] = STATE_FAILURE;
        	}
    }

	private void doGroupSeperationOnNumber() {
		for (int i = 0; i < groupManager.size(); i++) {
    		int dfaCount = 1;
    		newGroup = null;
    		DfaGroup group = groupManager.get(i);
    		
    		System.out.println("Handle seperation on group: ");
    		group.printGroup();
    		
    		Dfa first = group.get(0);
    		Dfa next = group.get(dfaCount);

    		while (next != null) {
    			for (char c = '0'; c < '0' + 9; c++) {
    				if (doGroupSeprationOnInput(group, first, next, c) == true) {
    					addNewGroup = true;
    					break;
    				}
    			}
    			
    		    dfaCount++;
    		    next = group.get(dfaCount);
    		}
    		
    		group.commitRemove();
    	}
	}

	private void doGroupSeperationOnCharacter() {
		for (int i = 0; i < groupManager.size(); i++) {
    		int dfaCount = 1;
    		newGroup = null;
    		DfaGroup group = groupManager.get(i);
    		
    		System.out.println("Handle seperation on group: ");
    		group.printGroup();
    		
    		Dfa first = group.get(0);
    		Dfa next = group.get(dfaCount);

    		while (next != null) {
    			for (char c = 0; c < ASCII_NUM; c++) {
    				if (Character.isDigit(c) == false && doGroupSeprationOnInput(group, first, next, c) == true) {
    					addNewGroup = true;
    					break;
    				}
    			}
    			
    		    dfaCount++;
    		    next = group.get(dfaCount);
    		}
    		
    		group.commitRemove();
    	}
		
	}
	
	private boolean doGroupSeprationOnInput(DfaGroup group, Dfa first, Dfa next, char c) {
		int goto_first = dfaTransTable[first.stateNum][c];
		int goto_next = dfaTransTable[next.stateNum][c];
		
		if (groupManager.getContainingGroup(goto_first) != 
				groupManager.getContainingGroup(goto_next)) {
			if (newGroup == null) {
				newGroup = groupManager.createNewGroup();
			}

			group.tobeRemove(next);
			newGroup.add(next);
			
			System.out.println("Dfa:" + first.stateNum + " and Dfa:" +
			next.stateNum + " jump to different group on input char " + c);
			
			System.out.println("remove Dfa:" + next.stateNum + " from group:" + group.groupNum() 
					+ " and add it to group:" + newGroup.groupNum());
			return true;
		}
	    
		return false;
	}

	private void addNoAcceptingDfaToGroup() {
    	Iterator<Dfa> it = dfaList.iterator();
    	DfaGroup group = groupManager.createNewGroup();
    	
    	while (it.hasNext()) {
    		Dfa dfa = (Dfa)it.next();
    		if (dfa.accepted == false) {
    			group.add(dfa);
    		}
    	}
    	
    	group.printGroup();
    }
    
    private void addAcceptingDfaToGroup() {
    	Iterator<Dfa> it = dfaList.iterator();
    	DfaGroup group = groupManager.createNewGroup();
    	
    	while (it.hasNext()) {
    		Dfa dfa = (Dfa)it.next();
    		if (dfa.accepted == true) {
    			group.add(dfa);
    		}
    	}
    	
    	group.printGroup();
    	
    }
	
}
