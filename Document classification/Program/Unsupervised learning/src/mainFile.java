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

//http://murphymind.blogspot.tw/2017/06/machine-learning-unsupervised-learning.html
//https://www.youtube.com/watch?v=_aWzGGNrcic

public class mainFile {

	public static void main(String[] args) throws IOException{
		System.out.println("How many traning files?");
		Scanner scanner = new Scanner(System.in);
		int files = scanner.nextInt();
		scanner.close();
		/******************************************************
		 Calculate how many distinct words
		 *****************************************************/
		Set<String> set = new HashSet<String>();
		for(int i=1;i<=files;i++)
		{
			String filename = i + ".txt";
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String[] stringArray = null;
			
			while(br.ready())
			{
				String s = br.readLine();
				stringArray = s.split(" |,");
				for(String token : stringArray)
				{
					set.add(token);
				}
			}
			br.close();
		}
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
			String filename = i + ".txt";
			BufferedReader br = new BufferedReader(new FileReader(filename));
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
		for(int i=0;i<files;i++)
		{
			category[i] = (int) ((Math.random()*(set.size()/2)));
		}
		
		/******************************************************
		 k-means algorithm
		 *****************************************************/
		for(int i=0;i<5;i++)
		{
			//Calculate centers
			for(int j=0;j<4;j++)
			{
				int[] centroidTemp = new int[set.size()];
				int categoryCount=0;
				for(int k=0;k<files;k++)
				{
					if(category[k] == j+1)
					{
						categoryCount++;
						for(int l=0;l<set.size();l++)
						{
							centroidTemp[l] += keywordsCount[k][l];
						}
					}
				}
				for(int k=0;k<set.size();k++)
				{
					if(categoryCount == 0)
						categoryCount++;
					centroid[j][k] = centroidTemp[k]/categoryCount;
				}
			}
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
		}
		/******************************************************
		 Write files
		 *****************************************************/
		/*for(int i=1;i<=files;i++)
		{
			String filename = i + "a.txt";
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
			bw.write(Integer.toString(category[i-1]));
			bw.close();
		}*/
		String filename = "Unsupervised model.txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<set.size();j++)
			{
				bw.write(Integer.toString(centroid[i][j]));
				bw.write("\r\n");
			}
		}
		
		bw.close();
		filename = "Dictionary.txt";
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		for (String s : sortedList) {
		    bw.write(s);
		    bw.write("\r\n");
		}
		bw.close();
			//System.out.println(category[i-1]);
	}		
}
