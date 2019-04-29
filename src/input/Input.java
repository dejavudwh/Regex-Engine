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
		return (readEof && next >= bufEnd);
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
		fileHandler.open();
		
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
		pMark = sMark;
		Pline = Cline;
		pLength = eMark - sMark;
		return pMark;
	}
	
	public byte advance() {
		if(noMoreChar()) {
			return 0;
		}
		
		if(readEof == false && flush(false) < 0) {
			return -1;
		}
		
		if(BUF[next] == '\n') {
			Cline++;
		}
		
		return BUF[next++];
	}

	public static int NO_MORE_CHARS_TO_READ = 0;
	public static int FLUSH_OK = 1;
	public static int FLUSH_FAIL = -1;
	
	private int flush(boolean force) {
		if(noMoreChar()) {
			return NO_MORE_CHARS_TO_READ;
		}
		
		if(readEof) {
			return FLUSH_OK;
		}
		
		if(next > DANGER || force) {
			int copy, shift, left;
			left = pMark < sMark ? pMark : sMark;
			shift = left;
			if(shift < MAXLEX) {
				if(!force) return FLUSH_FAIL;
				
				left = markStart();
				tomarkprev();
				shift = left;
			}
			
			copy = bufEnd - left;
			System.arraycopy(BUF, 0, BUF, left, copy);
			
			if(fillBuf(copy) == 0) {
				System.out.println("Fill BUF an error occurred");
			}
			
			if (pMark != 0) {
				pMark -= shift;
			}
			
			sMark -= shift;
			eMark -= shift;
			next  -= shift;
		}
		
		return FLUSH_OK;
	}

	private int fillBuf(int copylen) {
		int need;
		int got = 0;
		need = ((END - copylen) * MAXLEX) / MAXLEX;
		
		if(need < 0) {
			System.err.println("Bad read-request starting addr.");
		}
		if(need == 0) {
			return 0;
		}
		
		if((got = fileHandler.read(BUF, copylen, need)) == -1) {
			System.err.println("Can't read input file");
		}
		
		bufEnd = copylen + got;
		if(got < need) {
			readEof = true;
		}
		
		return got;
	}
	
	public byte lookAhead(int n) {
		byte p = BUF[next + n -1];
		
		if(next + n -1 >= bufEnd && readEof) {
			return EOF;
		}
		
		return (next + n - 1 < 0 || next + n - 1 >= bufEnd) ? 0 : p; 
	}
	
	public boolean pushback(int n) {
		while(--n >= 0 && next > sMark) {
			if (BUF[--next] == '\n' || BUF[next] == '\0') {
				--Cline;
			}
		}
		
		if (next < eMark) {
			eMark = next;
			Mline = Cline;
		}
		
		return (next > sMark);
	}

	public int getPline() {
		return Pline;
	}

	public void setPline(int pline) {
		Pline = pline;
	}
	
	
	
}
