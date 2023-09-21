package com.euler.tdk;
import java.util.Scanner;

public class Euler0009 {

	public static void main(final String[] args) {
		final Scanner in = new Scanner(System.in);
		final int t = in.nextInt();
		for (int a0 = 0; a0 < t; a0++) {
			final int n = in.nextInt();
			System.out.println(pythag(n));
		}
		in.close();
	}

	public static int pythag(final int target) {
		int temp = -1;
		for (int a = 1; a <= (target / 3) - 1; a++) {
			for (int b = a + 1; b <= (target - a) / 2; b++) {
				final int c = target - b - a;
				if (a * a + b * b == c * c && a * b * c > temp) {
					temp = a * b * c;
				}
			}
		}

		return temp;
	}
}