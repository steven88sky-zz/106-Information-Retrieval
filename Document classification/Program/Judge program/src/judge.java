import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class judge {
	public static void main(String[] args) throws IOException{
		System.out.println("How many judging files?");
		Scanner scanner = new Scanner(System.in);
		int files = scanner.nextInt();
		scanner.close();
		
		int[] ans = new int[files];
		int[] test = new int[files];
		int temp = 0;
		for(int i=1;i<=files;i++)
		{
			String filename = i + "a.txt";
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String s = br.readLine();
			ans[i-1] = Integer.parseInt(s);
			br.close();
		}
		for(int i=1;i<=files;i++)
		{
			String filename = i + "test.txt";
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String s = br.readLine();
			test[i-1] = Integer.parseInt(s);
			br.close();
		}
		for(int i=0;i<files;i++)
		{
			if(ans[i] == test[i])
				temp++;
		}
		System.out.println(temp);
	}
}
