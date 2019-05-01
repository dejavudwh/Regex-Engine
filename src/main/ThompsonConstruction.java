package main;

import input.Input;
import lexer.Lexer;
import nfa.NfaMachineConstructor;
import nfa.NfaPair;
import nfa.NfaPrinter;
import service.MacroHandler;
import service.RegularExpressionHandler;

public class ThompsonConstruction {

	private Input input = new Input();
	private MacroHandler macroHandler = null;
	private RegularExpressionHandler regularExpr = null;
    private Lexer lexer = null;
    
    private NfaMachineConstructor nfaMachineConstructor = null;
    private NfaPrinter nfaPrinter = new NfaPrinter();
    
    NfaPair pair = new NfaPair();
	
	private void renewInputBuffer() {
		input.newInput(null);
		input.advance();
		input.pushback(1);
	}
	
	private void runMacroExample() {
		System.out.println("Please enter macro definition");
    	
    	renewInputBuffer();
    	macroHandler = new MacroHandler(input);
    	macroHandler.printMacro();
	}
	
	private void runMacroExpandExample() throws Exception {
		System.out.println("Enter regular expression");
		renewInputBuffer();
		
		regularExpr = new RegularExpressionHandler(input, macroHandler);
    	System.out.println("regular expression after expanded: ");
    	for (int i = 0; i < regularExpr.getRegularExpressionCount(); i++) {
    		System.out.println(regularExpr.getRegularExpression(i));	
    	}
	}
	
	private void runLexerExample() {
	       lexer = new Lexer(regularExpr);
	       int exprCount = 0;
	       //System.out.println("size:" +  regularExpr.getRegularExpressionCount());
	       System.out.println("当前正则解析的正则表达式: " + regularExpr.getRegularExpression(exprCount));
	       lexer.advance();
	       
	       while (lexer.MatchToken(Lexer.Token.END_OF_INPUT) == false) {
	    	   
	    	   if (lexer.MatchToken(Lexer.Token.EOS) == true) {
	    		   System.out.println("解析下一个正则表达式");
	    		   exprCount++;
	    		   System.out.println("当前正则解析的正则表达式: " + regularExpr.getRegularExpression(exprCount));
	    		   lexer.advance();
	    	   }
	    	   else {
	    		   printLexResult();
	    	   }
	    	   
	       } 
	    }
	    
    private void printLexResult() {
    	while (lexer.MatchToken(Lexer.Token.EOS) == false) {
    		System.out.println("当前识别字符是: " + (char)lexer.getLexeme());
    		
    		if (lexer.MatchToken(Lexer.Token.L) != true) {
    			System.out.println("当前字符具有特殊含义");
    			
    			printMetaCharMeaning(lexer);
    		}
    		else {
    			System.out.println("当前字符是普通字符常量");
    		}
    		
    		lexer.advance();
    	}
    }
    
    private void printMetaCharMeaning(Lexer lexer) {
    	String s = "";
    	if (lexer.MatchToken(Lexer.Token.ANY)) {
    		s = "当前字符是点通配符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.AT_BOL)) {
    		s = "当前字符是开头匹配符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.AT_EOL)) {
    		s = "当前字符是末尾匹配符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CCL_END)) {
    		s = "当前字符是字符集类结尾括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CCL_START)) {
    		s = "当前字符是字符集类的开始括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CLOSE_CURLY)) {
    		s = "当前字符是结尾大括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CLOSE_PAREN)) {
    		s = "当前字符是结尾圆括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.DASH)) {
    		s = "当前字符是横杆";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OPEN_CURLY)) {
    		s = "当前字符是起始大括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OPEN_PAREN)) {
    		s = "当前字符是起始圆括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OPTIONAL)) {
    		s = "当前字符是单字符匹配符?";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OR)) {
    		s = "当前字符是或操作符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.PLUS_CLOSE)) {
    		s = "当前字符是正闭包操作符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CLOSURE)) {
    		s = "当前字符是闭包操作符";
    	}
    	
    	System.out.println(s);
    }
    
    private void runNfaMachineConstructorExample() throws Exception {
    	lexer = new Lexer(regularExpr);
    	nfaMachineConstructor = new NfaMachineConstructor(lexer);

    	//nfaMachineConstructor.constructNfaForSingleCharacter(pair);
    	//nfaMachineConstructor.constructNfaForDot(pair);
    	//nfaMachineConstructor.constructNfaForCharacterSetWithoutNegative(pair);
    	//nfaMachineConstructor.constructNfaForCharacterSet(pair);
    	//nfaMachineConstructor.term(pair);
    	//nfaMachineConstructor.constructStarClosure(pair);
    	//nfaMachineConstructor.constructPlusClosure(pair);
    	//nfaMachineConstructor.constructOptionsClosure(pair);
    	//nfaMachineConstructor.factor(pair);
    	//nfaMachineConstructor.cat_expr(pair);
    	nfaMachineConstructor.expr(pair);
    	nfaPrinter.printNfa(pair.startNode);
    	
    	
    }
	
	public static void main(String[] args) throws Exception {
		ThompsonConstruction construction = new ThompsonConstruction();
		construction.runMacroExample();
		construction.runMacroExpandExample();
		construction.runLexerExample();
		
		construction.runNfaMachineConstructorExample();
	}

}
