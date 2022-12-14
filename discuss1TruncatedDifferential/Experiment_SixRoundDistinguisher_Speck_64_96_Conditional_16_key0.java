package discuss1TruncatedDifferential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//test new extended six-round distinguisher using speck-64-96 as the pseudorandom permutation: D1 is inactive in the condition that D0 is inactive
//we use 100 threads to test separately, take key0 as an example: key0 represents the 0th group of random key experiments using 2^16 structures
public class Experiment_SixRoundDistinguisher_Speck_64_96_Conditional_16_key0 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();    
		
		int keynum = 0;
		
		int[][] byte_positions = new int[][] {
			{0, 5, 10, 15, 1, 6, 11, 12},
			{1, 6, 11, 12, 2, 7, 8, 13},
			{2, 7, 8, 13, 3, 4, 9, 14},
			{3, 4, 9, 14, 0, 5, 10, 15}
		};
		
		long[][] allCount = new long[4][2];
		
		for(int i = 0; i < DELTASETSIZE; i++) {
			int[][] cipher = getCipherByDeltaSet(i);
			int[] count_0 = find_collisions(cipher, byte_positions[0]);
			int[] count_1 = find_collisions(cipher, byte_positions[1]);
			int[] count_2 = find_collisions(cipher, byte_positions[2]);
			int[] count_3 = find_collisions(cipher, byte_positions[3]);
			
			allCount[0][0] += count_0[0];
			allCount[0][1] += count_0[1];
			allCount[1][0] += count_1[0];
			allCount[1][1] += count_1[1];
			allCount[2][0] += count_2[0];
			allCount[2][1] += count_2[1];
			allCount[3][0] += count_3[0];
			allCount[3][1] += count_3[1];
		}
		
		long[] all = new long[2];
		for(int i = 0; i < allCount.length; i++) {
			all[0] += allCount[i][0];
			all[1] += allCount[i][1];
		}
		
		System.out.println("----------------------------");
		System.out.println("speck-64-96");	
		System.out.println("key" + keynum + " : " + all[0] + " " + all[1]);	
		System.out.println(allCount[0][0] + " " + allCount[0][1] + " " + allCount[1][0] + " " + allCount[1][1] + " " + allCount[2][0] + " " + allCount[2][1] + " " + allCount[3][0] + " " + allCount[3][1]);
		
		long endTime = System.currentTimeMillis();    
		System.out.println("run time??" + (endTime - startTime) + "ms");    
		System.out.println("----------------------------"); 
		
	}
	
	public static int DELTASETSIZE = (int) Math.pow(2, 16);
	
	public static int BLOCK_SIZE = 64;
	public static int KEY_SIZE = 96;
	public static int WORD_SIZE = 32;
	public static int KEY_WORDS = 3;
	public static int ROT_ALPHA = 8;
	public static int ROT_BETA = 3;
	public static int ROUNDS_T = 26;
	
