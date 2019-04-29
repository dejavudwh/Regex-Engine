package service;

public class ErrorHandler {

    public enum Error{
    	E_MEM, //�ڴ治��
    	E_BADEXPR, //������ʽ����
    	E_PAREN,  //���Ų�ƥ��
    	E_LENGTH, //Ҫ������������ʽ����
    	E_BRACKET, //�ַ�����û���� [ ��ͷ
    	E_BOL, //^�����ڱ��ʽ�Ŀ�ͷ
    	E_CLOSE, //* ? + ���������ű��ʽ
    	E_NEWLINE, //˫�����в��ܱ������з�
    	E_BADMAC, //û��ƥ��� }
    	E_NOMAC, //�����ĺ���ʽ������
    	E_MACDEPTH //����ʽ�ļ���̫�� 
    }
    
    private static String[] errMsgs = new String[]{
    	 "Not enough memory for NFA",
    	 "Malformed regular expression",
    	 "Missing close parenthesis",
    	 "Too many regular expression or expression too long",
    	 "Missing [ in character class",
    	 "^ must be at the start of expression or after [",
    	 "+ ? or * must follow an expression or subexpression",
    	 "Newline in quoted string, use \\n to get newline into expression",
    	 "Missing ) in macro expansion",
    	 "Macro deoesn't exist",
    	 "Macro expansions nested too deeply"
    };
               
    public static void parseErr(Error type) throws Exception {
    	throw new Exception(errMsgs[type.ordinal()]);
    }
}
