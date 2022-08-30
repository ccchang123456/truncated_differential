package discuss1TruncatedDifferential;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Experiment_FourRoundDistinguishingAttack_AES {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		int delta_set = 25;
		int key_number = 0; //key_number = 0, ..., 4
		get_aes(delta_set, key_number);

	}
	
	public static BigDecimal[] get_aes(int delta_set, int number) throws FileNotFoundException {
		
		long[][] count_array = new long[2][100];
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("experimental data");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("the c1 and c2 of the i-th experiment (i = 0, ... , 99)");
		System.out.println("---------------------------------------------------------------------------");
		for(int i = 0; i < count_array[0].length; i++) {

			String filepath_0 = "./result/result_" + number + "/rebuttal_result_collision_4_AES_0_25_key" + number + "_deltaSet[" + i + "].txt";
			String filepath_00 = "./result/result_" + number + "/rebuttal_result_collision_4_AES_00_25_key" + number + "_deltaSet[" + i + "].txt";

			Scanner sc_0 = new Scanner(new FileReader(filepath_0));
			Scanner sc_00 = new Scanner(new FileReader(filepath_00));
			
			if(sc_0.hasNextLong()) {
				long count = sc_0.nextLong();
				count_array[0][i] = count;
			}
			if(sc_00.hasNextLong()) {
				long count = sc_00.nextLong();
				count_array[1][i] = count;
			}

			System.out.println(i + " : " + count_array[0][i] + " " + count_array[1][i]);
			
		}
		BigDecimal[] count_radio = new BigDecimal[count_array[0].length];
		for(int i = 0; i < count_array[0].length; i++) {
			count_radio[i] = BigDecimal.valueOf(count_array[1][i]);
			count_radio[i] = count_radio[i].divide(BigDecimal.valueOf(count_array[0][i]), 30, BigDecimal.ROUND_HALF_UP);

		}		
		System.out.println();
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("distinguishing attack");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("the c of the i-th experiment (i = 0, ... , 99)");
		System.out.println("---------------------------------------------------------------------------");
		
		BigDecimal[] array_attack = new BigDecimal[count_array[0].length];
		int count_attack = 0;
		BigDecimal[] threshold_array = new BigDecimal[100];
		threshold_array[25] = BigDecimal.valueOf(4279867617.136427597);
		BigDecimal threshold = threshold_array[delta_set];
		BigDecimal multiplier = BigDecimal.valueOf(Math.pow(2, 25));
		multiplier = multiplier.multiply(BigDecimal.valueOf(32640));
		System.out.println("threshold = " + threshold);
		for(int i = 0; i < array_attack.length; i++) {
			array_attack[i] = count_radio[i].multiply(multiplier);
			if(array_attack[i].compareTo(threshold) == -1) {
				count_attack++;
			}
			System.out.println(i + " : " + array_attack[i]);
		}
		System.out.println("success number: " + count_attack);
		
		return array_attack;
	}

}
