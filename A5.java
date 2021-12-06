/**
 * Assignment 5
 *
 * @author YOUR NAME GOES HERE
 * @version 12/13/2021
 */

import java.io.*;
import java.util.*;

public class A5
{        
    /** define your two data structures below */
    private static Map<Integer, TreeSet> dict = new HashMap<>();
    private static Map<String, List<String>> neighbors = new TreeMap<>();

    /**
     *  Given the name of a text file, loads the words contained
     *  in that file into the dict data structure. You should assume
     *  that if the file exists, it contains only words made up of
     *  upper- and/or lowercase letters separated by the default delimiter
     *  characters (e.g., blank space, newline character, etc.). In the end,
     *  dict may only contain words in all lowercase letters with no duplicates. 
     *  If the file does not exist, a clear message must be printed on the 
     *  terminal before terminating the program. 
     *  
     *  Does NOT send any output to the terminal if the input file exists.
     *   
     *  @param fileName   name of the text file of words
     */
    public static void loadWords(String fileName)
    {
        Scanner input = null;
        Set currentSet;
        try{
            input = new Scanner(new File(fileName));
            while (input.hasNext())
            {
                String word = input.next().toLowerCase();
                if (dict.get(word.length()) == null)
                    dict.put(word.length(), new TreeSet<String>());
                currentSet = dict.get(word.length());
                currentSet.add(word);
            }
        }catch(Exception e){
            System.out.println("Error reading from " + fileName + ". Program will now terminate.");
            System.exit(0);
        }finally{
            if (input != null)
                input.close();
        }
    }// loadWords method

    /**
     *  Given a word in dict, insert an entry into the neighbors data structure
     *  with this word as the key and the non-empty, alphabetized list of all of the word's 
     *  neighbors as the value, where a neighbor of a word w is any other word in dict 
     *  that is obtained by replacing exactly (any) one letter in w by another letter.
     *  Does not modify neighbors if the given word has no neighbors in dict.
     *  
     *  Does NOT send any output to the terminal.
     *  
     *  @param word   the word whose neighbors you must determine and store in neighbors
     */
    public static void findNeighbors(String word)
    {
        TreeSet<String> words = dict.get(word.length());
        List<String> wordList = new LinkedList<>();
        
        for(String w : words)
        {
            int strikes = 0;
            for(int i=0; i<word.length(); i++)
            {
                if(w.charAt(i) != word.charAt(i))
                    if(++strikes == 2) break;
            }
            if(strikes == 1) wordList.add(w);
        }
        if(wordList.size() != 0) neighbors.put(word, wordList);
    }// findNeighbors method

    /**
     *  Populates the neighbors data structure with an entry for each one of the words in 
     *  dict whose neighbors list is not empty.
     *  
     *  Does NOT send any output to the terminal.
     */
    public static void findAllNeighbors()
    {
        Set<String> currentSet;
        for (int key : dict.keySet()) 
        {
            currentSet = dict.get(key);
            for (String word : currentSet) findNeighbors(word);
        }
    }// findAllNeighbors method

    /**
     *  Sends the contents of the neighbors data structure to the terminal.
     *  The required format of this output is described in the handout for this
     *  assignment.
     */
    public static void printAllNeighbors()
    {
        for (String key : neighbors.keySet()) {
            System.out.print(key + ":");
            for(String word: neighbors.get(key)) {
                System.out.print(" " + word);
            }
            System.out.println();
        }
    }// printAllNeighbors method

    public static void testMe()
    {
        loadWords("commonWords.txt");
        findAllNeighbors();
        //printAllNeighbors();
        
        ArrayList<String> sequence = findSequence( "cat", "dog" );
        System.out.println( sequence );

        sequence = findSequence( "dry", "wet" );
        System.out.println( sequence );

        sequence = findSequence( "golf", "ball" );
        System.out.println( sequence );

        sequence = findSequence( "poor", "rich" );
        System.out.println( sequence );

        sequence = findSequence( "white", "black" );
        System.out.println( sequence );

        sequence = findSequence( "synonym", "homonym" );
        if (sequence == null)
            System.out.println( 
                "There is no sequence from \"synonym\" to \"homonym\"." );
        else
            System.out.println( sequence );
    }

    /**
     *  Returns a sequence connecting the two given words, where a sequence is a list
     *  of words with each word (except the first one) being a neighbor of the preceding
     *  word; or returns null if no such sequence exists (based on the dictionary used).
     *  
     *  Does NOT send any output to the terminal.
     *  
     *  @param  start   the word that must appear first in the sequence
     *  @param  finish  the word that must appear last in the sequence
     *  
     *  For full credit, your method must implement the following algorithm:
     *   + you must use a basic (i.e., FIFO) queue, called q, containing ArrayList<String>
     *     instances; each array list will be a partial sequence starting with the start 
     *     word
     *   + initialize q to contain a single ArrayList with start as its only element 
     *   + you will implement a loop that terminates when the first sequence ending in 
     *     the finish word is about to be added to q or when q is empty (in the latter case,
     *     the method must return null).
     *   + during each iteration, you must:
     *       + dequeue the first element (i.e., ArrayList) of q; call this element 'current'
     *       + find the list of neighbors of current's last element
     *           + if one of them is the finish word, return current with this word added at
     *             the end
     *           + if this list is empty, end the iteration since there is no way to 
     *             extend the current partial sequence
     *           + otherwise, for each neighbor n in the list:
     *               + make a copy of current and add n at the end of this copy
     *               + enqueue the resulting ArrayList into q
     *   
     *  This method may ONLY use the Queue (not Deque, etc.) methods that do NOT throw
     *  any exceptions.
     *  
     *  Sample outputs for this method are provided in the handout for this assignment.
     */
    public static ArrayList<String> findSequence(String start, String finish)
    {
        // private static Map<String, List<String>> neighbors = new TreeMap<>();
    
        Queue<ArrayList<String>> q = new LinkedList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add(start);
        q.offer(list);
        
        ArrayList<String> current = list;
        List<String> wordList;
        
        while(true) {
            if(q.size() == 0) return null;
            current = q.poll();
            wordList = (LinkedList) neighbors.get(current.get(current.size() - 1));
            if(wordList == null) return null;
            if(wordList.size() == 0) break;
            if(wordList.contains(finish)) {
                current.add(finish);
                return current;
            }
            for(String word : wordList) {
                
                
                ArrayList<String> copy = (ArrayList<String>)current.clone();
                copy.add(word);
                q.add(copy);
            }
        }
        

        return current;
    }// findSequence method

    /**
     *  Driver code, which you may NOT modify.
     *  
     *  Usage:
     *           java A5 <file name> neighbors
     *        or
     *           java A5 <file name> 
     */
    public static void main(String[] args)
    {
        loadWords(args[0]);
        findAllNeighbors();

        if (args.length > 1 && args[1].equals( "neighbors" ))
            printAllNeighbors();
        else
        {
            ArrayList<String> sequence = findSequence( "cat", "dog" );
            System.out.println( sequence );

            sequence = findSequence( "dry", "wet" );
            System.out.println( sequence );

            sequence = findSequence( "golf", "ball" );
            System.out.println( sequence );

            sequence = findSequence( "poor", "rich" );
            System.out.println( sequence );

            sequence = findSequence( "white", "black" );
            System.out.println( sequence );

            sequence = findSequence( "synonym", "homonym" );
            if (sequence == null)
                System.out.println( 
                    "There is no sequence from \"synonym\" to \"homonym\"." );
            else
                System.out.println( sequence );

        }
    }// main method
}// A5 class

