package com.euler.tdk.euler0781.tmk;

import java.util.Arrays;

public class Euler0781 {
	final static int _Modulo = 1000000007;

	public static long euler0781_0Based(final int n, final int modulo) {
		int[] alfa = new int[n];
		Arrays.fill(alfa, 1);

		for (int bravoN = n - 2; bravoN > 0; bravoN -= 2) {
			final int[] bravo = new int[bravoN];
			int cum = 0;
			for (int i = 0; i < bravoN; ++i) {
				cum = (cum + alfa[i]) % _Modulo;
				bravo[i] = (int) ((alfa[i + 2] * (i + 2L) + cum) % _Modulo);
			}
			alfa = bravo;
		}
		return alfa[1];
	}

	public static long euler0781_1Based(final int n, final int modulo) {
		int[] alfa = new int[n + 1];
		Arrays.fill(alfa, 1);

		for (int bravoN = n - 2; bravoN > 0; bravoN -= 2) {
			final int[] bravo = new int[bravoN + 1];
			int cum = 0;
			for (int i = 1; i <= bravoN; ++i) {
				cum = (cum + alfa[i]) % _Modulo;
				bravo[i] = (int) ((alfa[i + 2] * (i + 1L) + cum) % _Modulo);
			}
			alfa = bravo;
		}
		return alfa[2];
	}

	public static void main(final String[] args) {
		final int n = 50000;
		final long millis = System.currentTimeMillis();
		System.out.printf("n[%d] FeynmanF[%d]", n, euler0781_1Based(n, _Modulo));
		System.out.printf(".\tTook %d millis.", System.currentTimeMillis() - millis);
	}
}
