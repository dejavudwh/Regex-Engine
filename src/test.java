
public class test {

	public static void main(String[] args) {
		int a1[] = {1,2,3,4,5,6,7,8};
		int a2[] = {-1,-2,-3,-4};
		System.arraycopy(a1, 0, a1, 3, 3);
		for(int i = 0;i < a1.length;i++)
			System.out.print(a1[i] + "  ");
	}

}
