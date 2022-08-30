package discuss1TruncatedDifferential;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// get single probability
// one thread corresponding \Delta X_{2}^{SB}[12], take \Delta X_{2}^{SB}[12]=1 as an example
// calculate the probability of (\Delta X_{1}^{SB}[1] != 0 -> \Delta X_{4}[0]=0) by given \Delta X_{2}^{SB}[12]
// we use 255 threads to calcualte the p1[i] = Pr(\Delta X_{1}^{SB}[1] -> \Delta X_{4}[0]) for i = 1, ..., 255
// when multithreading finishes, use the function in Class Tool_ComputeResultForAES_4R to get the p[i] = Pr(\Delta X_{0}[1] -> \Delta X_{4}[0]) for i = 1, ..., 255
public class Differential_NewComputeDifferentialForAES_4R_Z1_0_A1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		int a = 1;
		

		int[] sbox = AESSBOX;
		int len = 8;
		
		int[][] COUNTMULTITABLE = getAfterCountMultiTable(len);
		int[][] secondDiffCombination = startPoint(a, COUNTMULTITABLE, len);
		int outputd = 0;		
		int[][] na = getOneSboxDifferential(sbox, COUNTMULTITABLE, 2, len);
		int[][] nb = getOneSboxDifferential(sbox, COUNTMULTITABLE, 3, len);
		long[][][] nab = getTwoSboxDifferential(na, nb, len);
		int[][] nc = getOneSboxDifferential(sbox, COUNTMULTITABLE, 1, len);
		int[][] nd = nc;
		long[][][] ncd = getTwoSboxDifferential(nc, nd, len);
		firstRoundAndMeetStartPoint(a, COUNTMULTITABLE, secondDiffCombination, generateTable(sbox), outputd, nab, ncd, len);
		
	}
	
	public static int[] SMALLAESSBOX = new int[] {6, 11, 5, 4, 2, 14, 7, 10, 9, 13, 15, 12, 3, 1, 0, 8};
	public static int[] AESSBOX = new int[] {99, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215, 171, 118, 202, 130, 201, 125, 250, 89, 71, 240, 173, 212, 162, 175, 156, 164, 114, 192, 183, 253, 147, 38, 54, 63, 247, 204, 52, 165, 229, 241, 113, 216, 49, 21, 4, 199, 35, 195, 24, 150, 5, 154, 7, 18, 128, 226, 235, 39, 178, 117, 9, 131, 44, 26, 27, 110, 90, 160, 82, 59, 214, 179, 41, 227, 47, 132, 83, 209, 0, 237, 32, 252, 177, 91, 106, 203, 190, 57, 74, 76, 88, 207, 208, 239, 170, 251, 67, 77, 51, 133, 69, 249, 2, 127, 80, 60, 159, 168, 81, 163, 64, 143, 146, 157, 56, 245, 188, 182, 218, 33, 16, 255, 243, 210, 205, 12, 19, 236, 95, 151, 68, 23, 196, 167, 126, 61, 100, 93, 25, 115, 96, 129, 79, 220, 34, 42, 144, 136, 70, 238, 184, 20, 222, 94, 11, 219, 224, 50, 58, 10, 73, 6, 36, 92, 194, 211, 172, 98, 145, 149, 228, 121, 231, 200, 55, 109, 141, 213, 78, 169, 108, 86, 244, 234, 101, 122, 174, 8, 186, 120, 37, 46, 28, 166, 180, 198, 232, 221, 116, 31, 75, 189, 139, 138, 112, 62, 181, 102, 72, 3, 246, 14, 97, 53, 87, 185, 134, 193, 29, 158, 225, 248, 152, 17, 105, 217, 142, 148, 155, 30, 135, 233, 206, 85, 40, 223, 140, 161, 137, 13, 191, 230, 66, 104, 65, 153, 45, 15, 176, 84, 187, 22};
	
	public static void firstRoundAndMeetStartPoint(int a, int[][] COUNTMULTITABLE, int[][] diffCombination, int[][] table, int outputd, long[][][] nab, long[][][] ncd, int len) throws IOException {
		
		int z = 1;
		int b = 0;
		
		int number = (int) Math.pow(2, len);
		
		for(int i = 1; i < number; i++) {
			
			long pcount = 0;
			
			int a1 = COUNTMULTITABLE[3][i];
			int b1 = COUNTMULTITABLE[2][i];
			int c1 = i;
			int d1 = i;
			int[] outa = table[a1];
			int pa = outa[a];
			if(pa == 0) {
				
			} else {
				int[] outb = table[b1];
				int[] outc = table[c1];
				int[] outd = outc;
				
				for(int sc = 0; sc < diffCombination.length; sc ++) {
					int[] scdiff = diffCombination[sc];
					int sca = scdiff[0];
					int scb = scdiff[1];
					int scc = scdiff[2];
					int scd = scdiff[3];
					int pabcd = pa * outb[scb] * outc[scc] * outd[scd];
					if(pabcd != 0) {
						int smca = scd;
						int smcb = COUNTMULTITABLE[3][scc];
						int smcc = scb;
						int smcd = COUNTMULTITABLE[3][sca];
						int[] thirdInputd = new int[] {smca, smcb, smcc, smcd};
						long pthird = getCollisionProbabilityByLookUpTable(thirdInputd, outputd, nab, ncd, len);
						long p = pabcd * pthird;
						pcount += p;
					}
				}
			}
			
			String name = "";
			if(len == 4) {
				name = "SMALLAES";
			} else if(len == 8) {
				name = "AES";
			}
			String fileName = "/result/result_TruncatedDifferential/" + name + "_4R/0/z" + z + "/byte/" + b + "/" + name + "-z" + z + "-byte" + b + "-" + i + "-" + a + ".txt";

		
	  		FileWriter fileWriter = new FileWriter(fileName);
	  	    PrintWriter printWriter = new PrintWriter(fileWriter);
	  	    printWriter.println(pcount);
	  	    printWriter.close();
		}
		
	}

	public static int[][] startPoint(int a, int[][] COUNTMULTITABLE, int len) {
		
		int dcSize = (int) Math.pow(2, len*3);
		int number = (int) Math.pow(2, len);
		
		int[][] diffCombination = new int[dcSize][];
		int count = 0;
		for(int b = 0; b < number; b++) {
			for(int c = 0; c < number; c++) {
				for(int d = 0; d < number; d++) {
					diffCombination[count] = new int[] {a, b, c, d};
					count ++;
				}
			}
		}
		
		return diffCombination;
		
	}
	
	public static int[][] getAfterCountMultiTable(int len) {
		
		int size = (int) Math.pow(2, len);
		
		int[][] table = new int[4][size];
		for(int c = 1; c < table.length; c++) {
			for(int number = 0; number < table[0].length; number++) {
				table[c][number] = countMulti(c, number, len);
			}
		}
		
		return table;
		
	}
	
	public static long getCollisionProbabilityByLookUpTable(int[] thirdInputd, int outputd, long[][][] nab, long[][][] ncd, int len) {

		long pcount  = 0;
		
		pcount += getThirdRoundDifferentialByApproximationTable_0(thirdInputd, outputd, nab, ncd);
		
		return pcount;
		
	}
	
	public static int[][] getOneSboxDifferential(int[] sbox, int[][] COUNTMULTITABLE, int c, int len) {
		
		int number = (int) Math.pow(2, len);
		
		int[][] na = new int[number][number];
		
		for(int a1 = 0; a1 < number; a1++) {
			for(int a2 = 0; a2 < number; a2++) {
				
				int ind = a1 ^ a2;
				
				int sa1 = sbox[a1];
				int sa2 = sbox[a2];
				
				int outd = COUNTMULTITABLE[c][sa1] ^ COUNTMULTITABLE[c][sa2];
				
				na[ind][outd]++;
				
			}
		}
		
		return na;
		
	}
	
	public static long[][][] getTwoSboxDifferential(int[][] na, int[][] nb, int len) {
		
		int number = (int) Math.pow(2, len);
		
		long[][][] nab = new long[number][number][number];
		int[] outdp = new int[number];
		
		for(int a = 0; a < na.length; a++) {
			int[] outa = na[a];
			
			for(int a1 = 0; a1 < outa.length; a1++) {
				
				for(int b = 0; b < nb.length; b++) {
					int[] outb = nb[b];
					
					for(int b1 = 0; b1 < outb.length; b1++) {
						
						int x = a1 ^ b1;
						int p = outa[a1] * outb[b1];
						nab[a][b][x] += p;
						outdp[x] += p;
						
					}
				}
			}
 		}
		
		return nab;
		
	}
	
	public static long getThirdRoundDifferentialByApproximationTable_0(int[] ipd, int outputd, long[][][] nab, long[][][] ncd) {
		 
		int a = ipd[0];
		int b = ipd[1];
		int c = ipd[2];
		int d = ipd[3];
		
		long op = 0;
		long[] outab = nab[a][b];
		long[] outcd = ncd[c][d];
		for(int i = 0; i < outab.length; i++) {
			if(outab[i] != 0) {
				int j = i ^ outputd;
				if(outcd[j] != 0) {
					long opab = outab[i];
					long opcd = outcd[j];
					op += opab * opcd;
				}
			}
		}	
		
		return op;
		
	}
	
	public static long[][][] getTwoSboxDifferential_00(int[][] table, int[][] COUNTMULTITABLE, int ca1, int cb1, int ca2, int cb2, int len) {
		
		int number = (int) Math.pow(2, len);
		int size = (int) Math.pow(2, len*2);
		
		long[][][] nab = new long[number][number][size];
		for(int a = 0; a < number; a++) {
			for(int b = 0; b < number; b++) {
				int[] at = table[a];
				for(int a1 = 0; a1 < at.length; a1++) {
					if(at[a1] != 0) {
						int[] bt = table[b];
						for(int b1 = 0; b1 < bt.length; b1++) {
							if(bt[b1] != 0) {
								int x1 = COUNTMULTITABLE[ca1][a1] ^ COUNTMULTITABLE[cb1][b1];
								int x2 = COUNTMULTITABLE[ca2][a1] ^ COUNTMULTITABLE[cb2][b1];
								int x12 = Integer.parseInt(getBitString(x1, len) + getBitString(x2, len), 2);
								int p = at[a1] * bt[b1];
								
								nab[a][b][x12] += p;
							}
						}
					}
				}
			}
		}
		
		return nab;
		
	}
	
	public static long getThirdRoundDifferentialByApproximationTable_00(int[] ipd, int outputd0, int outputd1, int[][][] nab, int[][][] ncd) {
		 
		int a = ipd[0];
		int b = ipd[1];
		int c = ipd[2];
		int d = ipd[3];
		
		long op = 0;
		int[] outab = nab[a][b];
		int[] outcd = ncd[c][d];
		for(int ab = 0; ab < outab.length; ab++) {
			for(int cd = 0; cd < outcd.length; cd++) {
				int opd = ab ^ cd;
				if(opd == outputd0) {
					op += outab[ab] * outcd[cd];
				}
			}
		}	
		
		return op;
		
	}
	
	public static String getBitString(int i, int len) {
		String str = Integer.toBinaryString(i);
		while(str.length() < len) {
			str = "0" + str;
		}
		
		return str;
	}
	
	public static int countMulti(int c1, int c2, int len) {
		
		String result = "00";
		String poly = "";
		if(len == 4) {
			//Small-AES
			poly = "0011";
		} else if (len == 8) {
			//AES
			poly = "00011011";
		}
		
		String result1 = getBitString(c2, len); 
		
		if (c1 == 2) {
			if(result1.substring(0, 1).equals("1")) {
				result1 = result1.substring(1, len) + "0";
				result1 = addKeyBy2Bit(poly, result1);
			} else {
				result1 = result1.substring(1, len) + "0";
			}
			
		} else if(c1 == 3) {
			String initialResult = result1;
			if(result1.substring(0, 1).equals("1")) {
				result1 = result1.substring(1, len) + "0";
				result1 = addKeyBy2Bit(poly, result1);
			} else {
				result1 = result1.substring(1, len) + "0";
			}
			result1 = addKeyBy2Bit(result1, initialResult);
		}
		
		result = addKeyBy2Bit(getBitString(Integer.parseInt(result, 16), len), result1);
		
		return bitToBytes(result);
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
	
	public static int[][] generateTable(int[] sbox) {
		
		int[][] table = new int[sbox.length][sbox.length];
		
		for (int i = 0; i < table.length; i++) {
			
			for (int j = 0; j < table[0].length; j++) {
				
				table[i][j] = 0;

				for (int x = 0; x < sbox.length; x++) {
					
					int x1 = x ^ i;
					int b1 = sbox[x] ^ sbox[x1];
					
					if(j == b1) {
						table[i][j]++;
					}
				}
			}
		}
		
		return table;
	}

}