//	static byte[] test_key = new byte[] {0x13, 0x12, 0x11, 0x10, 0x0b, 0x0a, 0x09, 0x08, 0x03, 0x02, 0x01, 0x00};
//	static int[] test_plaintext = new int[] {0x74, 0x61, 0x46, 0x20, 0x73, 0x6e, 0x61, 0x65};
//	static int[] test_expected_ciphertext = new int[] {0x9f, 0x79, 0x52, 0xec, 0x41, 0x75, 0x94, 0x6c};
	
	public static int[] find_collisions(int[][] ciphertext, int[] byte_positions) {		
		
		int number = (int) Math.pow(2, 16);
		List list = new ArrayList<>();
		for(int i = 0; i < number; i++) {
			List list_initial = new ArrayList<>();
			list.add(list_initial);
		}
		for(int i = 0; i < ciphertext.length; i++) {
			String str_byte4 = get4BitString(ciphertext[i][byte_positions[0]]) + get4BitString(ciphertext[i][byte_positions[1]]) + get4BitString(ciphertext[i][byte_positions[2]]) + get4BitString(ciphertext[i][byte_positions[3]]);
			String str_byte8 = get4BitString(ciphertext[i][byte_positions[4]]) + get4BitString(ciphertext[i][byte_positions[5]]) + get4BitString(ciphertext[i][byte_positions[6]]) + get4BitString(ciphertext[i][byte_positions[7]]);
			int number_byte4 = Integer.parseInt(str_byte4, 2);
			int number_byte8 = Integer.parseInt(str_byte8, 2);
			((List) list.get(number_byte4)).add(number_byte8);
		}
		int[] count = new int[2];
		for(int i = 0; i < list.size(); i++) {
			List list_byte4 = (List) list.get(i);
			if(list_byte4.size() > 1) {
				int count_byte4 = (list_byte4.size()*(list_byte4.size()-1))/2;
				count[0] += count_byte4;
				for(int first = 0; first < list_byte4.size(); first++) {
					for(int second = first+1; second < list_byte4.size(); second++) {
						int difference = (int) list_byte4.get(first) ^ (int) list_byte4.get(second);
						if(difference == 0) {
							count[1] ++;
						}
					}
				}
			}
		}
		
		return count;
		
	}
	
	public static String get4BitString(int i) {
		String str = Integer.toBinaryString(i);
		while(str.length() < 4) {
			str = "0" + str;
		}
		
		return str;
	}
	
	public static int[][] getCipherByDeltaSet(long number) {
		
		int plaintext_number = (int) Math.pow(2, 16);
		
		int[] position = new int[] {0, 5, 10, 15};
		
		int[][] delta = new int[plaintext_number][];
		int[][] cipher = new int[plaintext_number][];
		for(int i = 0; i < delta.length; i++) {
			delta[i] = getOne16ByteTextAfterPadding(number, i, position);
			int[] key = getOneRandomkey();
			cipher[i] = handle_format_ciphertext(encrypt(key, handle_format_plaintext(delta[i])));
		}
		
		return cipher;
		
	}
	
	public static int[] getOneRandomkey() {
		
		int[] key = new int[12];
		
		Random rd = new Random();
		for(int i = 0; i < key.length; i++) {
			key[i] = (rd.nextInt(256) & 0x0ff);
		}
		
		return key;
		
	}
	
	public static int[] handle_format_plaintext(int[] before) {
		
		int[] plaintext = new int[8];
		for(int i = 0; i < plaintext.length; i++) {
			plaintext[i] = (before[i*2] << 4) | before[i*2+1];
		}
		
		return plaintext;
		
	}
	
	public static int[] handle_format_ciphertext(int[] before) {
		
		int[] ciphertext = new int[16];
		for(int i = 0; i < before.length; i++) {
			ciphertext[i*2] = (before[i] >> 4) & 0xf;
			ciphertext[i*2+1] = before[i] & 0xf;
		}
		
		return ciphertext;
		
	}
	
	public static int[] getOne16ByteTextAfterPadding(long number, int num, int[] position) {
		
		String numberStr = Long.toHexString(number);
		while(numberStr.length() < 12) {
			numberStr = "0" + numberStr;
		}
		
		String numStr = Integer.toHexString(num);
		while(numStr.length() < 4) {
			numStr = "0" + numStr;
		}
		
		int[] text = new int[16];
		int j1 = 0;
		int j2 = 0;
		for(int i = 0; i < 16; i++) {
			if((i != position[0]) && (i != position[1]) && (i != position[2]) && (i != position[3])) {
				text[i] = Integer.parseInt(numberStr.substring(j1, j1+1), 16);
				j1++;
			} else {
				text[i] = Integer.parseInt(numStr.substring(j2, j2+1), 16);
				j2++;
			}
		}
		
		return text;
		
	}
	
	public static int[] encrypt(int[] key, int[] plaintext) {
		
		long[] subkeys = key_schedule(key);
        long[] state = new long[2];
	    for(int i = 0; i < 2; i++) {
	    	state[i] = ((plaintext[i*4] << 24) | (plaintext[i*4+1] << 16) | (plaintext[i*4+2] << 8) | (plaintext[i*4+3])) & 0xffffffffL; 
	    }
	    for(int i = 0; i < ROUNDS_T; i++) {
	    	state = roundFunction(state[0], state[1], subkeys[i]);
	    }
	    
	    int[] ciphertext = new int[8];
	    for(int i = 0; i < state.length; i++) {
	    	ciphertext[i*4] = (int)((state[i] >> 24) & 0xff);
	    	ciphertext[i*4+1] = (int)((state[i] >> 16) & 0xff);
	    	ciphertext[i*4+2] = (int)((state[i] >> 8) & 0xff);
	    	ciphertext[i*4+3] = (int)((state[i]) & 0xff);
	    }
	    
	    return ciphertext;
		
	}
	
	
	public static long[] roundFunction(long left, long right, long round_key) {
		
		left = (rot_right(left, ROT_ALPHA) + right ^ round_key) & 0xffffffffL;
		right = (rot_left(right, ROT_BETA) ^ left) & 0xffffffffL;
		
		long[] state = new long[] {left, right};
		
		return state;
		
	}
	
	public static long[] key_schedule(int[] masterkey) {
		
		int[] key = new int[12];
	    for(int i = 0; i < key.length/4; i++) {
	    	key[i] = ((int)masterkey[i*4] << 24) | ((int)masterkey[i*4+1] << 16) | ((int)masterkey[i*4+2] << 8) | ((int)masterkey[i*4+3]); 
	    }
		
		long lp0 = 0;
		long lp1 = 0;
		long lp2 = 0;
		
		long[] subkeys = new long[ROUNDS_T];
		
		subkeys[0] = key[2];
		
		for(int i = 0; i < ROUNDS_T-1 ; i++) {
			
			if(i == 0) {
				lp0 = key[1];
				lp1 = key[0];
			} else {
				lp0 = lp1;
				lp1 = lp2;
			}
			lp2 = ((rot_right(lp0, ROT_ALPHA) + subkeys[i]) ^ i) & 0xffffffffL;
			subkeys[i+1] = (rot_left(subkeys[i], ROT_BETA) ^ lp2) & 0xffffffffL;
			
		}
		
		return subkeys;
		
	}
	
	public static long rot_left(long x, int n) {
		
		long a = (x << n | x >> (32-n));
		
		return a & 0xffffffffL;
		
	}
	
	public static long rot_right(long x, int n) {
		
		long a = (x >> n | x << (32-n));
		return a & 0xffffffffL;
		
	}

}
