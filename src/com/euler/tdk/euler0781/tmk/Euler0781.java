package com.euler.tdk.euler0781.tmk;

import java.util.Arrays;

public class Euler0781 {
	final static int _Modulo = 1000000007;

	public static long euler0781(final int n, final int modulo) {
		int[] alpha = new int[n];
		Arrays.fill(alpha, 1);

		for (int alphaN = n; alphaN > 2; alphaN -= 2) {
			final int bravoN = alphaN - 2;
			final int[] bravo = new int[bravoN];
			int cum = 0;
			for (int i = 0; i < bravoN; ++i) {
				cum = (cum + alpha[i]) % modulo;
				bravo[i] = (int) ((alpha[i + 2] * (i + 2L) + cum) % modulo);
			}
			alpha = bravo;
		}
		return alpha[1];
	}

	public static void main(final String[] args) {
		final int n = 50000;
		final long millis = System.currentTimeMillis();
		System.out.printf("n[%d] euler0781[%d]", n, euler0781(n, _Modulo));
		System.out.printf(".\tTook %d millis.", System.currentTimeMillis() - millis);
	}
}
