package FinalProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{    
	// ATTRIBUTES
    private int numEntries;								  // num of entries to the table
    private int numBuckets;								  // num of buckets 
    private static final double MAX_LOAD_FACTOR = 0.75;   // load factor needed to check for rehashing 
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; // ArrayList of buckets. Each bucket is a LinkedList of HashPair
        
    /* CONSTRUCTOR:
     * takes int as input representing the initial capacity of the table.
     * using the input, the constructor initializes all the fields.
     */
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS
    	this.numEntries = 0;
    	this.numBuckets = initialCapacity;
    	this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>(this.numBuckets);
    	
    	for(int i=0; i<this.numBuckets; i++) {
    		LinkedList<HashPair<K,V>> bucketLL = new LinkedList<HashPair<K,V>>();
    		this.buckets.add(bucketLL);
    	}    	
        // ADD YOUR CODE ABOVE THIS
    }
    
    // GETTER METHODS
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }    
    
    /*
     * Returns the buckets variable. Useful for testing purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){    
    	
    	for(int i=0; i<this.buckets.size(); i++) {
    		LinkedList<HashPair<K,V>> buck = this.buckets.get(i);
    		System.out.println("BUCKET: "+i);
    		
    		for(int j=0; j<buck.size(); j++) {
    			HashPair<K,V> curr = buck.get(j);
    			System.out.println("PAIR: "+curr.toString());
    		}
    	}
    	
        return this.buckets;

    }
   
   
    
    
    
    // METHODS:
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    
    
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time O(1)
     */
    public V put(K key, V value) { 
    	//ADD YOUR CODE BELOW HERE
    	int keyBucketPosition = this.hashFunction(key);
    	
    	HashPair<K,V> addPair = new HashPair<K,V>(key, value);
    	LinkedList<HashPair<K,V>> addBucket = this.buckets.get(keyBucketPosition);
    	
    	if(this.get(key) != null) {
    		V oldValue = this.get(key);
    		
    		for(int i=0; i<addBucket.size(); i++) {
    			HashPair<K,V> oldPair = addBucket.get(i);
    			
    			if(oldPair.getKey().equals(key)) {
    				oldPair.setValue(value);
    			}
    		}
    		
    		return oldValue;
    	}
    	
    	
    	else {
    		if((this.numEntries / this.numBuckets) > MAX_LOAD_FACTOR) {
    			this.rehash();
    			keyBucketPosition = this.hashFunction(key);
    			addBucket = this.buckets.get(keyBucketPosition);
    		}
    		    		
    		addBucket.add(addPair);
    		this.numEntries++;
    		
    		return null;
    	}
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    public V get(K key) {
        //ADD YOUR CODE BELOW HERE
    	int bucketIndex = this.hashFunction(key);
    	LinkedList<HashPair<K,V>> findEntry = this.buckets.get(bucketIndex);
    	V keyValue = null;

    	for(int i=0; i<findEntry.size(); i++) {
    		HashPair<K,V> currPair = findEntry.get(i);
    		
    		if(currPair.getKey().equals(key)) {
    			keyValue = findEntry.get(i).getValue();
    		}
    	}
        
    	return keyValue;   	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1) 
     */
    public V remove(K key) {
        //ADD YOUR CODE BELOW HERE
    	int keysPosition = this.hashFunction(key);
    	LinkedList<HashPair<K,V>> keysBucket =  this.buckets.get(keysPosition);
    	V keysValue = null;    	
    	
    	for(int i=0; i<keysBucket.size(); i++) {
    		if(keysBucket.get(i).getKey().equals(key)) {
    			keysValue = keysBucket.get(i).getValue();
    			keysBucket.remove(i);
    			this.numEntries--;
    		}    		
    	}
    	
    	return keysValue;
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
        //ADD YOUR CODE BELOW HERE
    	ArrayList<LinkedList<HashPair<K,V>>> biggerHT = new ArrayList<LinkedList<HashPair<K,V>>>(2 * this.numBuckets);

    	for(int i=0; i<(2 * this.numBuckets); i++) {   
    		LinkedList<HashPair<K,V>> tempLL = new LinkedList<HashPair<K,V>>();
    		biggerHT.add(tempLL); 
		}    	
    	
    	this.numBuckets = 2 * this.numBuckets;
    	
    	for(int j=0; j<this.buckets.size(); j++) {
    		LinkedList<HashPair<K,V>> currLL = this.buckets.get(j);
    		
    		for(int k=0; k<currLL.size(); k++) {
    			HashPair<K,V> currPair = currLL.get(k);
    			int newIndex = this.hashFunction(currPair.getKey());
    			
    			LinkedList<HashPair<K,V>> updateLL = biggerHT.get(newIndex);
    			updateLL.add(currPair);
    		}
    	}

		this.buckets = biggerHT;
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public ArrayList<K> keys() {
        //ADD YOUR CODE BELOW HERE
    	ArrayList<K> tableKeys = new ArrayList<K>(this.numEntries);
    	
    	for(int i=0; i<this.numBuckets; i++) {
    		LinkedList<HashPair<K,V>> currBucket = this.buckets.get(i);
    		
    		for(int j=0; j<currBucket.size(); j++) {
    			K keyToAdd = currBucket.get(j).getKey();
    			tableKeys.add(keyToAdd);
    		}
   
    	}
    	        
    	return tableKeys;    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    
    
    /**
     * Returns an ArrayList of UNIQUE values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
        //ADD CODE BELOW HERE
    	ArrayList<V> tableValues = new ArrayList<V>();
    	MyHashTable<V,K> valueTable = new MyHashTable<V,K>(this.numBuckets);
    	
    	for(int i=0; i<this.numBuckets; i++) {
    		LinkedList<HashPair<K,V>> currBucket = this.buckets.get(i);
    		
    		for(int j=0; j<currBucket.size(); j++) {
    			HashPair<K,V> currPair = currBucket.get(j);
    			valueTable.put(currPair.getValue(), currPair.getKey());
    		}
    	}
    	
    	tableValues = valueTable.keys();
    	
    	return tableValues;    	
        //ADD CODE ABOVE HERE
    }
    
    
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults;
    }
    
    
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        //ADD CODE BELOW HERE
    	ArrayList<K> fastSortedK = new ArrayList<K>();
    	LinkedList<HashPair<K,V>> mergeSortList = new LinkedList<HashPair<K,V>>();
    	
    	for(HashPair<K,V> entry : results) {    		
    		mergeSortList.add(entry);
    	}    	    	
    	
    	mergeSortList = mergeSort(mergeSortList);
    	
    	for(HashPair<K,V> sorted : mergeSortList) {
    		K keyToAdd = sorted.getKey();
    		fastSortedK.add(keyToAdd);
    	}    	
    	
    	return fastSortedK;
        //ADD CODE ABOVE HERE
    }
    
    
    
    
    // MERGESORT() helper for fastSort()
    private static <K, V extends Comparable<V>> LinkedList<HashPair<K,V>> mergeSort(LinkedList<HashPair<K,V>> list) {
    	if(list.size() == 1) {
    		return list;
    	}
    	
    	else {
    		int mid = (list.size() - 1) / 2;    

    		LinkedList<HashPair<K,V>> list1 = new LinkedList<HashPair<K,V>>();
    		for(int i=0; i<=mid; i++) {
    			list1.add(list.get(i));
    		}
    		LinkedList<HashPair<K,V>> list2 = new LinkedList<HashPair<K,V>>(); 
    		for(int i=mid+1; i<list.size(); i++) {
    			list2.add(list.get(i));
    		}
			
			list1 = mergeSort(list1);			
    		list2 = mergeSort(list2);
    		
    		return merge(list1, list2);
    	}    	
    	
    }
    
    
    
    
    // MERGE() helper for mergeSort() 
    private static <K, V extends Comparable<V>> LinkedList<HashPair<K,V>> merge(LinkedList<HashPair<K,V>> list1, LinkedList<HashPair<K,V>> list2) {
    	LinkedList<HashPair<K,V>> mergedList = new LinkedList<HashPair<K,V>>();   	
		
    	while(!list1.isEmpty() && !list2.isEmpty()) {
    		HashPair<K,V> list1Pair = list1.get(0);
    		HashPair<K,V> list2Pair = list2.get(0); 

    		if(list1Pair.getValue().compareTo(list2Pair.getValue()) >= 1) {    			
    			mergedList.addLast(list1.removeFirst());    			
    		}
    		
    		else {
    			mergedList.addLast(list2.removeFirst());
    		}
    		    		
    	}
    	
    	while(!list1.isEmpty()) {
    		mergedList.addLast(list1.removeFirst());  
		}
		
		while(!list2.isEmpty()) {
			mergedList.addLast(list2.removeFirst());
		}
    	
    	return mergedList;
    }

    
    
    
    
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        //ADD YOUR CODE BELOW HERE
    	private LinkedList<HashPair<K,V>> hashPairLL;
    	private HashPair<K,V> currHP;
        //ADD YOUR CODE ABOVE HERE
    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
            //ADD YOUR CODE BELOW HERE
        	this.hashPairLL = new LinkedList<HashPair<K,V>>();
        	
        	ArrayList<K> keys = keys();
        	for(int i=0; i<keys.size(); i++) {
        		HashPair<K,V> pairToAdd = new HashPair<K,V>(keys.get(i), get(keys.get(i)));
        		hashPairLL.add(pairToAdd);
        	}        	
        	
        	this.currHP = this.hashPairLL.getFirst();
            //ADD YOUR CODE ABOVE HERE
        }
        
        
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            //ADD YOUR CODE BELOW HERE
        	return (this.hashPairLL.size() != 0);        	
            //ADD YOUR CODE ABOVE HERE
        }
        
        
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            //ADD YOUR CODE BELOW HERE
        	if(!this.hasNext()) {
        		return null;
        	}
        	
        	this.currHP = this.hashPairLL.removeFirst();
        	
        	return this.currHP;
            //ADD YOUR CODE ABOVE HERE
        }
        
    }
    
    
}
