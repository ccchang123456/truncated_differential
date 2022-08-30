package discuss1TruncatedDifferential;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// calculate the 5-round and corresponding single output inactive byte of 4-round truncated differentials on Small-AES
public class Differential_NewComputeDifferentialForSMALLAES {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();    	

//		int sboxnum = 0;
//		int[] sbox = ALLTABLE[sboxnum];
//		int head = 1;
//		long[][] ph = new long[16][];
//		int input_position = 1;
//		for(int output_position = 0; output_position < 16; output_position++) {
//
//			//建表输出一个0字节
//			int[][] na = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][0]);
//			int[][] nb = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][1]);
//			long[][][] nab = getSmallTwoSboxDifferential(na, nb);
//			int[][] nc = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][2]);
//			int[][] nd = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][3]);
//			long[][][] ncd = getSmallTwoSboxDifferential(nc, nd);
//			ph[output_position] = getSmallFullDifferential_0(getDDT(sbox), nab, ncd, input_position, output_position);
//			
//		}
//		
//		long[] high = new long[] {ph[0][0], ph[0][1], 0};
//		for(int i = 0; i < ph.length; i++) {
//			if(high[0] < ph[i][0]) {
//				high[0] = ph[i][0];
//				high[1] = ph[i][1];
//				high[2] = i;
//			}
//			System.out.println("byteposition: " + i + " the highest porbability" + ph[i][0] + " input difference: " + ph[i][1]);
//		}
//		System.out.println();
//		System.out.println("the byte position: " + high[2] + " the highest probability" + high[0] + " input difference: " + high[1]);		

		int sboxnum = 4;
		int input_position = 3;
		int[][] output_diagonal = new int[][] {{0, 5, 10, 15}, {1, 6, 11, 12}, {2, 7, 8, 13}, {3, 4, 9, 14}};
		get_OneDiagonal(sboxnum, input_position, output_diagonal[3]);
//		get_TwoDiagonal(sboxnum, input_position, MC_C[0]);
		
		long endTime = System.currentTimeMillis();    

