/*Working from left-to-right if no digit is exceeded by the digit to its left it is called an increasing number; for example, 134468.

Similarly if no digit is exceeded by the digit to its right it is called a decreasing number; for example, 66420.

We shall call a positive integer that is neither increasing nor decreasing a "bouncy" number; for example, 155349.

Clearly there cannot be any bouncy numbers below one-hundred, but just over half of the numbers below one-thousand (525) are bouncy. In fact, the least number for which the proportion of bouncy numbers first reaches 50% is 538.

Surprisingly, bouncy numbers become more and more common and by the time we reach 21780 the proportion of bouncy numbers is equal to 90%.

Find the least number for which the proportion of bouncy numbers is exactly 99%.*/

//Takes two numbers, first is number of trials, second is percent to hit.

package com.euler.tdk;

import java.util.Scanner;

public class Euler0112 {
	public static void main(final String[] args) {
		try (Scanner in = new Scanner(System.in)) {
			final int test = in.nextInt();
			for (int a0 = 0; a0 < test; a0++) {
				final int percent = in.nextInt();

				int tries = 101;
				double bouncyCount = 1;
				while ((bouncyCount / tries) * 100 < percent) {
					tries += 1;
					if (isBouncy(tries)) {
						bouncyCount += 1;
					}
				}
				System.out.println(tries);
			}
		}
	}

	public static boolean isBouncy(final int num) {

		final String number = String.valueOf(num);
		final char[] digits = number.toCharArray();

		boolean bouncy = false;
		boolean up = true;
		boolean down = true;
		for (int i = 0; i < digits.length - 1; i++) {
			if (digits[i] < digits[i + 1]) {
				down = false;
			} else if (digits[i] > digits[i + 1]) {
				up = false;
			}
			if (!down && !up) {
				bouncy = true;
				break;
			}
		}

		return bouncy;
	}
}
//test comment