package discuss1TruncatedDifferential;

import java.io.IOException;
import java.util.Random;

//test new four-round distinguisher using random number as the pseudorandom permutation: byte 1 is inactive in the condition that byte 0 is inactive
//we use 100 threads to test separately, take key0 as an example: key0 represents the 0th group of random key experiments using 2^12 δ-sets
public class Experiment_FourRoundDistinguisher_RandomNumber_Conditional_12_key0 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();    //获取开始时间
		
		int keynum = 0;
		
		int[][] byte_positions = new int[][] {
			{0, 1}, {5, 6}, {10, 11}, {15, 12}
		};
		
		long[] allCount = new long[2];
		
		for(int i = 0; i < DELTASETSIZE; i++) {
			int[][] cipher = getRandomDeltaSet();
			int[] count = getZeroDifferentialByOneDeltaSetCipherText(cipher, byte_positions[0]);
			
			allCount[0] += count[0];
			allCount[1] += count[1];
		}
		
		System.out.println("----------------------------");
		System.out.println("random generate number");	
		System.out.println("key" + keynum + " : " + allCount[0] + " " + allCount[1]);	
		long endTime = System.currentTimeMillis();    
		System.out.println("run time：" + (endTime - startTime) + "ms");    
		System.out.println("----------------------------");
	
		
	}
	
	public static int DELTASETSIZE = (int) Math.pow(2, 12);
	
	public static int[][] getRandomDeltaSet() {
		
		Random rd = new Random();
		
		int[][] delta = new int[16][16];
		for(int i = 0; i < delta.length; i++) {
			for(int j = 0; j < 16; j++) {
				delta[i][j] = rd.nextInt(16);
			}
		}
		
		return delta;
		
	}
	
	// find collisions
	public static int[] getZeroDifferentialByOneDeltaSetCipherText(int[][] ciphertext, int[] byte_position) {
		
		int[] count = new int[2];
		for(int i = 0; i < ciphertext.length-1; i++) {
			for(int j = i+1; j < ciphertext.length; j++) {
				
				int num0 = ciphertext[i][4*byte_position[0]] ^ ciphertext[j][4*byte_position[0]];
				int num1 = ciphertext[i][4*byte_position[1]] ^ ciphertext[j][4*byte_position[1]];
				
				if(num0 == 0) {
					count[0]++;
					if(num1 == 0) {
						count[1]++;
					}
				}
					
			}
		}
		
		return count;
		
	}

}
