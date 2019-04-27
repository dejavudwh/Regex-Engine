package input;

public interface FileHandler {

	public void open();
	public int close();
	
	public int read(byte[] buf, int begin, int len);
}
