package com.euler.tmk;

import com.euler.util.UtilsForFactoringInts;

public class Euler0012 {

	private static int getNDivisorsForTriangularNumber(final int k) {
		final int a, b;
		if (k % 2 == 0) {
			a = k / 2;
			b = k + 1;
		} else {
			a = k;
			b = (k + 1) / 2;
		}
		final long[] aDivisors = UtilsForFactoringInts.getAllDivisors(a);
		final long[] bDivisors = UtilsForFactoringInts.getAllDivisors(b);
		final int nADivisors = aDivisors.length;
		final int nBDivisors = bDivisors.length;
		return nADivisors * nBDivisors;
	}

	public static void main(final String[] args) {
		for (int k = 1;; ++k) {
			final int nDivisors = getNDivisorsForTriangularNumber(k);
			System.out.printf("\n%d. %d", k, nDivisors);
			if (nDivisors > 500) {
				final long triangularNumber = ((long) k * (k + 1)) / 2;
				System.out.printf("\t\t%d", triangularNumber);
				break;
			}
		}
	}

}
