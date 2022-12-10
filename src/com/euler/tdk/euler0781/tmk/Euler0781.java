package com.euler.tdk.euler0781.tmk;

import java.util.Arrays;

public class Euler0781 {
	final static int _Modulo = 1000000007;

	public static int feynmanF(final int n, final int modulo) {
		final int[] alfa = new int[n + 1];
		Arrays.fill(alfa, 1);
		for (int bravoN = n - 2; bravoN > 0; bravoN -= 2) {
			int cum = 0;
			for (int i = 1; i <= bravoN; ++i) {
				cum = (cum + alfa[i]) % modulo;
				alfa[i] = (int) ((alfa[i + 2] * (i + 1L) + cum) % modulo);
			}
		}
		return alfa[2];
	}

	public static void main(final String[] args) {
		final int n = 50000;
		final long millis = System.currentTimeMillis();
		System.out.printf("n[%d] FeynmanF[%d]", n, feynmanF(n, _Modulo));
		System.out.printf(".\tTook %d millis.", System.currentTimeMillis() - millis);
	}
}
