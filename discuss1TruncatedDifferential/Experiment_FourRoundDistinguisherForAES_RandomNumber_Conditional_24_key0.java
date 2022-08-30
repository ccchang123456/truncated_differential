package discuss1TruncatedDifferential;

import java.io.IOException;
import java.util.Random;

//test new four-round distinguisher using random number as the pseudorandom permutation: byte 1 is inactive in the condition that byte 0 is inactive
//we use 100 threads to test separately, take key0 as an example: key0 represents the 0th group of random key experiments using 2^24 ¦Ä-sets
public class Experiment_FourRoundDistinguisherForAES_RandomNumber_Conditional_24_key0 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();    
		
		int keynum = 0;

		long[] allCount = new long[2];
		
		for(int i = 0; i < DELTASETSIZE; i++) {
			long[] onecount = getZeroDifferentialByOneDeltaSetCipherText();
			
			allCount[0] += onecount[0];
			allCount[1] += onecount[1];
			

		}
	
		System.out.println("----------------------------");
		System.out.println("Random number");	
		System.out.println("key" + keynum + " : " + allCount[0] + " " + allCount[1]);	
		long endTime = System.currentTimeMillis();    
		System.out.println("run time£º" + (endTime - startTime) + "ms");    
		System.out.println("----------------------------");
		
	}
	
	public static int DELTASETSIZE = (int) Math.pow(2, 24);
	
	public static long[] getZeroDifferentialByOneDeltaSetCipherText() {
		
		int oneDelta = 256;		
		int pairs = (oneDelta * (oneDelta - 1)) / 2;
		long[] count = new long[2];
		
		Random rd = new Random();
		for(int i = 0; i < pairs; i++) {
			
			int num = rd.nextInt(256);
			if(num == 0) {
				count[0]++;
			}
			
			int num0 = rd.nextInt(256);
			int num1 = rd.nextInt(256);
			if(num0 == 0 && num1 == 0) {
				count[1]++;
			}
		}
		
		return count;
		
	}

}
