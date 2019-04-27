package input;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Input {

	public static final int EOF = 0;
	private final int MAXLOOK = 16;
	private final int MAXLEX = 1024;
	private final int BUFSIZE = (MAXLEX * 3) + (2 * MAXLOOK);
	private final int END = BUFSIZE;
	private final int DANGER = (END - MAXLOOK);
	private final byte[] BUF = new byte[BUFSIZE];
	
	private int bufEnd = BUFSIZE;
	private boolean readEof = false;
	
	private int next = END;
	private int sMark = END;
	private int eMark = END;
	private int pMark = END;
	private int Pline = END;
	private int pLength = END;
	private int Cline = END;
	private int Mline  = 1; 
	
	private FileHandler fileHandler = null;
	
	private boolean noMoreChar() {
		return next >= END;
	}
	
	private FileHandler getFileHandler(String filename) {
		if(filename != null) return new DiskFileHandler(filename);
		else return new StdinHandler();
	}
	
	public void newInput(String filename) {
		if(fileHandler != null) {
			fileHandler.close();
		}
		
		fileHandler = getFileHandler(filename);
		
		readEof = false;
		next     = END;
		pMark    = END;
		sMark    = END;
		eMark    = END;
		bufEnd  = END;
		Cline   = 1;
		Mline    = 1;
	}
	
	public String getText() {
		byte[] bb = Arrays.copyOfRange(BUF, sMark, eMark);
		return new String(bb, StandardCharsets.UTF_8);
	}
	
	public int getLineNo() {
		return Cline;
	}
	
	public String getPrevText() {
		byte[] bb = Arrays.copyOfRange(BUF, pMark, pMark + pLength);
		return new String(bb, StandardCharsets.UTF_8);
	}
	
	public int getPLength() {
		return pLength;
	}
	
	public int markStart() {
		Mline = Cline;
		eMark = sMark = next;
		return sMark;
	}
	
	public int markEnd() {
		Mline = Cline;
		eMark = next;
		return eMark;
	}
	
	public int tomark() {
		Pline = Mline;
		next = eMark;
		return next;
	}
	
	public int tomarkprev() {
		/*
		 * 执行这个函数后，上一个被词法解析器解析的字符串将无法在缓冲区中找到
		 */
		pMark = sMark;
		Pline = Cline;
		pLength = eMark - sMark;
		return pMark;
	}
	
	
	
}