		System.out.println("run time: " + (endTime - startTime) + "ms");    


	}
	
	// the coefficient of MC operation when the positions of output bytes are different
	public static int[][] MC_OUTPUT = new int[][] {
		{2, 3, 1, 1},
		{1, 2, 3, 1},
		{1, 1, 2, 3},
		{3, 1, 1, 2},
		{2, 3, 1, 1},
		{1, 2, 3, 1},
		{1, 1, 2, 3},
		{3, 1, 1, 2},
		{2, 3, 1, 1},
		{1, 2, 3, 1},
		{1, 1, 2, 3},
		{3, 1, 1, 2},
		{2, 3, 1, 1},
		{1, 2, 3, 1},
		{1, 1, 2, 3},
		{3, 1, 1, 2}
	};
	
	public static int[][] ALLTABLE = new int[][] {
		{6, 11, 5, 4, 2, 14, 7, 10, 9, 13, 15, 12, 3, 1, 0, 8}, //Samll-AES
		{12, 5, 6, 11, 9, 0, 10, 13, 3, 14, 15, 8, 4, 7, 1, 2}, //Present
		{1, 3, 6, 4, 2, 5, 9, 10, 0, 15, 7, 14, 12, 11, 13, 8}, //Toy6
		{1, 3, 6, 4, 2, 5, 10, 12, 0, 15, 7, 8, 14, 11, 13, 9}, //Toy8
		{6, 4, 12, 5, 0, 7, 2, 14, 1, 15, 3, 13, 8, 10, 9, 11}  //Toy10
	};
	public static String[] ALLTABLENAME = new String[] {
			"SMALLAESSBOX", "PRESENTSBOX", "TOY6SBOX", "TOY8SBOX", "TOY10SBOX"
	};	
	public static int[][] MC = new int[][] {
			{2, 3, 1, 1},
			{1, 2, 3, 1},
			{1, 1, 2, 3},
			{3, 1, 1, 2}
	};
	
	public static int[][][][] MC_C = new int[][][][] {
		//0,1 and 5,6 and 10,11 and 15,12
		{{{2, 3 ,1, 2}, {1, 1, 3, 1}}, {{1, 2, 1, 1}, {3, 1, 2, 3}}, {{1, 1, 3, 1}, {2, 3, 1, 2}}, {{3, 1, 2, 3}, {1, 2, 1, 1}}},
		//1,2 and 6,7 and 11,8 and 12,13
		{{{1, 2, 1, 1}, {3, 1, 2, 3}}, {{1, 1, 3, 1}, {2, 3, 1, 2}}, {{3, 1, 2, 3}, {1, 2, 1, 1}}, {{2, 3, 1, 2}, {1, 1, 3, 1}}},
		//2,3 and 7,4 and 8,9 and 13,14
		{{{1, 1, 3 ,1}, {2, 3, 1, 2}}, {{3, 1, 2, 3}, {1, 2, 1, 1}}, {{2, 3, 1, 2}, {1, 1, 3, 1}}, {{1, 2, 1, 1}, {3, 1, 2, 3}}},
		//3,0 and 4,5 and 9,10 and 14,15
		{{{3, 1, 2, 3}, {1, 2, 1, 1}}, {{2, 3, 1, 2}, {1, 1, 3, 1}}, {{1, 2, 1, 1}, {3, 1, 2, 3}}, {{1, 1, 3, 1}, {2, 3, 1, 2}}}
	};
	
	// get the biggest bias and the highest probability, respectively, when input byte is 1 and output two inactive diagonals
	public static void get_TwoDiagonal(int sboxnum, int input_position, int[][][] mc) {
		
		System.out.println("two inactive diagonals in output");
		System.out.println("begin -------------------------------------------------------------------------------------------------------------");
		
		int[] sbox = ALLTABLE[sboxnum];
		String sboxname = ALLTABLENAME[sboxnum];
		
		long[][] ph = new long[4][];
		
		System.out.println("merge table " + "---------- " + 0);
		long[][][] nab0 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[0][0][0], mc[0][0][1], mc[0][0][2], mc[0][0][3], 4);
		long[][][] ncd0 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[0][1][0], mc[0][1][1], mc[0][1][2], mc[0][1][3], 4);
		ph[0] = getSmallFullDifferential_00(getDDT(sbox), nab0, ncd0, input_position, 0);
		
		System.out.println("merge table " + "---------- " + 1);
		long[][][] nab1 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[1][0][0], mc[1][0][1], mc[1][0][2], mc[1][0][3], 4);
		long[][][] ncd1 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[1][1][0], mc[1][1][1], mc[1][1][2], mc[1][1][3], 4);
		ph[1] = getSmallFullDifferential_00(getDDT(sbox), nab1, ncd1, input_position, 5);
		
		System.out.println("merge table " + "---------- " + 2);
		long[][][] nab2 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[2][0][0], mc[2][0][1], mc[2][0][2], mc[2][0][3], 4);
		long[][][] ncd2 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[2][1][0], mc[2][1][1], mc[2][1][2], mc[2][1][3], 4);
		ph[2] = getSmallFullDifferential_00(getDDT(sbox), nab2, ncd2, input_position, 10);
		
		System.out.println("merge table " + "---------- " + 3);
		long[][][] nab3 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[3][0][0], mc[3][0][1], mc[3][0][2], mc[3][0][3], 4);
		long[][][] ncd3 = getSmallTwoSboxDifferential_00(getDDT(sbox), mc[3][1][0], mc[3][1][1], mc[3][1][2], mc[3][1][3], 4);
		ph[3] = getSmallFullDifferential_00(getDDT(sbox), nab3, ncd3, input_position, 15);
		
		System.out.println();
		// record every corresponding probability of input difference when output two inactive diagonals
		// each value of array is the number of input difference
		BigDecimal[] p_diagonal_one = new BigDecimal[16];
		for(int ip = 1; ip < p_diagonal_one.length; ip++) {
			p_diagonal_one[ip] = new BigDecimal(1);
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[0][ip]));
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[1][ip]));
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[2][ip]));
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[3][ip]));
		}
		// compare the absolute value of bias
		BigDecimal p_diagonal_one_highest = p_diagonal_one[1];
		int inputd_diagonal_one_highest = 1;
		BigDecimal p_diagonal_one_average_all = p_diagonal_one[1];
		BigDecimal[] p_diagonal_one_average = new BigDecimal[] {BigDecimal.valueOf(ph[0][1]), BigDecimal.valueOf(ph[1][1]), BigDecimal.valueOf(ph[2][1]), BigDecimal.valueOf(ph[3][1])};
		for(int ip = 2; ip < p_diagonal_one.length; ip++) {
			int result = p_diagonal_one_highest.compareTo(p_diagonal_one[ip]);
			if(result > 0) {
				p_diagonal_one_highest = p_diagonal_one[ip];
				inputd_diagonal_one_highest = ip;
			}
			p_diagonal_one_average_all = p_diagonal_one_average_all.add(p_diagonal_one[ip]);
			p_diagonal_one_average[0] = p_diagonal_one_average[0].add(BigDecimal.valueOf(ph[0][ip]));
			p_diagonal_one_average[1] = p_diagonal_one_average[1].add(BigDecimal.valueOf(ph[1][ip]));
			p_diagonal_one_average[2] = p_diagonal_one_average[2].add(BigDecimal.valueOf(ph[2][ip]));
			p_diagonal_one_average[3] = p_diagonal_one_average[3].add(BigDecimal.valueOf(ph[3][ip]));
		}
		System.out.println("the biggest bias: " + p_diagonal_one_highest.divide(BigDecimal.valueOf(Math.pow(2, 36*4)), 30, BigDecimal.ROUND_HALF_UP) + " corresponding input difference: " + inputd_diagonal_one_highest);
		System.out.println("the first joint probability in colum 0: " + BigDecimal.valueOf(ph[0][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the second joint probability in column 1: " + BigDecimal.valueOf(ph[1][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the third joint probability in column 2: " + BigDecimal.valueOf(ph[2][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the forth joint probability in column 3: " + BigDecimal.valueOf(ph[3][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the average bias " + p_diagonal_one_average_all.divide(BigDecimal.valueOf(Math.pow(2, 36*4)*15), 30, BigDecimal.ROUND_HALF_UP));
		System.out.println("the first joint probability in column 0: " + p_diagonal_one_average[0].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the second joint probability in column 1: " + p_diagonal_one_average[1].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the third joint probability in column 2: " + p_diagonal_one_average[2].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the forth joint probability in column 3: " + p_diagonal_one_average[3].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		
		System.out.println();
		for(int ip = 1; ip < p_diagonal_one.length; ip++) {
			System.out.println("input difference " + ip + " probability " + p_diagonal_one[ip].divide(BigDecimal.valueOf(Math.pow(2, 36*4)), 30, BigDecimal.ROUND_HALF_UP));
			System.out.println("the first joint probability in column 0: " + BigDecimal.valueOf(ph[0][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println("the second joint probability in column 1: " + BigDecimal.valueOf(ph[1][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println("the third joint probability in column 2: " + BigDecimal.valueOf(ph[2][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println("the forth joint probability in column 3: " + BigDecimal.valueOf(ph[3][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println();
		}
		
		System.out.println("------------------------------------------------------------------------------------------------------------- end");
		System.out.println();
		
	}
	
	// get the highest probability  when input byte is 1 and output one inactive diagonal
	public static void get_OneDiagonal(int sboxnum, int input_position, int[] output_diagonal) {
		
		System.out.println("one inactive diagonal in output");
		System.out.println("begin -------------------------------------------------------------------------------------------------------------");
		
		int[] sbox = ALLTABLE[sboxnum];
		String sboxname = ALLTABLENAME[sboxnum];

		long[][] ph = new long[16][];
		for(int output_position = 0; output_position < output_diagonal.length; output_position++) {

			int[][] na = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_diagonal[output_position]][0]);
			int[][] nb = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_diagonal[output_position]][1]);
			long[][][] nab = getSmallTwoSboxDifferential(na, nb);
			int[][] nc = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_diagonal[output_position]][2]);
			int[][] nd = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_diagonal[output_position]][3]);
			long[][][] ncd = getSmallTwoSboxDifferential(nc, nd);
			ph[output_diagonal[output_position]] = getSmallFullDifferential_0(getDDT(sbox), nab, ncd, input_position, output_diagonal[output_position]);
			
		}
		
		// record every corresponding probability of input difference when output one inactive diagonal
		// each value of array is the number of input difference
		BigDecimal[] p_diagonal_one = new BigDecimal[16];
		for(int ip = 1; ip < p_diagonal_one.length; ip++) {
			p_diagonal_one[ip] = new BigDecimal(1);
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[output_diagonal[0]][ip]));
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[output_diagonal[1]][ip]));
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[output_diagonal[2]][ip]));
			p_diagonal_one[ip] = p_diagonal_one[ip].multiply(BigDecimal.valueOf(ph[output_diagonal[3]][ip]));
		}
		// compare probability
		BigDecimal p_diagonal_one_highest = p_diagonal_one[1];
		int inputd_diagonal_one_highest = 1;
		BigDecimal p_diagonal_one_average_all = p_diagonal_one[1];
		BigDecimal[] p_diagonal_one_average = new BigDecimal[] {BigDecimal.valueOf(ph[output_diagonal[0]][1]), BigDecimal.valueOf(ph[output_diagonal[1]][1]), BigDecimal.valueOf(ph[output_diagonal[2]][1]), BigDecimal.valueOf(ph[output_diagonal[3]][1])};
		for(int ip = 2; ip < p_diagonal_one.length; ip++) {
			int result = p_diagonal_one_highest.compareTo(p_diagonal_one[ip]);
			if(result < 0) {
				p_diagonal_one_highest = p_diagonal_one[ip];
				inputd_diagonal_one_highest = ip;
			}
			p_diagonal_one_average_all = p_diagonal_one_average_all.add(p_diagonal_one[ip]);
			p_diagonal_one_average[0] = p_diagonal_one_average[0].add(BigDecimal.valueOf(ph[output_diagonal[0]][ip]));
			p_diagonal_one_average[1] = p_diagonal_one_average[1].add(BigDecimal.valueOf(ph[output_diagonal[1]][ip]));
			p_diagonal_one_average[2] = p_diagonal_one_average[2].add(BigDecimal.valueOf(ph[output_diagonal[2]][ip]));
			p_diagonal_one_average[3] = p_diagonal_one_average[3].add(BigDecimal.valueOf(ph[output_diagonal[3]][ip]));
		}
		System.out.println("the highest probability: " + p_diagonal_one_highest.divide(BigDecimal.valueOf(Math.pow(2, 36*4)), 30, BigDecimal.ROUND_HALF_UP) + " corresponding input difference: " + inputd_diagonal_one_highest);
		System.out.println("output byte " + output_diagonal[0] + "：" + BigDecimal.valueOf(ph[output_diagonal[0]][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[1] + "：" + BigDecimal.valueOf(ph[output_diagonal[1]][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[2] + "：" + BigDecimal.valueOf(ph[output_diagonal[2]][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[3] + "：" + BigDecimal.valueOf(ph[output_diagonal[3]][inputd_diagonal_one_highest]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("the average probability: " + p_diagonal_one_average_all.divide(BigDecimal.valueOf(Math.pow(2, 36*4)*15), 30, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[0] + "：" + p_diagonal_one_average[0].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[1] + "：" + p_diagonal_one_average[1].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[2] + "：" + p_diagonal_one_average[2].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		System.out.println("output byte " + output_diagonal[3] + "：" + p_diagonal_one_average[3].divide(BigDecimal.valueOf(Math.pow(2, 36)*15), 15, BigDecimal.ROUND_HALF_UP));
		 
		System.out.println();
		for(int ip = 1; ip < p_diagonal_one.length; ip++) {
			System.out.println("input difference； " + ip + " the probability of output one inactive diagonal: " + p_diagonal_one[ip].divide(BigDecimal.valueOf(Math.pow(2, 36*4)), 30, BigDecimal.ROUND_HALF_UP));
			System.out.println("output byte " + output_diagonal[0] + "：" + BigDecimal.valueOf(ph[output_diagonal[0]][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println("output byte " + output_diagonal[1] + "：" + BigDecimal.valueOf(ph[output_diagonal[1]][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println("output byte " + output_diagonal[2] + "：" + BigDecimal.valueOf(ph[output_diagonal[2]][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
			System.out.println("output byte " + output_diagonal[3] + "：" + BigDecimal.valueOf(ph[output_diagonal[3]][ip]).divide(BigDecimal.valueOf(Math.pow(2, 36)), 15, BigDecimal.ROUND_HALF_UP));
		}
		
		System.out.println("------------------------------------------------------------------------------------------------------------- end");
		System.out.println();
		System.out.println();
		
	}
	
	// related operations of different combinations of input position and output position
	public static void get_AllInput_AllOutput() throws IOException {
		
		for(int sboxnum = 0; sboxnum < 5; sboxnum++) {		
			int[] sbox = ALLTABLE[sboxnum];
			String sboxname = ALLTABLENAME[sboxnum];
			
			long[][][] ph = new long[16][16][];
			for(int input_position = 0; input_position < 16; input_position++) {
				for(int output_position = 0; output_position < 16; output_position++) {
	
					int[][] na = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][0]);
					int[][] nb = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][1]);
					long[][][] nab = getSmallTwoSboxDifferential(na, nb);
					int[][] nc = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][2]);
					int[][] nd = getSmallOneSboxDifferential(sbox, MC_OUTPUT[output_position][3]);
					long[][][] ncd = getSmallTwoSboxDifferential(nc, nd);
					ph[input_position][output_position] = getSmallFullDifferential_0(getDDT(sbox), nab, ncd, input_position, output_position);
					
				}
			}
			System.out.println();
			String fileName = "/discuss/result_TruncatedDifferential/test all position_" + sboxname + ".txt";
			getOneHighestPosition(ph, fileName);
			getAverageHighestPosition(ph, fileName);
		}
		
	}
	
	// get the single input and output position of the highest probability
	public static void getOneHighestPosition(long[][][] ph, String fileName) throws IOException {
		
		long[][][] all_highest = new long[16][16][2];
		for(int input_position = 0; input_position < 16; input_position++) {
			for(int output_position = 0; output_position < 16; output_position++) {
				long[] p = ph[input_position][output_position]; 
				long p_highest = p[1];
				int inputd_highest = 1;
				for(int ipd = 2; ipd < p.length; ipd++) {
					if(p_highest < p[ipd]) {
						p_highest = p[ipd];
						inputd_highest = ipd;
					}
				}
				all_highest[input_position][output_position][0] = p_highest;
				all_highest[input_position][output_position][1] = inputd_highest;
				System.out.println("input position " + input_position + " output position " + output_position + " : the highest probability " + p_highest + " corresponding input difference " + inputd_highest);
			}
		}
		System.out.println();
		
		// the total highest
		long all_p_highest = all_highest[0][0][0];
		long all_inputd_highest = all_highest[0][0][1];
		int input_position_highest = 0;
		int output_position_highest = 0;
		for(int ip = 0; ip < all_highest.length; ip++) {
			for(int op = 0; op < all_highest[0].length; op++) {
				if(all_p_highest < all_highest[ip][op][0]) {
					all_p_highest = all_highest[ip][op][0];
					all_inputd_highest = all_highest[ip][op][1];
					input_position_highest = ip;
					output_position_highest = op;
				}
			}
		}
		System.out.println("the total highest probability " + all_p_highest + " : input " + input_position_highest + " output " + output_position_highest + " input difference " + all_inputd_highest);
		
		
	}
	
	// get the average input and output position of the highest probability
	public static void getAverageHighestPosition(long[][][] ph, String fileName) throws IOException {
		
  	    System.out.println();
		
		// record all position
		long[][] all_average = new long[16][16];
		for(int input_position = 0; input_position < 16; input_position++) {
			for(int output_position = 0; output_position < 16; output_position++) {
				long[] p = ph[input_position][output_position]; 
				for(int ipd = 1; ipd < p.length; ipd++) {
					all_average[input_position][output_position] += p[ipd];
				}
				System.out.println("input " + input_position + " output " + output_position + " : the average probability " + all_average[input_position][output_position]);
			}
		}
		System.out.println();
		
		// the average highest
		long average_p_highest = all_average[0][0];
		int average_input_position = 0;
		int average_output_position = 0;
		for(int ip = 0; ip < all_average.length; ip++) {
			for(int op = 0; op < all_average[0].length; op++) {
				if(average_p_highest < all_average[ip][op]) {
					average_p_highest = all_average[ip][op];
					average_input_position = ip;
					average_output_position = op;
				}
			}
		}
		System.out.println("the average highest probability " + average_p_highest + " : input " + average_input_position + " output " + average_output_position);
		
		
	}
	
	// get the 5-round highest probability of one diagonal
	public static void getDiagonal_5r(long[][] ph  ) {
		
		BigDecimal[] diagonal = new BigDecimal[16];
		BigDecimal pde = new BigDecimal(1);
		pde = pde.multiply(BigDecimal.valueOf(Math.pow(2, 32*4*4)));
		for(int i = 1; i < ph.length; i++) {
			BigDecimal pmul = new BigDecimal(1);
			for(int j = 0; j < 16; j++) {
				pmul = pmul.multiply(BigDecimal.valueOf(ph[j][i]));
			}
			diagonal[i] = pmul;
		}
		// compare
		BigDecimal pmax = diagonal[1];
		int ipd_0 = 1;
		for(int i = 2; i < diagonal.length; i++) {
			int result = pmax.compareTo(diagonal[i]);
			if(result < 0) {
				pmax = diagonal[i];
				ipd_0 = i;
			}
		}
		System.out.println("corresponding input difference: " + ipd_0);
		System.out.println("the highest probability " + pmax + " = 2-64 + " + pmax.subtract(pde));
		int[][] zall = new int[][] {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
		for(int z = 0; z < 4; z++) {
			System.out.println("the number of the which diagonal: " + z);
			System.out.println(ph[zall[z][0]][ipd_0] + " " + ph[zall[z][1]][ipd_0] + " " + ph[zall[z][2]][ipd_0] + " " + ph[zall[z][3]][ipd_0]);
		}
		
	}
	
	public static long[] getSmallFullDifferential_0(int[][] table, long[][][] nab, long[][][] ncd, int input_position, int output_position) {
		
		long[] pcount = new long[16];
		
		long[] r3p = new long[16];
		
		for(int i = 0; i < 16; i++) {
			int[] ocountput = table[i];
			
			for(int ocp = 0; ocp < ocountput.length; ocp++) {
				if(ocountput[ocp] != 0) {
					int[] opd = new int[2];
					
					opd[0] = ocp;
					opd[1] = ocountput[ocp];
					
					int ps1 = opd[1];
					
					int[] mcd = getSmallFirstRoundOutputDifferential(opd, input_position);
					
					List secondopd = getSmallSecondRoundDifferential(mcd, table, input_position, output_position);
					
					for(int sop = 0; sop < secondopd.size(); sop++) {
						int[] thirdipd = (int[]) secondopd.get(sop);
						int ps2 = thirdipd[thirdipd.length-1];
						
						int outputd = 0;
						long[] thirdopd = getSmallThirdRoundDifferentialByApproximationTable_0(thirdipd, outputd, nab, ncd);
						long ps3 = thirdopd[thirdopd.length-1];
						
						r3p[i] += ps3;
						
						pcount[i] += ps1 * ps2 * ps3;
					}
					
				}
			}
			
//			System.out.println("input difference: " + i + " total probability: " + pcount[i]);
		}
		
//		System.out.println();
		
		return pcount;
		
	}
	
	public static long[] getSmallFullDifferential_00(int[][] table, long[][][] nab, long[][][] ncd, int input_position, int output_position) {
		
		long[] pcount = new long[16];
		
		long[] r3p = new long[16];
		
		for(int i = 0; i < 16; i++) {
			int[] ocountput = table[i];
			
			for(int ocp = 0; ocp < ocountput.length; ocp++) {
				if(ocountput[ocp] != 0) {
					int[] opd = new int[2];
					opd[0] = ocp;
					opd[1] = ocountput[ocp];
					int ps1 = opd[1];
					int[] mcd = getSmallFirstRoundOutputDifferential(opd, input_position);
					
					List secondopd = getSmallSecondRoundDifferential(mcd, table, input_position, output_position);
					
					for(int sop = 0; sop < secondopd.size(); sop++) {
						int[] thirdipd = (int[]) secondopd.get(sop);
						int ps2 = thirdipd[thirdipd.length-1];
						
						int outputd0 = 0;
						int outputd1 = 0;
						long[] thirdopd = getSmallThirdRoundDifferentialByApproximationTable_00(thirdipd, outputd0, outputd1, nab, ncd);
						long ps3 = thirdopd[thirdopd.length-1];
						
						r3p[i] += ps3;
						
						pcount[i] += ps1 * ps2 * ps3;
					}
					
				}
			}
			
//			System.out.println("input difference: " + i + " total probability: " + pcount[i]);
		}
		
		return pcount;
		
	}
	
	public static int[] getSmallFirstRoundOutputDifferential(int[] opd, int input_position) {
		
		int[] beforemc = new int[4];
		
		int position = input_position % 4;
		beforemc[position] = opd[0];
		
		int[] mcd = new int[5];
		for(int i = 0 ; i < mcd.length-1; i++) {
			mcd[i] = countMulti(MC[i][0], beforemc[0]) ^ countMulti(MC[i][1], beforemc[1]) ^ countMulti(MC[i][2], beforemc[2]) ^ countMulti(MC[i][3], beforemc[3]);
		}
		
		mcd[mcd.length-1] = opd[1];
		
		return mcd;
		
 	}
	
	public static List getSmallSecondRoundDifferential(int[] ipd, int[][] table, int input_position, int output_position) {
		
		int[] ocopd0 = table[ipd[0]];
		int[] ocopd1 = table[ipd[1]];
		int[] ocopd2 = table[ipd[2]];
		int[] ocopd3 = table[ipd[3]];
		
		List output = new ArrayList<>();
		
		for(int opd0 = 0; opd0 < ocopd0.length; opd0++) {
			if(ocopd0[opd0] != 0) {
				int p0 = ocopd0[opd0];
				
				for(int opd1 = 0; opd1 < ocopd1.length; opd1++) {
					if(ocopd1[opd1] != 0) {
						int p1 = ocopd1[opd1];
						
						for(int opd2 = 0; opd2 < ocopd2.length; opd2++) {
							if(ocopd2[opd2] != 0) {
								int p2 = ocopd2[opd2];
								
								for(int opd3 = 0; opd3 < ocopd3.length; opd3++) {
									if(ocopd3[opd3] != 0) {
										int p3 = ocopd3[opd3];
										
										int[] outputd = new int[5];
										outputd = getMC_by_Input_Output(outputd, opd0, opd1, opd2, opd3, input_position, output_position);		
										outputd[4] = p0 * p1 * p2 * p3; 		
										
										output.add(outputd);
										
									}
								}
							}
						}
					}
				}
			}
		}
		
		return output;
		
	}
	
	// get the result after MC operation according to the given input position and output position
	public static int[] getMC_by_Input_Output(int[] outputd, int opd0, int opd1, int opd2, int opd3, int input_position, int output_position) {
		
		int column = output_position / 4;
		
		if(input_position == 0 || input_position == 5 || input_position == 10 || input_position == 15) {
			if(column == 0) {
				outputd[0] = countMulti(2, opd0);
				outputd[1] = opd3;
				outputd[2] = countMulti(2, opd2);
				outputd[3] = opd1;	
			} else if(column == 1) {
				outputd[0] = opd3;
				outputd[1] = countMulti(3, opd2);
				outputd[2] = opd1;
				outputd[3] = countMulti(3, opd0);	
			} else if(column == 2) {
				outputd[0] = opd2;
				outputd[1] = countMulti(2, opd1);
				outputd[2] = opd0;
				outputd[3] = countMulti(2, opd3);
			} else if(column == 3) {
				outputd[0] = countMulti(3, opd1);
				outputd[1] = opd0;
				outputd[2] = countMulti(3, opd3);
				outputd[3] = opd2;	
			}
		} else if (input_position == 1 || input_position == 12 || input_position == 6 || input_position == 11) {
			if(column == 0) {
				outputd[0] = opd3;
				outputd[1] = countMulti(3, opd2);
				outputd[2] = opd1;
				outputd[3] = countMulti(3, opd0);
			} else if(column == 1) {
				outputd[0] = opd2;
				outputd[1] = countMulti(2, opd1);
				outputd[2] = opd0;
				outputd[3] = countMulti(2, opd3);
			} else if(column == 2) {
				outputd[0] = countMulti(3, opd1);
				outputd[1] = opd0;
				outputd[2] = countMulti(3, opd3);
				outputd[3] = opd2;	
			} else if(column == 3) {
				outputd[0] = countMulti(2, opd0);
				outputd[1] = opd3;
				outputd[2] = countMulti(2, opd2);
				outputd[3] = opd1;	
			}
		} else if (input_position == 2 || input_position == 8 || input_position == 13 || input_position == 7) {
			if(column == 0) {
				outputd[0] = opd2;
				outputd[1] = countMulti(2, opd1);
				outputd[2] = opd0;
				outputd[3] = countMulti(2, opd3);
			} else if(column == 1) {
				outputd[0] = countMulti(3, opd1);
				outputd[1] = opd0;
				outputd[2] = countMulti(3, opd3);
				outputd[3] = opd2;	
			} else if(column == 2) {
				outputd[0] = countMulti(2, opd0);
				outputd[1] = opd3;
				outputd[2] = countMulti(2, opd2);
				outputd[3] = opd1;
			} else if(column == 3) {
				outputd[0] = opd3;
				outputd[1] = countMulti(3, opd2);
				outputd[2] = opd1;
				outputd[3] = countMulti(3, opd0);
			}
		} else if (input_position == 3 || input_position == 4 || input_position == 9 || input_position == 14) {
			if(column == 0) {
				outputd[0] = countMulti(3, opd1);
				outputd[1] = opd0;
				outputd[2] = countMulti(3, opd3);
				outputd[3] = opd2;	
			} else if(column == 1) {
				outputd[0] = countMulti(2, opd0);
				outputd[1] = opd3;
				outputd[2] = countMulti(2, opd2);
				outputd[3] = opd1;
			} else if(column == 2) {
				outputd[0] = opd3;
				outputd[1] = countMulti(3, opd2);
				outputd[2] = opd1;
				outputd[3] = countMulti(3, opd0);
			} else if(column == 3) {
				outputd[0] = opd2;
				outputd[1] = countMulti(2, opd1);
				outputd[2] = opd0;
				outputd[3] = countMulti(2, opd3);
			}
		}
		
		return outputd;
		
	}
	
	public static long[] getSmallThirdRoundDifferentialByApproximationTable_0(int[] ipd, int outputd, long[][][] nab, long[][][] ncd) {
		 
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
		
		long[] opd = new long[5];
		opd[0] = a;
		opd[1] = b;
		opd[2] = c;
		opd[3] = d;
		opd[4] = op;
	
		
		return opd;
		
	}
	
	public static long[] getSmallThirdRoundDifferentialByApproximationTable_00(int[] ipd, int outputd0, int outputd1, long[][][] nab, long[][][] ncd) {
		 
		int a = ipd[0];
		int b = ipd[1];
		int c = ipd[2];
		int d = ipd[3];
		
		long op = 0;
		long[] outab = nab[a][b];
		long[] outcd = ncd[c][d];
		for(int ab = 0; ab < outab.length; ab++) {
			for(int cd = 0; cd < outcd.length; cd++) {
				//the equal item is the output difference, that is, ()A'+()B'+()C'+()D'=0
				int opd = ab ^ cd;
				if(opd == outputd0) {
					op += outab[ab] * outcd[cd];
				}
			}
		}
		
		long[] opd = new long[5];
		opd[0] = a;
		opd[1] = b;
		opd[2] = c;
		opd[3] = d;
		opd[4] = op;
	
		
		return opd;
		
	}
	
	public static int[][] getSmallOneSboxDifferential(int[] sbox, int c) {
		
		int[][] na = new int[16][16];
		
		for(int a1 = 0; a1 < 16; a1++) {
			for(int a2 = 0; a2 < 16; a2++) {
				
				int ind = a1 ^ a2;
				
				int sa1 = sbox[a1];
				int sa2 = sbox[a2];
				
				int outd = countMulti(c, sa1) ^ countMulti(c, sa2);
				
				na[ind][outd]++;
				
			}
		}
		
		return na;
		
	}
	
	public static long[][][] getSmallTwoSboxDifferential(int[][] na, int[][] nb) {
		
		long[][][] nab = new long[16][16][16];
		int[] outdp = new int[16];
		
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
	
	public static long[][][] getSmallTwoSboxDifferential_00(int[][] table, int ca1, int cb1, int ca2, int cb2, int len) {
		
		int number = (int) Math.pow(2, len);
		
		long[][][] nab = new long[number][number][256];
		for(int a = 0; a < number; a++) {
			for(int b = 0; b < number; b++) {
				int[] at = table[a];
				for(int a1 = 0; a1 < at.length; a1++) {
					if(at[a1] != 0) {
						int[] bt = table[b];
						for(int b1 = 0; b1 < bt.length; b1++) {
							if(bt[b1] != 0) {
								int x1 = countMulti(ca1, a1) ^ countMulti(cb1, b1);
								int x2 = countMulti(ca2, a1) ^ countMulti(cb2, b1);
								int x12 = Integer.parseInt(getBitString(x1, 4) + getBitString(x2, 4), 2);
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
	
	public static String getBitString(int i, int len) {
		String str = Integer.toBinaryString(i);
		while(str.length() < len) {
			str = "0" + str;
		}
		
		return str;
	}
	
	public static int countMulti(int c1, int c2) {
		
		String result = "00";
		String poly = "0011";
		
		String result1 = getBitString(c2, 4); 
		
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
		
		result = addKeyBy2Bit(getBitString(Integer.parseInt(result, 16), 4), result1);
		
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
	
	public static int[][] getDDT(int[] sbox) {
		
		int[][] table = generateTable(sbox);
		
		return table;
		
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
