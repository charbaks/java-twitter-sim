package FinalProject;
import java.util.ArrayList;

public class Twitter {
	
	//ADD YOUR CODE BELOW HERE
	private ArrayList<Tweet> tweets;
	private ArrayList<String> stopWords;
	private MyHashTable<String, ArrayList<Tweet>> dates;
	private MyHashTable<String, ArrayList<Tweet>> authors;
	private MyHashTable<Tweet, ArrayList<String>> stops;
	//ADD CODE ABOVE HERE 
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		//ADD YOUR CODE BELOW HERE
		this.tweets = tweets;
		this.stopWords = stopWords;

		this.authors = new MyHashTable<String, ArrayList<Tweet>>(this.tweets.size());
		this.dates = new MyHashTable<String, ArrayList<Tweet>>(this.tweets.size());
		this.stops = new MyHashTable<Tweet, ArrayList<String>>(this.tweets.size());
				
		for(Tweet t : this.tweets) {
			String currAuthor = t.getAuthor();
			if(this.authors.get(currAuthor) == null) {
				this.authors.put(currAuthor, this.authorTableHelper(currAuthor));
			}
			
			String currDate = t.getDateAndTime().substring(0, 10);
			if(this.dates.get(currDate) == null) {
				this.dates.put(currDate, this.dateTableHelper(currDate));
			}	
			
			this.stops.put(t, this.stopsTableHelper(t));	
		}		
		//ADD CODE ABOVE HERE 
	}

	
	
	
	// AUTHOR TABLE HELPER METHOD
	private ArrayList<Tweet> authorTableHelper(String author) {
		ArrayList<Tweet> allTweetsFromAuthor = new ArrayList<Tweet>();
		
		for(Tweet t : this.tweets) {
			if(t.getAuthor().equalsIgnoreCase(author)) {
				allTweetsFromAuthor.add(t);
			}
		}		
		
		return allTweetsFromAuthor;
	}	
	
	

	
	// DATE TABLE HELPER METHOD
	private ArrayList<Tweet> dateTableHelper(String date) {
		ArrayList<Tweet> allTweetsFromDate = new ArrayList<Tweet>();
		
		for(Tweet t : this.tweets) {
			String tweetsDate = t.getDateAndTime().substring(0, 10);
			
			if(tweetsDate.equalsIgnoreCase(date)) {
				allTweetsFromDate.add(t);
			}
		}
		
		return allTweetsFromDate;
	}
	
	

	
	// STOP TABLE HELPER METHOD
	private ArrayList<String> stopsTableHelper(Tweet currTweet) {
		ArrayList<String> stopWordsInTweet = new ArrayList<String>();
		String[] message = currTweet.getMessage().split(",");
		
		ArrayList<String> messageWords = new ArrayList<String>();		
		for(String s : message) {
			if(stopWordsInTweet.contains(s)) {
				messageWords.add(s);
			}
		}
		
		for(String s : this.stopWords) {
			
			if(messageWords.contains(s)) {
				if(!stopWordsInTweet.contains(s)) {
					stopWordsInTweet.add(s);
				}
			}
		}
		return stopWordsInTweet;
	}
		
	
	
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		//ADD CODE BELOW HERE
		this.tweets.add(t);
		
		String currAuthor = t.getAuthor();
		if(this.authors.get(currAuthor) == null) {
			this.authors.put(currAuthor, this.authorTableHelper(currAuthor));
		}
		
		String currDate = t.getDateAndTime().substring(0, 10);
		if(this.dates.get(currDate) == null) {
			this.dates.put(currDate, this.dateTableHelper(currDate));
		}		
		
		this.stops.put(t, this.stopsTableHelper(t));
		//ADD CODE ABOVE HERE 
	}
	

	
	
    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
        //ADD CODE BELOW HERE
    	Tweet latestTweet = null;
    	ArrayList<Tweet> tweets = new ArrayList<>();
    	
    	if(this.authors.get(author) != null) {
    		tweets = this.authorsOrderedDates(author);
    		latestTweet = tweets.get(0);
    	}

    	return latestTweet;
        //ADD CODE ABOVE HERE 
    }
    
    
    
    
    // AUTHORS TWEETS BY DATE HELPER
 	private ArrayList<Tweet> authorsOrderedDates(String author) {
 		ArrayList<Tweet> sortedTweets = new ArrayList<Tweet>();
 		ArrayList<Tweet> tweets = this.authors.get(author);
 		MyHashTable<Tweet, String> tweetsByDate = new MyHashTable<Tweet, String>(tweets.size());
 		
 		for(Tweet t : tweets) {
 			tweetsByDate.put(t, t.getDateAndTime());
 		}
 		
 		sortedTweets = tweetsByDate.fastSort(tweetsByDate);
 		
 		return sortedTweets;
 	}
    
    

 	
    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
        //ADD CODE BELOW HERE
    	ArrayList<Tweet> tweetsOnDate = this.dates.get(date);
    	
    	if(tweetsOnDate.size() == 0) {
    		return null;
    	}
    	
    	return tweetsOnDate; 	
        //ADD CODE ABOVE HERE
    }
    
    
    
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */    
    public ArrayList<String> trendingTopics() {
        //ADD CODE BELOW HERE
    	ArrayList<String> freqOfWords = new ArrayList<String>();
    	ArrayList<String> totalWords = new ArrayList<String>();
    	for(Tweet t : this.tweets) {
    		totalWords.addAll(this.wantedWords(t));
    	}
    	    	
    	MyHashTable<String, Integer> orderedWords = new MyHashTable<String, Integer>(totalWords.size());    	
    	for(String str : totalWords) {
    		Integer count = orderedWords.get(str);
    		
    		if(count == null) {
    			count = 1;
    		}
    		
    		else {
    			count = count + 1;
    		}
    		
    		orderedWords.put(str, count);
    	}
    	
    	freqOfWords = orderedWords.fastSort(orderedWords);
    	
    	return freqOfWords;  	
        //ADD CODE ABOVE HERE    	
    }
    
    
    
    
    private ArrayList<String> wantedWords(Tweet tweet) {
    	ArrayList<String> allWordsInTweet = getWords(tweet.getMessage());
    	ArrayList<String> noDuplicates = new ArrayList<String>();
    	
    	for(String str : allWordsInTweet) {
    		if(!noDuplicates.contains(str)) {
    			noDuplicates.add(str);
    		}
    	}
    	
    	ArrayList<String> stopWordsInTweet = this.stops.get(tweet);
    	
    	noDuplicates.removeAll(stopWordsInTweet);
    	
    	return noDuplicates;
    }
    
    
    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    

}
