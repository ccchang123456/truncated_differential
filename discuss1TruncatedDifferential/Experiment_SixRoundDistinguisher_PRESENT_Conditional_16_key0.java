package discuss1TruncatedDifferential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//test new extended six-round distinguisher with present s-box: D1 is inactive in the condition that D0 is inactive
//we use 100 threads to test separately, take key0 as an example: key0 represents the 0th group of random key experiments using 2^16 structures
public class Experiment_SixRoundDistinguisher_PRESENT_Conditional_16_key0 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();    //获取开始时间
	
		int sboxnum = 1;
		int[] sbox = ALLTABLE[sboxnum];
		String tablename = ALLTABLENAME[sboxnum];

		
		int[][][][][] FOURTEXT = getFourByteTable(sbox);
		
		int keynum = 0;
		
		int[][] key = getRoundRandomKey();
		int[][] byte_positions = new int[][] {
			{0, 5, 10, 15, 1, 6, 11, 12},
			{1, 6, 11, 12, 2, 7, 8, 13},
			{2, 7, 8, 13, 3, 4, 9, 14},
			{3, 4, 9, 14, 0, 5, 10, 15}
		};
		
		long[][] allCount = new long[4][2];
		
		for(int i = 0; i < DELTASETSIZE; i++) {
			int[][] cipher = getCipherByDeltaSet(FOURTEXT, i, key, sbox);
			int[] count_0 = getZeroDifferentialByOneDeltaSetCipherText(cipher, byte_positions[0]);
			int[] count_1 = getZeroDifferentialByOneDeltaSetCipherText(cipher, byte_positions[1]);
			int[] count_2 = getZeroDifferentialByOneDeltaSetCipherText(cipher, byte_positions[2]);
			int[] count_3 = getZeroDifferentialByOneDeltaSetCipherText(cipher, byte_positions[3]);
			
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
		System.out.println(tablename);	
		System.out.println("key" + keynum + " : " + all[0] + " " + all[1]);	
		System.out.println(allCount[0][0] + " " + allCount[0][1] + " " + allCount[1][0] + " " + allCount[1][1] + " " + allCount[2][0] + " " + allCount[2][1] + " " + allCount[3][0] + " " + allCount[3][1]);
		
		long endTime = System.currentTimeMillis();    
		System.out.println("run time：" + (endTime - startTime) + "ms");    
		System.out.println("----------------------------"); 
		

		
		
	}
	
	public static int DELTASETSIZE = (int) Math.pow(2, 16);
	
	public static int[][] ALLTABLE = new int[][] {
		{6, 11, 5, 4, 2, 14, 7, 10, 9, 13, 15, 12, 3, 1, 0, 8},
		{12, 5, 6, 11, 9, 0, 10, 13, 3, 14, 15, 8, 4, 7, 1, 2},
		{1, 3, 6, 4, 2, 5, 9, 10, 0, 15, 7, 14, 12, 11, 13, 8},
		{1, 3, 6, 4, 2, 5, 10, 12, 0, 15, 7, 8, 14, 11, 13, 9},
		{6, 4, 12, 5, 0, 7, 2, 14, 1, 15, 3, 13, 8, 10, 9, 11}
	};
	public static String[] ALLTABLENAME = new String[] {
			"SMALLAESSBOX", "PRESENTSBOX", "TOY6SBOX", "TOY8SBOX", "TOY10SBOX"
	};
	
	public static int[][] getCipherByDeltaSet(int[][][][][] FOURTEXT, long number, int[][] key, int[] sbox) {
		
		int plaintext_number = (int) Math.pow(2, 16);
		
		int[] position = new int[] {0, 5, 10, 15};
		
		int[][] delta = new int[plaintext_number][];
		int[][] cipher = new int[plaintext_number][];
		for(int i = 0; i < delta.length; i++) {
			delta[i] = getOne16ByteTextAfterPadding(number, i, position);
			cipher[i] = encrypt(FOURTEXT, delta[i], key, sbox);
		}
		
		return cipher;
		
	}
	
	public static int[][] getCipherByMiddleState(int[][][][][] FOURTEXT, int[][] plaintext, int[][] key, int[] sbox, int times) {
		
		int plaintext_number = (int) Math.pow(2, 16);
		int[][] cipher = new int[plaintext_number][];
		for(int i = 0; i < cipher.length; i++) {
			cipher[i] = plaintext[i];
			for(int t = 0; t < times; t++) {
				cipher[i] = encryptOneRound(FOURTEXT, cipher[i], key[t], sbox);
			}
		}
		
		return cipher;
		
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
	
	public static int[][] getRoundRandomKey() {
		
		int[][] key = new int[4][16];
		
		Random rd = new Random();
		for(int i = 0; i < key.length; i++) {
			for(int j = 0; j < key[0].length; j++) {
				key[i][j] = rd.nextInt(16);
			}
		}
		
		return key;
	}
	
	public static int[][][][][] getFourByteTable(int[] sbox) {
		
		int[][][][][] fourthtext = new int[16][16][16][16][];
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				for(int k = 0; k < 16; k++) {
					for(int l = 0; l < 16; l++) {
						int[] text = new int[] {i, j, k, l};
						int[] result = encryptOneRound(text, sbox);
						fourthtext[i][j][k][l] = result;
					}
				}
			}
		}

		return fourthtext;
		
	}
	
	public static int[] encryptOneRound(int[] text, int[] sbox) {
		
		int[] result = new int[text.length];
		result = nibbleSub(text, sbox);
//		result = shiftRowByPermutation(result, position);
		result = mixColumn(result);
		
		return result;
	}
	
	public static int[] getZeroDifferentialByOneDeltaSetCipherText(int[][] ciphertext, int[] byte_positions) {		
		
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
	
	public static int[] encrypt(int[][][][][] FOURTEXT, int[] plaintext, int[][] key, int[] sbox) {
		
		int[] ciphertext = plaintext;
		
		for(int i = 0; i < key.length; i++) {
			ciphertext = addKey(ciphertext, key[i]);
			int[] text1 = new int[] {ciphertext[0], ciphertext[5], ciphertext[10], ciphertext[15]};
			int[] text2 = new int[] {ciphertext[4], ciphertext[9], ciphertext[14], ciphertext[3]};
			int[] text3 = new int[] {ciphertext[8], ciphertext[13], ciphertext[2], ciphertext[7]};
			int[] text4 = new int[] {ciphertext[12], ciphertext[1], ciphertext[6], ciphertext[11]};
			
			text1 = FOURTEXT[text1[0]][text1[1]][text1[2]][text1[3]];
			text2 = FOURTEXT[text2[0]][text2[1]][text2[2]][text2[3]];
			text3 = FOURTEXT[text3[0]][text3[1]][text3[2]][text3[3]];
			text4 = FOURTEXT[text4[0]][text4[1]][text4[2]][text4[3]];
			
			for(int j = 0; j < text1.length; j++) {
				ciphertext[j] = text1[j];
			}
			for(int j = 0; j < text2.length; j++) {
				ciphertext[j+4] = text2[j];
			}
			for(int j = 0; j < text3.length; j++) {
				ciphertext[j+8] = text3[j];
			}
			for(int j = 0; j < text4.length; j++) {
				ciphertext[j+12] = text4[j];
			}
		}
		
		return ciphertext;
		
	}
	
	public static int[] encryptOneRound(int[][][][][] FOURTEXT, int[] plaintext, int[] key, int[] sbox) {
		
		int[] ciphertext = plaintext;

		ciphertext = addKey(ciphertext, key);
		int[] text1 = new int[] {ciphertext[0], ciphertext[5], ciphertext[10], ciphertext[15]};
		int[] text2 = new int[] {ciphertext[4], ciphertext[9], ciphertext[14], ciphertext[3]};
		int[] text3 = new int[] {ciphertext[8], ciphertext[13], ciphertext[2], ciphertext[7]};
		int[] text4 = new int[] {ciphertext[12], ciphertext[1], ciphertext[6], ciphertext[11]};
		
		text1 = FOURTEXT[text1[0]][text1[1]][text1[2]][text1[3]];
		text2 = FOURTEXT[text2[0]][text2[1]][text2[2]][text2[3]];
		text3 = FOURTEXT[text3[0]][text3[1]][text3[2]][text3[3]];
		text4 = FOURTEXT[text4[0]][text4[1]][text4[2]][text4[3]];
		
		for(int j = 0; j < text1.length; j++) {
			ciphertext[j] = text1[j];
		}
		for(int j = 0; j < text2.length; j++) {
			ciphertext[j+4] = text2[j];
		}
		for(int j = 0; j < text3.length; j++) {
			ciphertext[j+8] = text3[j];
		}
		for(int j = 0; j < text4.length; j++) {
			ciphertext[j+12] = text4[j];
		}
	
		
		return ciphertext;
		
	}
	
	public static int[] nibbleSub(int[] before, int[] sbox) {
		
		int[] result = new int[before.length];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = sbox[before[i]];
		}
		
		return result;
	}
	
	public static int[] mixColumn(int[] text) {
		int[] result = new int [text.length];
		int[] row1 = new int[] {2, 3, 1, 1};
		int[] row2 = new int[] {1, 2, 3, 1};
		int[] row3 = new int[] {1, 1, 2, 3};
		int[] row4 = new int[] {3, 1, 1, 2};
		int[] col1 = new int[] {text[0], text[1], text[2], text[3]};
		
		result[0] = countMulti(row1[0], col1[0]) ^ countMulti(row1[1], col1[1]) ^ countMulti(row1[2], col1[2]) ^ countMulti(row1[3], col1[3]);
		result[1] = countMulti(row2[0], col1[0]) ^ countMulti(row2[1], col1[1]) ^ countMulti(row2[2], col1[2]) ^ countMulti(row2[3], col1[3]);
		result[2] = countMulti(row3[0], col1[0]) ^ countMulti(row3[1], col1[1]) ^ countMulti(row3[2], col1[2]) ^ countMulti(row3[3], col1[3]);
		result[3] = countMulti(row4[0], col1[0]) ^ countMulti(row4[1], col1[1]) ^ countMulti(row4[2], col1[2]) ^ countMulti(row4[3], col1[3]);
		
		return result;
		
	}
	
	public static int[] addKey(int[] text, int[] key) {
		
		int[] result = new int[text.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = text[i] ^ key[i];
		}
		
		return result;
	}
	
	public static int countMulti(int c1, int c2) {
		
		String result = "00";
		String poly = "0011";
		
		String result1 = get4BitString(c2); 
		
		if (c1 == 2) {
			if(result1.substring(0, 1).equals("1")) {
				result1 = result1.substring(1, 4) + "0";
				result1 = addKeyBy2Bit(poly, result1);
			} else {
				result1 = result1.substring(1, 4) + "0";
			}
			
		} else if(c1 == 3) {
			String initialResult = result1;
			if(result1.substring(0, 1).equals("1")) {
				result1 = result1.substring(1, 4) + "0";
				result1 = addKeyBy2Bit(poly, result1);
			} else {
				result1 = result1.substring(1, 4) + "0";
			}
			result1 = addKeyBy2Bit(result1, initialResult);
		}
		
		result = addKeyBy2Bit(get4BitString(Integer.parseInt(result, 16)), result1);
		
		return bitToBytes(result);
	}
	
	public static String get4BitString(int i) {
		String str = Integer.toBinaryString(i);
		while(str.length() < 4) {
			str = "0" + str;
		}
		
		return str;
	}
	
	public static String addKeyBy2Bit(String key, String text) {
		
		int length = key.length();
		
		char[] key1 = key.toCharArray();
		char[] text1 = text.toCharArray();
		
		int[] result = new int[length];
				
		StringBuffer buffer = new StringBuffer();
		
		for(int i = 0; i < length; i++) {
			int a1 = key1[i] - '0';
			int a2 = text1[i] - '0';
			result[i] =  a1 ^ a2;
			
			buffer.append(Integer.toString(result[i]));
		}
		
		return buffer.toString();
	}
	
	public static int bitToBytes(String text) {
				
		int a = Integer.parseInt(text, 2);
		
		return a;
		
	}
	
}
