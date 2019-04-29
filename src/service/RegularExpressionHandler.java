package service;

import java.util.ArrayList;

import input.Input;

public class RegularExpressionHandler {

	private Input input = null;
	private MacroHandler macroHandler = null;
	private ArrayList<String> regularExprArr = new ArrayList<String>();
	private boolean inquoted = false;
	
	public RegularExpressionHandler(Input input, MacroHandler macroHandler) throws Exception {
		this.input = input;
		this.macroHandler = macroHandler;
		
		processRegularExprs();
	}

	private void processRegularExprs() throws Exception {
		while(input.lookAhead(1) != Input.EOF) {
			preProcessExpr();
		}
	}

	private void preProcessExpr() throws Exception {
		while(Character.isSpaceChar(input.lookAhead(1)) || input.lookAhead(1) == '\n') {
			input.advance();
		}
		
		String regularExpr = "";
		char c = (char) input.lookAhead(1);
		while(c != '\n' && Character.isSpaceChar(c) == false) {
			if(c == '"') {
				inquoted = !inquoted;
			}
			
			if(!inquoted && c == '{') {
				String name = extracMacroNameFromInput();
				regularExpr += expandMacro(name);
			}
			else {
				regularExpr += c;
			}
			
			c = (char) input.advance();
		}
		
		regularExprArr.add(regularExpr);
	}

	private String expandMacro(String macroName) throws Exception {
		String macroContent = macroHandler.getMacro(macroName);
		int begin = macroContent.indexOf('{');
		while(begin != -1) {
			int end = macroContent.indexOf('}', begin);
			if(end == -1) {
				ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
				return null;
			}
			
			boolean inquoted = checkInQuoted(macroContent, begin, end);
			
			if(!inquoted) {
				macroName = macroContent.substring(begin + 1, end);
				String content = macroContent.substring(0, begin);
				content += macroHandler.getMacro(macroName);
				content += macroContent.substring(end + 1, macroContent.length());
				macroContent = content;
				
				begin = macroContent.indexOf('{');
			}
			else {
				begin = macroContent.indexOf('{', end);
			}
		}
		return macroContent;
	}

	private boolean checkInQuoted(String macroContent, int begin, int end) throws Exception {
		boolean inquoted = false;
		int quoteBegin = macroContent.indexOf('"');
		int quoteEnd = -1;
		
		while(quoteBegin != -1) {
			quoteEnd = macroContent.indexOf('"', quoteBegin + 1);
			
			if(quoteEnd == -1) {
				ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
			}
			
			if(quoteBegin < begin && quoteEnd > end) {
				inquoted = true;
			}
			else if(quoteBegin < begin && quoteEnd > end) {
				ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
			}
			else if(quoteBegin > begin && quoteEnd < end) {
				ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
			}
			
			quoteBegin = macroContent.indexOf('"', quoteEnd + 1);
		}
		
		return inquoted;
	}

	private String extracMacroNameFromInput() throws Exception {
		String name = "";
		char c = (char) input.lookAhead(1);
		while(c != '}' && c != '\n') {
			name += c;
			input.advance();
			c = (char) input.lookAhead(1);
		}
		
		if(c == '}') {
			return name;
		}else {
			ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
			return null;
		}
	}
}
