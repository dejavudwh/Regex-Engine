package nfa;

import java.util.Set;

import lexer.Lexer;
import lexer.Lexer.Token;
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
	
	public boolean expr(NfaPair pairOut) throws Exception {
		constructAndFactor(pairOut);
		NfaPair localPair = new NfaPair();
		
		while(lexer.MatchToken(Lexer.Token.OR)) {
			lexer.advance();
			constructAndFactor(localPair);
			
			Nfa startNode = nfaManager.newNfa();
			startNode.next = pairOut.startNode;
			startNode.next2 = localPair.startNode;
			pairOut.startNode = startNode;
			
			Nfa endNode = nfaManager.newNfa();
			pairOut.endNode.next = endNode;
			localPair.endNode.next = endNode;
			pairOut.endNode = endNode;
		}
		
		return true;
	}
	
	public boolean constructAndFactor(NfaPair pairOut) throws Exception {
		//System.out.println("fuck in");
		if(isCorrectChar(lexer.getCurrentToken())) {
			factor(pairOut);
		}
		
		while(isCorrectChar(lexer.getCurrentToken())) {
			NfaPair pairLocal = new NfaPair();
			factor(pairLocal);
			System.out.println("pairout"+pairOut.endNode);
			System.out.println("pairlocal"+pairLocal.startNode);
			pairOut.endNode.next = pairLocal.startNode;
			pairOut.endNode = pairLocal.endNode;
		}
		
		return true;
	}
	
	private boolean isCorrectChar(Token token) throws Exception {
		switch (token) {
    	//正确的表达式不会以 ) $ 开头,如果遇到EOS表示正则表达式解析完毕，那么就不应该执行该函数
    	case CLOSE_PAREN:
    	case AT_EOL:
    	case EOS:
    		return false;
    	case CLOSURE:
    	case PLUS_CLOSE:
    	case OPTIONAL:
    		//*, +, ? 这几个符号应该放在表达式的末尾
    		ErrorHandler.parseErr(ErrorHandler.Error.E_CLOSE);
    		return false;
    	case CCL_END:
    		//表达式不应该以]开头
    		ErrorHandler.parseErr(ErrorHandler.Error.E_BRACKET);
    		return false;
    	case AT_BOL:
    		//^必须在表达式的最开始
    		ErrorHandler.parseErr(ErrorHandler.Error.E_BOL);
    		return false;
		default:
			break;
    	}
    	
    	return true;
	}

	public void factor(NfaPair pairOut) throws Exception {
		term(pairOut);
		
    	boolean handled = false;
    	handled = constructStarClosure(pairOut);
    	if (handled == false) {
    		handled = constructPlusClosure(pairOut);
    	}
    	
    	if (handled == false) {
    		handled = constructOptionsClosure(pairOut);
    	}
    }
	
	public boolean constructOptionsClosure(NfaPair pairOut) throws Exception {
		Nfa start, end;
		//term(pairOut);
		
		if(!lexer.MatchToken(Lexer.Token.OPTIONAL)) {
			return false;
		}
		
		start = nfaManager.newNfa();
		end = nfaManager.newNfa();
		
		start.next = pairOut.startNode;
		start.next2 = end;
		
		pairOut.endNode.next = end;
		
		pairOut.startNode = start;
		pairOut.endNode = end;
		
		lexer.advance();
		
		return true;
	}
	
	public boolean constructPlusClosure(NfaPair pairOut) throws Exception {
		Nfa start ,end;
		//term(pairOut);
		
		if(!lexer.MatchToken(Lexer.Token.PLUS_CLOSE)) {
			return false;
		}
		
		start = nfaManager.newNfa();
		end = nfaManager.newNfa();
		
		start.next = pairOut.startNode;
		
		pairOut.endNode.next = end;
		pairOut.endNode.next2 = pairOut.startNode;
		
		pairOut.startNode = start;
		pairOut.endNode = end;
		
		lexer.advance();
		
		return true;
	}
	
	public boolean constructStarClosure(NfaPair pairOut) throws Exception {
		Nfa start, end;
		//term(pairOut);
		
		if(!lexer.MatchToken(Lexer.Token.CLOSURE)) {
			return false;
		}
		
		start = nfaManager.newNfa();
		end = nfaManager.newNfa();
		
		start.next = pairOut.startNode;
		start.next2 = end;
		
		pairOut.endNode.next = pairOut.startNode;
		pairOut.endNode.next2 = end;
		
		pairOut.startNode = start;
		pairOut.endNode = end;
		
		lexer.advance();
		
		return true;
	}
	
	public void term(NfaPair pairOut)throws Exception {
    	
    	boolean handled = constructExprInParen(pairOut);
    	if (handled == false) {
    		handled = constructNfaForSingleCharacter(pairOut);
    	}
    	
    	if (handled == false) {
    		handled = constructNfaForDot(pairOut);
    	}
    	
    	if (handled == false) {
    		constructNfaForCharacterSet(pairOut);
    	}
    }
	
	private boolean constructExprInParen(NfaPair pairOut) throws Exception {
		if (lexer.MatchToken(Lexer.Token.OPEN_PAREN)) {
    		lexer.advance();
    		expr(pairOut);
    		if (lexer.MatchToken(Lexer.Token.CLOSE_PAREN)) {
    			lexer.advance();
    		}
    		else {
    			ErrorHandler.parseErr(ErrorHandler.Error.E_PAREN);
    		}
    		
    		return true;
    	}
    	
    	return false;
	}

	private boolean constructNfaForCharacterSet(NfaPair pairOut) throws Exception {
		if(!lexer.MatchToken(Lexer.Token.CCL_START)) {
			return false;
		}
		
		lexer.advance();
		boolean negative = false;
		if (lexer.MatchToken(Lexer.Token.AT_BOL)) {
    		negative = true;
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
    	
    	if (negative) {
    		start.setComplement();
    	}
    	
    	lexer.advance();
    	
    	return true;
		
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
		start.setComplement();
		
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
		
		while(!lexer.MatchToken(Lexer.Token.EOS) && !lexer.MatchToken(Lexer.Token.CCL_END)) {
			if(!lexer.MatchToken(Lexer.Token.DASH)) {
				first = lexer.getLexeme();
				inputSet.add((byte)first);
			}
			else {
				lexer.advance(); 
    			for (; first <= lexer.getLexeme(); first++) {
    				inputSet.add((byte)first);
    			}
			}
			
			lexer.advance();
		}
	}
	
	
	
	
	
}
