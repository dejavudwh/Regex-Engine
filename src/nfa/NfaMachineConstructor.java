package nfa;

import java.util.Set;

import lexer.Lexer;
import service.ErrorHandler;

public class NfaMachineConstructor {

	private Lexer lexer;
	private NfaManager nfaManager = null;
	
	public NfaMachineConstructor(Lexer lexer) throws Exception {
		this.lexer = lexer;
		nfaManager = new NfaManager();
		
		while(lexer.MatchToken(Lexer.Token.EOS)) {
			lexer.advance();
		}
	}
	
	public boolean constructNfaForSingleCharacter(NfaPair pairOut) throws Exception {
		if(!lexer.MatchToken(Lexer.Token.L)) {
			return false;
		}
		
		Nfa start = null;
		start = pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = pairOut.startNode.next = nfaManager.newNfa();
		
		start.setEdge(lexer.getLexeme());
		
		lexer.advance();
		
		return true;
	}
	
	public boolean constructNfaForDot(NfaPair pairOut) throws Exception {
		if(!lexer.MatchToken(Lexer.Token.ANY)) {
			return false;
		}
		
		Nfa start = null;
		start = pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = pairOut.startNode.next = nfaManager.newNfa();
		
		start.setEdge(Nfa.CCL);
		start.addToSet((byte)'\n');
		start.addToSet((byte)'\r');
		start.addComplement();
		
		lexer.advance();
		
		return false;
	}
	
	public boolean constructNfaCharacterSetWithoutNegative(NfaPair pairOut) throws Exception {
		if(!lexer.MatchToken(Lexer.Token.CCL_START)) {
			return false;
		}
		
		Nfa start = null;
		start = pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = pairOut.startNode.next = nfaManager.newNfa();
		
		start.setEdge(Nfa.CCL);
		
		if (lexer.MatchToken(Lexer.Token.CCL_END) == false) {
    		dodash(start.inputSet);
    	}
		if (lexer.MatchToken(Lexer.Token.CCL_END) == false) {
    		ErrorHandler.parseErr(ErrorHandler.Error.E_BADEXPR);
    	}
    	lexer.advance();
    	
    	return true;
	}

	private void dodash(Set<Byte> inputSet) {
		int first = 0;
		
		while(!lexer.MatchToken(Lexer.Token.EOS) && !lexer.MatchToken(Lexer.Token.CCL_END) == false) {
			if(!lexer.MatchToken(Lexer.Token.DASH)) {
				first = lexer.getLexeme();
				inputSet.add((byte)first);
			}
			else {
				lexer.advance(); //Ô½¹ý -
    			for (; first <= lexer.getLexeme(); first++) {
    				inputSet.add((byte)first);
    			}
			}
			
			lexer.advance();
		}
	}
	
	
	
	
	
}
