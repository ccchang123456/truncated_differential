package discuss1TruncatedDifferential;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

// this class is to handle the result files for AES of output one inactive byte, to calculate 4-round and 5-round distinguisher
public class Tool_ComputeResultForAES_4R {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		

//		for(int z = 1; z <= 4; z++) {
//			int[] b4 = new int[] {(z-1)*4, (z-1)*4+1, (z-1)*4+2, (z-1)*4+3};
//			for(int b = 0; b < b4.length; b++) {
//				if(b4[b] != 0) {
//					getDifferentialProbabilityForAES(z, b4[b]);
//				}
//			}
//		}
		
		int z = 4;
		int b = 15;
		getDifferentialProbabilityForAES(z, b);
//		getDifferentialProbabilityForAES_two_diagonal(z);

	}
	
	public static String[] ALLTABLENAME = new String[] {
			"SMALLAESSBOX", "PRESENTSBOX", "TOY6SBOX", "TOY8SBOX", "TOY10SBOX"
	};
	public static String[] NAME = new String[] {
			"small-aes", "present", "toy6", "toy8", "toy10"
	};
	
	// handle all probabilities corresponding to difference of each byte in output, and get the highest probability and corresponding difference
	public static void getDifferentialProbabilityForAES(int z, int b) throws IOException {
		
		int[] sbox = Differential_NewComputeDifferentialForAES_4R_Z1_0_A1.AESSBOX;		
		int[][] table = Differential_NewComputeDifferentialForAES_4R_Z1_0_A1.generateTable(sbox);
		
		int number = 256;
		
		String filepath = "/result/result_TruncatedDifferential/AES_4R/0/z" + z + "/byte" + b + "/";
		
		long[] p = new long[number];
		for(int i = 1; i < number; i++) {
			long acount = 0;
			for(int a = 1; a < number; a++) {
				String filename = "AES-z" + z + "-byte" + b + "-" + i + "-" + a + ".txt";
				Scanner sc = new Scanner(new FileReader(filepath  + filename));
		  		
				if(sc.hasNextLong()) {
					long count = sc.nextLong();
					System.out.println(count);
					acount += count;
				}
			}
			p[i] = acount;
		}
		
		BigDecimal[] pcount = new BigDecimal[number];
		for(int i = 0; i < pcount.length; i++) {
			pcount[i] = new BigDecimal(0);
		}
		for(int i = 1; i < number; i++) {
			int[] outi = table[i];
			for(int oi = 0; oi < outi.length; oi++) {
				if(outi[oi] != 0) {
					pcount[i] = pcount[i].add(BigDecimal.valueOf(outi[oi] * p[oi]));
				}
			}
		}

		System.out.println();
		BigDecimal max_probability = pcount[1];
		BigDecimal average_probability = pcount[1];
		int inputd = 1;
		for(int i = 2; i < pcount.length; i++) {
			int result = max_probability.compareTo(pcount[i]);
			if(result < 0) {
				max_probability = pcount[i];
				inputd = i;
			}
			average_probability = average_probability.add(pcount[i]);
		}
		
		System.out.println("the average probability:  " + average_probability);	
	}
	
	// handle all probabilities corresponding to difference of each two bytes in output, and get the highest probability, the biggest bias, and corresponding difference, respectively
	public static void getDifferentialProbabilityForAES_two_diagonal(int z) throws IOException {
		
		int[] sbox = Differential_NewComputeDifferentialForAES_4R_Z1_00_A1.AESSBOX;		
		int[][] table = Differential_NewComputeDifferentialForAES_4R_Z1_00_A1.generateTable(sbox);
		
		int number = 256;
		
		String filepath = "/result/result_TruncatedDifferential/AES_4R/00/z" + z + "/";
		
		long[] p = new long[number];
		for(int i = 1; i < number; i++) {
			long acount = 0;
			for(int a = 1; a < number; a++) {
				String filename = "AES_00_Z" + z + "-" + i + "-" + a + ".txt";
				
				Scanner sc = new Scanner(new FileReader(filepath + filename));
		  		
				if(sc.hasNextLong()) {
					long count = sc.nextLong();
					System.out.println(count);
					acount += count;
				}
			}
			p[i] = acount;
		}
		
		BigDecimal[] pcount = new BigDecimal[number];
		for(int i = 0; i < pcount.length; i++) {
			pcount[i] = new BigDecimal(0);
		}
		for(int i = 1; i < number; i++) {
			int[] outi = table[i];
			for(int oi = 0; oi < outi.length; oi++) {
				if(outi[oi] != 0) {
					pcount[i] = pcount[i].add(BigDecimal.valueOf(outi[oi] * p[oi]));
				}
			}
		}

		System.out.println();
		BigDecimal max_probability = pcount[1];
		BigDecimal average_probability = pcount[1];
		int inputd = 1;
		for(int i = 2; i < pcount.length; i++) {
			int result = max_probability.compareTo(pcount[i]);
			if(result > 0) {
				max_probability = pcount[i];
				inputd = i;
			}
			average_probability = average_probability.add(pcount[i]);
		}
		
		System.out.println("the average bias: " + average_probability);	
		
		System.out.println("the biggest bias: " + max_probability + " corresponding input difference " + inputd);
		for(int i = 1; i < number; i++) {
			System.out.println("input difference: " + i + " probability: " + pcount[i]);
		}
	}

}
