import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class testFile {
	public static void main(String[] args) throws IOException{
		System.out.println("How many testing files?");
		Scanner scanner = new Scanner(System.in);
		int files = scanner.nextInt();
		scanner.close();
		
		/******************************************************
		 Calculate how many distinct words
		 *****************************************************/
		Set<String> set = new HashSet<String>();
		
		String filename = "Dictionary.txt";
		BufferedReader br = new BufferedReader(new FileReader(filename));
			
		while(br.ready())
		{
			String s = br.readLine();
			set.add(s);
		}
			br.close();
		if(set.contains(""))
			set.remove("");
		
		/******************************************************
		 Sort the words
		 *****************************************************/
		ArrayList<String> sortedList = new ArrayList<String>(set);
		Collections.sort(sortedList);
		String[] keywordsArray = sortedList.toArray(new String[0]);
		int[][] keywordsCount = new int[1000+files][set.size()];
		
		/******************************************************
		 Count all keywords in each file
		 *****************************************************/
		for(int i=1;i<=files;i++)
		{
			filename = i + ".txt";
			br = new BufferedReader(new FileReader(filename));
			while(br.ready())
			{
				String s = br.readLine();
				String[] stringArray = null;
				stringArray = s.split(" |,");
				for(String token : stringArray)
				{
					for(int j=0;j<set.size();j++)
					{
						if(token.equals(keywordsArray[j]))
						{
							keywordsCount[i-1+1000][j]++;
							break;
						}
					}
				}
			}
			br.close();
		}
		/******************************************************
		 Start clustering
		 *****************************************************/
		//Get prepared
		int[] category = new int[1000+files];
		filename = "Supervised model.txt";
		br = new BufferedReader(new FileReader(filename));
		for(int i=0;i<1000;i++)
		{
			for(int j=0;j<set.size();j++)
			{
				keywordsCount[i][j] = Integer.parseInt(br.readLine());
				//System.out.println(keywordsCount[i][j]);
			}
			category[i] = Integer.parseInt(br.readLine());
			//System.out.println(category[i]);
		}
		br.close();
		
		/******************************************************
		 k-NN algorithm
		 *****************************************************/
		int radius = 6;
		for(int i=1000;i<1000+files;i++)
		{
			int[] countOfPoints = new int[4];
			for(int j=0;j<1000;j++)
			{
				for(int k=0;k<set.size();k++)
				{
					if(keywordsCount[j][k] > keywordsCount[i][k]+radius || keywordsCount[j][k] < keywordsCount[i][k]-radius)
						break;
					if(k == set.size()-1)
						countOfPoints[category[j]-1]++;
				}
			}
			int max=0;
			for(int j=0;j<4;j++)
			{
				if(countOfPoints[j] > max)
				{
					max = countOfPoints[j];
					category[i] = j+1;
					//System.out.println(countOfPoints[j]);
				}
			}
			
		}
		
		/******************************************************
		 Write files
		 *****************************************************/
		for(int i=1000;i<1000+files;i++)
		{
			filename = (i-999) + "a.txt";
			//filename = (i-999) + "test.txt";
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
			bw.write(Integer.toString(category[i]));
			bw.close();
		}
		
		/*for(int i=1000;i<1000+files;i++)
			System.out.println(category[i]);*/
		
	}
}
