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
		int[][] keywordsCount = new int[files*2][set.size()];
		
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
		//Training
		int[] category = new int[files*2];
		for(int i=1;i<=files;i++)
		{
			String filename = i + "a.txt";
			String s = "";
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while(br.ready())
			{
				s = br.readLine();
				category[i-1] = Integer.parseInt(s);
			}
			br.close();
		}
		//Verify
		//Get prepared
		/*for(int i=1+files;i<=files*2;i++)
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
		*/
		/******************************************************
		 k-NN algorithm
		 *****************************************************/
		/*int temp = 0;
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<set.size();j++)
			{
				temp += Math.pow(keywordsCount[i][j] - keywordsCount[i+1][j], 2);
			}
			temp = (int)Math.sqrt(temp);
			System.out.println(temp);
			temp = 0;
		}*/
		
		
		/*
		int radius = 6;
		for(int i=0+files;i<files*2;i++)
		{
			int[] countOfPoints = new int[4];
			for(int j=0;j<files;j++)
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
				}
			}
		}
		for(int i=0;i<files*2;i++)
			System.out.println(category[i]);
			
		*/
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
			//System.out.println(category[i-1]);
		
		String filename = "Supervised model.txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		for(int i=0;i<files;i++)
		{
			for(int j=0;j<set.size();j++)
			{
				bw.write(Integer.toString(keywordsCount[i][j]));
				bw.write("\r\n");
			}
			bw.write(Integer.toString(category[i]));
			bw.write("\r\n");
		}
		
		bw.close();
		filename = "Dictionary.txt";
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		for (String s : sortedList) {
		    bw.write(s);
		    bw.write("\r\n");
		}
		bw.close();
	}		
}
