package discuss1TruncatedDifferential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// test new four-round distinguisher on AES: byte 1 is inactive in the condition that byte 0 is inactive
// we use 100 threads to test separately, take key0 as an example: key0 represents the 0th group of random key experiments using 2^24 δ-sets
public class Experiment_FourRoundDistinguisherForAES_Conditional_24_key0 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis(); 
		
		int[][] COUNTMULTITABLE = getAfterCountMultiTable();
		
		int head = 1;
		
		int keynum = 0;
		
		int[][] key = getRoundRandomKey();
		
		int[][] byte_positions = new int[][] {
			{0, 1}, {5, 6}, {10, 11}, {15, 12}
		};
		
		long[] allCount = new long[2];
		
		for(int i = 0; i < DELTASETSIZE; i++) {
			int[][] cipher = getCipherByDeltaSet(i, head, key, AESSBOX, COUNTMULTITABLE);
			int[] count = getZeroDifferentialByOneDeltaSetCipherText(cipher, byte_positions[0]);
			
			allCount[0] += count[0];
			allCount[1] += count[1];
		}
		
		System.out.println("----------------------------");
		System.out.println("AES");	
		System.out.println("key" + keynum + " : " + allCount[0] + " " + allCount[1]);	
		long endTime = System.currentTimeMillis();    
		System.out.println("run time：" + (endTime - startTime) + "ms");    
		System.out.println("----------------------------");
		
	}
	
	public static int DELTASETSIZE = (int) Math.pow(2, 24);
	
	public static int[] AESSBOX = new int[] {99, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215, 171, 118, 202, 130, 201, 125, 250, 89, 71, 240, 173, 212, 162, 175, 156, 164, 114, 192, 183, 253, 147, 38, 54, 63, 247, 204, 52, 165, 229, 241, 113, 216, 49, 21, 4, 199, 35, 195, 24, 150, 5, 154, 7, 18, 128, 226, 235, 39, 178, 117, 9, 131, 44, 26, 27, 110, 90, 160, 82, 59, 214, 179, 41, 227, 47, 132, 83, 209, 0, 237, 32, 252, 177, 91, 106, 203, 190, 57, 74, 76, 88, 207, 208, 239, 170, 251, 67, 77, 51, 133, 69, 249, 2, 127, 80, 60, 159, 168, 81, 163, 64, 143, 146, 157, 56, 245, 188, 182, 218, 33, 16, 255, 243, 210, 205, 12, 19, 236, 95, 151, 68, 23, 196, 167, 126, 61, 100, 93, 25, 115, 96, 129, 79, 220, 34, 42, 144, 136, 70, 238, 184, 20, 222, 94, 11, 219, 224, 50, 58, 10, 73, 6, 36, 92, 194, 211, 172, 98, 145, 149, 228, 121, 231, 200, 55, 109, 141, 213, 78, 169, 108, 86, 244, 234, 101, 122, 174, 8, 186, 120, 37, 46, 28, 166, 180, 198, 232, 221, 116, 31, 75, 189, 139, 138, 112, 62, 181, 102, 72, 3, 246, 14, 97, 53, 87, 185, 134, 193, 29, 158, 225, 248, 152, 17, 105, 217, 142, 148, 155, 30, 135, 233, 206, 85, 40, 223, 140, 161, 137, 13, 191, 230, 66, 104, 65, 153, 45, 15, 176, 84, 187, 22};

	public static int[] SR = new int[] {0, 5, 10, 15, 4, 9, 14, 3, 8, 13, 2, 7, 12, 1,  6, 11};
	
	public static int[][] getAfterCountMultiTable() {
		
		int[][] table = new int[4][256];
		for(int c = 1; c < table.length; c++) {
			for(int number = 0; number < table[0].length; number++) {
				table[c][number] = countMulti(c, number);
			}
		}
		
		return table;
		
	}
	
	public static int[][] getOneDeltaSet(int number, int head) {
		
		int[][] delta = new int[256][];
		for(int i = 0; i < delta.length; i++) {
			delta[i] = getOne16ByteTextAfterPadding(number, i, head);
		}
		
		return delta;
		
	}
	
	public static int[][] getCipherByDeltaSet(int number, int head, int[][] key, int[] sbox, int[][] COUNTMULTITABLE) {
		
		int[][] delta = new int[256][];
		int[][] cipher = new int[256][];
		for(int i = 0; i < delta.length; i++) {
			delta[i] = getOne16ByteTextAfterPadding(number, i, head);
			cipher[i] = encrypt(delta[i], key, sbox, COUNTMULTITABLE);
		}
		
		return cipher;
		
	}
	
	public static int[] getOne16ByteTextAfterPadding(int number, int num, int position) {
		
		String numStr = Long.toHexString(number);
		
		while(numStr.length() < 30) {
			numStr = "0" + numStr;
		}
		
		int[] text = new int[16];
		int j = 0;
		for(int i = 0; i < text.length; i++) {
			if(i != position) {
				text[i] = Integer.parseInt(numStr.substring(j, j+2), 16);
				j += 2;
			} else {
				text[i] = num;
			}
		}
		
		return text;
		
	}
	
	public static int[][] getRoundRandomKey() {
		
		int[][] key = new int[3][16];
		
		Random rd = new Random();
		for(int i = 0; i < key.length; i++) {
			for(int j = 0; j < key[0].length; j++) {
				key[i][j] = rd.nextInt(256);
			}
		}
		
		return key;
	}
	
	public static int[] getZeroDifferentialByOneDeltaSetCipherText(int[][] ciphertext) {
		
		int[] count = new int[2];
		for(int i = 0; i < ciphertext.length-1; i++) {
			for(int j = i+1; j < ciphertext.length; j++) {
				
				//统计一次0的个数
				int num0 = ciphertext[i][0] ^ ciphertext[j][0];
				int num1 = ciphertext[i][1] ^ ciphertext[j][1];
				
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
	
	public static int[] getZeroDifferentialByOneDeltaSetCipherText(int[][] ciphertext, int[] byte_positions) {		
		
		int number = (int) Math.pow(2, 8);
		List list = new ArrayList<>();
		for(int i = 0; i < number; i++) {
			List list_initial = new ArrayList<>();
			list.add(list_initial);
		}
		for(int i = 0; i < ciphertext.length; i++) {
			String str_byte1 = get8BitString(ciphertext[i][byte_positions[0]]);
			String str_byte2 = get8BitString(ciphertext[i][byte_positions[1]]);
			int number_byte1 = Integer.parseInt(str_byte1, 2);
			int number_byte2 = Integer.parseInt(str_byte2, 2);
			((List) list.get(number_byte1)).add(number_byte2);
		}
		int[] count = new int[2];
		for(int i = 0; i < list.size(); i++) {
			List list_byte1 = (List) list.get(i);
			if(list_byte1.size() > 1) {
				int count_byte1 = (list_byte1.size()*(list_byte1.size()-1))/2;
				count[0] += count_byte1;
				for(int first = 0; first < list_byte1.size(); first++) {
					for(int second = first+1; second < list_byte1.size(); second++) {
						int difference = (int) list_byte1.get(first) ^ (int) list_byte1.get(second);
						if(difference == 0) {
							count[1] ++;
						}
					}
				}
			}
		}
		
		return count;
		
	}
	
	public static int[] encrypt(int[] plaintext, int[][] key, int[] sbox, int[][] COUNTMULTITABLE) {
		
		int[] ciphertext = plaintext;
		
		for(int i = 0; i < key.length; i++) {
			ciphertext = addKey(ciphertext, key[i]);
			ciphertext = sub(ciphertext, sbox);
			ciphertext = shiftRowByPermutation(ciphertext);
			ciphertext = mixColumn(ciphertext, COUNTMULTITABLE);
		}
		
		return ciphertext;
		
	}
	
	public static int[] sub(int[] before, int[] sbox) {
		
		int[] result = new int[before.length];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = sbox[before[i]];
		}
		
		return result;
	}
	
	public static int[] shiftRowByPermutation(int[] text) {
		
		int[] result = new int[text.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = text[SR[i]];
		}
		
		return result;
	}
	
	public static int[] mixColumn(int[] text, int[][] COUNTMULTITABLE) {
		
		int[] result = new int [text.length];
		
		int[] row1 = new int[] {2, 3, 1, 1};
		int[] row2 = new int[] {1, 2, 3, 1};
		int[] row3 = new int[] {1, 1, 2, 3};
		int[] row4 = new int[] {3, 1, 1, 2};
		int[] col1 = new int[] {text[0], text[1], text[2], text[3]};
		int[] col2 = new int[] {text[4], text[5], text[6], text[7]};
		int[] col3 = new int[] {text[8], text[9], text[10], text[11]};
		int[] col4 = new int[] {text[12], text[13], text[14], text[15]};
		
		result[0] = COUNTMULTITABLE[row1[0]][col1[0]] ^ COUNTMULTITABLE[row1[1]][col1[1]] ^ COUNTMULTITABLE[row1[2]][col1[2]] ^ COUNTMULTITABLE[row1[3]][col1[3]];
		result[4] = COUNTMULTITABLE[row1[0]][col2[0]] ^ COUNTMULTITABLE[row1[1]][col2[1]] ^ COUNTMULTITABLE[row1[2]][col2[2]] ^ COUNTMULTITABLE[row1[3]][col2[3]];
		result[8] = COUNTMULTITABLE[row1[0]][col3[0]] ^ COUNTMULTITABLE[row1[1]][col3[1]] ^ COUNTMULTITABLE[row1[2]][col3[2]] ^ COUNTMULTITABLE[row1[3]][col3[3]];
		result[12] = COUNTMULTITABLE[row1[0]][col4[0]] ^ COUNTMULTITABLE[row1[1]][col4[1]] ^ COUNTMULTITABLE[row1[2]][col4[2]] ^ COUNTMULTITABLE[row1[3]][col4[3]];
		result[1] = COUNTMULTITABLE[row2[0]][col1[0]] ^ COUNTMULTITABLE[row2[1]][col1[1]] ^ COUNTMULTITABLE[row2[2]][col1[2]] ^ COUNTMULTITABLE[row2[3]][col1[3]];
		result[5] = COUNTMULTITABLE[row2[0]][col2[0]] ^ COUNTMULTITABLE[row2[1]][col2[1]] ^ COUNTMULTITABLE[row2[2]][col2[2]] ^ COUNTMULTITABLE[row2[3]][col2[3]];
		result[9] = COUNTMULTITABLE[row2[0]][col3[0]] ^ COUNTMULTITABLE[row2[1]][col3[1]] ^ COUNTMULTITABLE[row2[2]][col3[2]] ^ COUNTMULTITABLE[row2[3]][col3[3]];
		result[13] = COUNTMULTITABLE[row2[0]][col4[0]] ^ COUNTMULTITABLE[row2[1]][col4[1]] ^ COUNTMULTITABLE[row2[2]][col4[2]] ^ COUNTMULTITABLE[row2[3]][col4[3]];
		result[2] = COUNTMULTITABLE[row3[0]][col1[0]] ^ COUNTMULTITABLE[row3[1]][col1[1]] ^ COUNTMULTITABLE[row3[2]][col1[2]] ^ COUNTMULTITABLE[row3[3]][col1[3]];
		result[6] = COUNTMULTITABLE[row3[0]][col2[0]] ^ COUNTMULTITABLE[row3[1]][col2[1]] ^ COUNTMULTITABLE[row3[2]][col2[2]] ^ COUNTMULTITABLE[row3[3]][col2[3]];
		result[10] = COUNTMULTITABLE[row3[0]][col3[0]] ^ COUNTMULTITABLE[row3[1]][col3[1]] ^ COUNTMULTITABLE[row3[2]][col3[2]] ^ COUNTMULTITABLE[row3[3]][col3[3]];
		result[14] = COUNTMULTITABLE[row3[0]][col4[0]] ^ COUNTMULTITABLE[row3[1]][col4[1]] ^ COUNTMULTITABLE[row3[2]][col4[2]] ^ COUNTMULTITABLE[row3[3]][col4[3]];
		result[3] = COUNTMULTITABLE[row4[0]][col1[0]] ^ COUNTMULTITABLE[row4[1]][col1[1]] ^ COUNTMULTITABLE[row4[2]][col1[2]] ^ COUNTMULTITABLE[row4[3]][col1[3]];
		result[7] = COUNTMULTITABLE[row4[0]][col2[0]] ^ COUNTMULTITABLE[row4[1]][col2[1]] ^ COUNTMULTITABLE[row4[2]][col2[2]] ^ COUNTMULTITABLE[row4[3]][col2[3]];
		result[11] = COUNTMULTITABLE[row4[0]][col3[0]] ^ COUNTMULTITABLE[row4[1]][col3[1]] ^ COUNTMULTITABLE[row4[2]][col3[2]] ^ COUNTMULTITABLE[row4[3]][col3[3]];
		result[15] = COUNTMULTITABLE[row4[0]][col4[0]] ^ COUNTMULTITABLE[row4[1]][col4[1]] ^ COUNTMULTITABLE[row4[2]][col4[2]] ^ COUNTMULTITABLE[row4[3]][col4[3]];
		
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
		String poly = "00011011";
		
		String result1 = get8BitString(c2); 
		
		if (c1 == 2) {
			if(result1.substring(0, 1).equals("1")) {
				result1 = result1.substring(1, 8) + "0";
				result1 = addKeyBy2Bit(poly, result1);
			} else {
				result1 = result1.substring(1, 8) + "0";
			}
			
		} else if(c1 == 3) {
			String initialResult = result1;
			if(result1.substring(0, 1).equals("1")) {
				result1 = result1.substring(1, 8) + "0";
				result1 = addKeyBy2Bit(poly, result1);
			} else {
				result1 = result1.substring(1, 8) + "0";
			}
			result1 = addKeyBy2Bit(result1, initialResult);
		}
		
		result = addKeyBy2Bit(get8BitString(Integer.parseInt(result, 16)), result1);
		
		return bitToBytes(result);
	}
	
	public static String get8BitString(int i) {
		String str = Integer.toBinaryString(i);
		while(str.length() < 8) {
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
