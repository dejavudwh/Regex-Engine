package main;

import input.Input;
import service.MacroHandler;

public class ThompsonConstruction {

	private Input input = new Input();
	private MacroHandler macroHandler = null;
	
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
	
	private void runMacroExpandExample() {
		System.out.println("Enter regular expression");
		renewInputBuffer();
	}
	
	public static void main(String[] args) {
		ThompsonConstruction construction = new ThompsonConstruction();
		construction.runMacroExample();
	}

}
