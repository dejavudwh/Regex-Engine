package service;

import java.util.HashMap;

import input.Input;

public class MacroHandler {
	
	private HashMap<String, String> macroMap = new HashMap<String, String>();
	private Input inputSystem;
	
	public MacroHandler(Input input) {
		inputSystem = input;
		
		while(input.lookAhead(1) != Input.EOF) {
			newMacro();
		}
	}

	private void newMacro() {
		while(Character.isSpaceChar(inputSystem.lookAhead(1)) || inputSystem.lookAhead(1) == '\n') {
			inputSystem.advance();
		}
		
		String macroName = "";
		char c = (char) inputSystem.lookAhead(1);
		while(Character.isSpaceChar(c) == false && c != '\n') {
			macroName += c;
			inputSystem.advance();
			c = (char) inputSystem.lookAhead(1);
		}
		
		while(Character.isSpaceChar(inputSystem.lookAhead(1))) {
			inputSystem.advance();
		}
		
		String macroContent = "";
		c = (char) inputSystem.lookAhead(1);
		while(Character.isSpaceChar(c) == false && c != '\n') {
			macroContent += c;
			inputSystem.advance();
			c = (char) inputSystem.lookAhead(1);
		}
		
		inputSystem.advance();
		
		macroMap.put(macroName, macroContent);
	}
}
