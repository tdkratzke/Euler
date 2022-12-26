package com.euler.tdk.euler0007;

import java.util.ArrayList;
import java.util.Scanner;

public class Euler0007 {
	public static void main(final String[] args) {

		final Scanner in = new Scanner(System.in);
		final int t = in.nextInt();
		final ArrayList<Integer> primes = new ArrayList<>();
		primes.add(2);
		primes.add(3);

		for (int a0 = 0; a0 < t; a0++) {
			final int n = in.nextInt();
			int count = 1;
			int tempPrime = primes.get(primes.size() - 1);

			for (int j = tempPrime; j <= 1299709; j += 2) {

				if (n <= primes.size()) {
					System.out.println(primes.get(n - 1));
					break;
				}

				if (isPrime(j)) {
					tempPrime = j;
					count++;
					if (tempPrime > primes.get(primes.size() - 1)) {
						primes.add(j);
					}
				}

				if (count == n) {
					System.out.println(tempPrime);
					break;
				}
			}
		}
		in.close();
	}

	public static boolean isPrime(final long num) {
		for (int i = 3; i <= Math.sqrt(num); i += 2) {
			if (num % i == 0) {
				return false;
			}
		}
		// if(num%2==0){
		// return false;
		// }
		return true;
	}
}