import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
		int[][] keywordsCount = new int[files][set.size()];
		
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
							keywordsCount[i-1][j]++;
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
		int[] category = new int[files];
		int[][] centroid = new int[4][set.size()];
		filename = "Unsupervised model.txt";
		br = new BufferedReader(new FileReader(filename));
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<set.size();j++)
			{
				centroid[i][j] = Integer.parseInt(br.readLine());
			}
		}
		br.close();
		//Calculate distance
		for(int x=0;x<files;x++)
		{
			double nearestTemp = 0;
			double ifNearestTemp = Double.MAX_VALUE;
			for(int center=0;center<4;center++)
			{
				for(int access=0;access<set.size();access++)
				{
					nearestTemp += Math.pow(keywordsCount[x][access] - centroid[center][access], 2);
				}
				nearestTemp = Math.sqrt(nearestTemp);
				if(nearestTemp < ifNearestTemp)
				{
					category[x] = center+1;
					ifNearestTemp = nearestTemp;
				}
				nearestTemp = 0;
			}
		}
		for(int i=1;i<=files;i++)
		{
			filename = i + "a.txt";
			//filename = i + "test.txt";
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
			bw.write(Integer.toString(category[i-1]));
			bw.close();
		}
	}
}
