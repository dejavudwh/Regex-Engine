package input;

import java.io.UnsupportedEncodingException;

public class InputSystem {

	private Input input = new Input();
	public void runStdinExampe() {
    	input.newInput(null); //����̨����
    	
    	input.markStart();
    	printWord();
    	input.markEnd();
    	input.tomarkprev();
    	
    	input.markStart();
    	printWord();
    	input.markEnd();
    	
    	
    	System.out.println("prev word: " + input.getPrevText());// ��ӡ�� typedef
    	System.out.println("current word: " + input.getText()); //��ӡ��int
		
	}
	
	 private void printWord() {
	    	
	    	byte c;
	    	while ((c = input.advance()) != ' ') {
	    		byte[] buf = new byte[1];
	    		buf[0] = c;
	    		try {
					String s = new String(buf, "UTF8");
					System.out.print(s);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    	System.out.println("");
	    }
	
    public static void main(String[] args) {
    	InputSystem input = new InputSystem();
    	input.runStdinExampe();
    }
    
}
