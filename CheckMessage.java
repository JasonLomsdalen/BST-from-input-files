//Jason Lomsdalen
//Assignment 4
//Buckalew
import mycs1.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class CheckMessage
{
	private static RecordBST spellCheck = new RecordBST();
	private static RecordBST alarmWords = new RecordBST();
	private static int numArgs, numAlarmFiles;
	private static String textMessageFile, spellingFile;
	private static TextIO inFile, alarmFile, messageFile;
	private static String[] alarms;
	private static String correctlySpelled;

	public static void main (String[] args)
	{
		//Get number of text files passed in
		numArgs = args.length;
		//Check to see if arguments were passed in for message file and spelling file
		try
		{
			textMessageFile = args[0];
			spellingFile = args[1];
		}
		catch (Error e)
		{
			throw new Error("Didn't enter message file and word file.");
		}
		//Takes words from spelling files and inserts them into a BST
		try
		{
			inFile = new TextIO(spellingFile, TextIO.INPUT);
			while(!inFile.endOfFile())
			{
				ItemRecord newItem = new ItemRecord(inFile.readToken());
				spellCheck.insert(newItem);
			}
			inFile.close();
		}
		catch (TextIO.Error e)
		{
			throw new Error("Unable to open input file " + spellingFile);
		}
		//Takes in words from multiple alarm files and inserts them into a single BST
		try
		{
			//Makes sure alarm files were passed in
			if(args.length > 2)
			{
				//Determine number of alarm files passed in
				numAlarmFiles = args.length - 2;
				//Create an array of the alarm file names
				alarms = new String[numAlarmFiles];
				//For each alarm file go trhough line by line and insert to BST
				for(int i = 0; i < numAlarmFiles; i++)
				{
					alarms[i] = args[i+2];
					alarmFile = new TextIO(alarms[i], TextIO.INPUT);
					while(!alarmFile.endOfFile())
					{
						ItemRecord newItem = new ItemRecord(alarmFile.readToken(), false);
						alarmWords.insert(newItem);
					}
					alarmFile.close();
				}
			}
		}
		catch(Error e)
		{
			throw new Error("Unable to open alarm word files.");
		}
		
		//Scan message text file words against spellcheck BST
		messageFile = new TextIO(textMessageFile, TextIO.INPUT);
		System.out.println("\nAnalyzing the file "+args[0]+"\n");
		System.out.println("Checking against the file "+args[1]+":");
		System.out.println("these words were not found:");
		//Goes until end of message file is reached
		while(!messageFile.endOfFile())
		{
			ItemRecord newItem = new ItemRecord(messageFile.readToken());
			int wordLength = newItem.token.length();
			String word = newItem.token;
			//If the word is not found it will first look to see if it is found after removing
			//"es", "s", or "ing".  If still not found it will check for possible spelling errors
			//by swapping consecutive characters and rechecking the BST each time.
			if(!spellCheck.searchSpelling(newItem))
			{
				if(word.endsWith("es"))
				{
					//First remove the s and search for the word in BST
					word = word.substring(0,wordLength-1);
					//If it doesn't get found remove the 'e'
					if(!checkBST(word))
					{
						word = word.substring(0,wordLength-2);
						//Recheck to see if found in BST
						if(!checkBST(word))
						{
							//If it is found print the possible spelling
							correctlySpelled = swapChars(word);
							System.out.println(word + "\tpossible spellings: "+correctlySpelled);
						}
					}
				}
				else if(word.endsWith("s"))
				{
					word = word.substring(0,wordLength-1);
					if(!checkBST(word))
					{
						correctlySpelled = swapChars(word);
						System.out.println(word + "\tpossible spellings: "+correctlySpelled);
					}
				}
				else if(word.endsWith("ing"))
				{
					word = word.substring(0,wordLength-3);
					if(!checkBST(word))
					{
						correctlySpelled = swapChars(word);
						System.out.println(word + "\tpossible spellings: "+correctlySpelled);
					}
				}
				else 
				{
					correctlySpelled = swapChars(word);
					System.out.println(word + "\tpossible spellings: "+correctlySpelled);
				}
			}
		}
		
		//Scan message against alarm BST
		messageFile = new TextIO(textMessageFile, TextIO.INPUT);
		System.out.println("\nChecking against the alarm files "+Arrays.deepToString(alarms).replace("[", "").replace("]", "")+":");
		System.out.println("these words were found:");
		//Goes through message file and scans each word against Alarm BST
		while(!messageFile.endOfFile())
		{
			//Casts alarm word from file as an ItemRecord object
			//then calls search to look in the Alarm BST for it
			ItemRecord newItem = new ItemRecord(messageFile.readToken());
			String word = newItem.token;
			alarmWords.searchAlarm(newItem);
		}
		//Prints alarm words in order traversal of the Alarm BST
		alarmWords.inOrder(alarmWords.root);
		messageFile.close();
	}

	//Method to swap the characters and return the word if found in BST
	public static String swapChars(String word)
	{
		//Puts word into an array of characters, so you can easily swap them
		char[] c = word.toCharArray();
		//Goes through word and swaps adjacent pairs of letters
		for (int i = 0; i<word.length()-1; i++)
		{
			char temp = c[i];
			c[i] = c[i+1];
			c[i+1] = temp;
			String swappedWord = new String(c);
			ItemRecord newWord = new ItemRecord(swappedWord);
			//If the word is found after swapping it gets returned 
			if(spellCheck.searchSpelling(newWord))
			{
				return swappedWord;
			}
			//Swap character back before moving along in word
			temp = c[i];
			c[i] = c[i+1];
			c[i+1] = temp;
		}
		return "";
	}

	//Method to call search method for word in BST
	public static boolean checkBST(String word)
	{
		ItemRecord item = new ItemRecord(word);
		//Returns true if the word is found in the spelling BST
		if(spellCheck.searchSpelling(item))
		{
			return true;
		}
		return false;
	}
}