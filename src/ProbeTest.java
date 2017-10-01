
import java.util.Random;
//Skyler Lovecraft (Dylan Scott)
//Hash INIT_POPULATION random numbers into a table of length INIT_CAPACITY using open addressing 
//then add ADDITIONAL_NUMBERS more elements to the table while counting the total number of probes for the last 50.
//https://github.com/SkylerLovecraft/hashTableProbing
public class ProbeTest {
	private static final int INIT_CAPACITY = 1009;
	private static final int INIT_POPULATION = 900;
	private static final int ADDITIONAL_NUMBERS = 50;

	public static void main(String[] args) {
		int[] keys;
		int[] values;
		keys = new int[INIT_CAPACITY];
		values = new int[INIT_CAPACITY];
		System.out.println("This program will create a table with " +INIT_CAPACITY + " cells \nand randomly populate it using open addressing"
				+ " with " +INIT_POPULATION + " random distinct values. \nIt will then add " +ADDITIONAL_NUMBERS +" more random values using "
						+ "linear probing, quadratic probing, and \ndouble hashing and report the number of probes to insert these last numbers");
		linearProbe(values, keys);
		quadraticProbe(values, keys);
		doubleHash(values, keys);
		
		System.exit(1);
	}

	
	/*
	 * 1. Set all the elements to NULL in the array. (Interpreting table[ndx] == -1; to be an empty cell
	 * 2. Hash 900 random numbers into the table using oopen addressing
	 * 3. Add  another 50 elements to the table using linear/quadratic/double probing
	 * 			3-a. Count the total number of probes for the combined final numbers.
	 * 4. Print the result of this count. 
	 * 5. return
	 * */
	public static void linearProbe(int[] values, int[] keys) {
		//(h'(i) + i)modm
		clearTable(values, keys);
		populateTable(values, keys);
		int numCollisions = 0;
	    for(int ndx = 0; ndx < 50;++ndx) {
		Random rand = new Random();
		int randomNum = 0;
		do {
			randomNum = rand.nextInt((10000 - 0) + 1) + 0;
		}
		while(search(values, randomNum) == true);

	    int key = hash(randomNum);
        for (ndx = key; ndx < INIT_CAPACITY; ndx = (ndx + 1) % 1008) {
            if (keys[ndx] == -1) {
                values[ndx] = randomNum;
                break;
            }
            ++numCollisions;
        }
	    }
	    System.out.println("numCollisions linearProbing: " +numCollisions);
	}
	public static void quadraticProbe(int[] values, int[] keys) {
		//h(k,i) = (h'(k) + c1* i + c2 * i ^2) % m
		clearTable(values, keys);
		populateTable(values, keys);
		int numCollisions = 0;
		int c1 = 1, c2 = 3;
		int probeNumber = 0;

		for(int ndx = 0; ndx < 50;++ndx) {
			Random rand = new Random();
			int randomNum = 0;
			do {
				randomNum = rand.nextInt((10000 - 0) + 1) + 0;
			}
			while(search(values, randomNum) == true);

		    int key = hash(randomNum);
	        for (ndx = key; ndx < INIT_CAPACITY; ndx = (key + c1 * probeNumber + c2 * probeNumber * probeNumber)%(INIT_CAPACITY -1) ){
	            if (keys[ndx] == -1) {
	                values[ndx] = randomNum;
	                break;
	            }
	            ++probeNumber;
	            ++numCollisions;
	        }
	        probeNumber = 0;
		    }		
	    System.out.println("numCollisions quadraticProbing: " +numCollisions);
	}
	
	public static void doubleHash(int[] values, int[] keys) {
		//h1(k) = k;
		//h2(k) = 1 + (k mod(m - 1));
		clearTable(values, keys);
		populateTable(values, keys);
		int h1, h2;
		int numCollisions = 0;
		int probeNumber = 0;
		for(int ndx = 0; ndx < 50;++ndx) {
				Random rand = new Random();
				int randomNum = 0;
				do {
					randomNum = rand.nextInt((10000 - 0) + 1) + 0;
				}
				while(search(values, randomNum) == true);

			    int key = hash(randomNum);
			    h1 = key;
			    h2 = (key % (INIT_CAPACITY - 2));
		        for (ndx = key; ndx < INIT_CAPACITY; ndx = (h1 + probeNumber * h2)%(INIT_CAPACITY - 1)){
		            if (keys[ndx] == -1) {
		                values[ndx] = randomNum;
		                break;
		            }
		            ++probeNumber;
		            ++numCollisions;
		        }
		        probeNumber = 0;
			    }
		 System.out.println("numCollisions doubleHash: " +numCollisions);

	}

	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////----TOTALLY COMPLETED FUNCTIONS----///////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void populateTable(int[] values, int[] keys) {
	    for(int ndx = 0; ndx < INIT_CAPACITY;) {
		Random rand = new Random();
	    int randomNum = rand.nextInt((10000 - 0) + 1) + 0;
	    int key = hash(randomNum);
	    if(insert(values, randomNum, key, keys) == true)
	    {
	    	ndx++;
	    }
	    
	    }
	}
	//Insert value into the array
	public static boolean insert(int [] values, int value, int key, int[] keys) {
		//System.out.println(+key);
		if(keys[key] == -1) {
			if(search(values, value) == true)
			{
				return false;
			}
			keys[key] = 1;
			values[key] = value;
			return true;
		}
		else if(keys[key] == 1)
		{
			if(key + 1 >= INIT_CAPACITY)
			{
				key = -1;
			}
			insert(values, value, key + 1, keys);
			return true;
		}
		return false;
	}
	

	
	public static int hash(int value) {
		int key = 0;
		key = value % 1008;
		return key;
	}
	
	//Remove value from array
	public static void remove(int [] a, int value) {
		if(search(a, value) == false)
		{
			return;
		}
		else if(search(a, value) == true) {
			for (int ndx = 0; ndx < INIT_CAPACITY; ++ndx)
			{
				if(a[ndx] == value)
				{
					a[ndx] = -1; 
					return;
				}
			}
		}
	}
	//Searches the array to see if value exists in the array
	public static boolean search(int [] a, int value) {
		for(int ndx = 0; ndx < INIT_CAPACITY; ++ndx) {
			if (a[ndx] == value)
				return true;
		}
		return false;
	}
	
	//Set all elements in the array to -1, interpreting -1 to be an empty cell in the table. 
	public static void clearTable(int[] values, int[] table) {
		for (int ndx = 0; ndx < INIT_CAPACITY; ++ndx)
		{
			table[ndx] = -1;
			values[ndx] = -1;
		}
	}
}
