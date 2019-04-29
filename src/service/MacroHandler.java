package service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public String getMacro(String macroName) throws Exception {
		if(macroMap.containsKey(macroName) == false) {
			ErrorHandler.parseErr(ErrorHandler.Error.E_NOMAC);
		}
		else {
			return "(" + macroMap.get(macroName) + ")";
		}
		
		return "ERROR";//
	}
	
	public void printMacro() {
		if(macroMap.isEmpty()) {
			System.out.println("No marco");
		}
		else {
			Iterator<Entry<String, String>> it = macroMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
				System.out.println("Macro name: " + entry.getKey() + " Macro content: " + entry.getValue());
			}
		}
	}
}
