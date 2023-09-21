package com.euler.tmk;

import java.util.BitSet;

import com.euler.util.UtilsForFactoringInts;

public class Euler0010 {

	final static private int _N = 2000000;

	public static void main(final String[] args) {
		final BitSet primes = UtilsForFactoringInts.getPrimesUpTo(_N);
		long sum = 0;
		for (int p = primes.nextSetBit(0); p >= 0; p = primes.nextSetBit(p + 1)) {
			sum += p;
		}
		System.out.printf("Sum = %d.", sum);
	}
}
