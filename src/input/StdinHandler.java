package input;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class StdinHandler implements FileHandler {

	private String inputBuf = "";
	private int curPos = 0;
	
	@Override
	public void open() {
		Scanner s = new Scanner(System.in);
		while(true) {
			String line = s.nextLine();
			if(line.equals("end")) {
				break;
			}
			inputBuf += line;
			inputBuf += '\n';
		}
		
		s.close();
	}

	@Override
	public int close() {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public int read(byte[] buf, int begin, int len) {
		if(curPos >= inputBuf.length()) {
			return 0;
		}
		
		int readCnt = 0;
		try {
			byte[] bb = inputBuf.getBytes("UTF-8");
			while(readCnt + curPos < inputBuf.length() && readCnt < len) {
				buf[begin + readCnt] = bb[curPos + readCnt];
				readCnt++;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		return readCnt;
	}

}
