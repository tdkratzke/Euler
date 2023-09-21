package com.euler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class UtilsForFactoringInts {

	/** https://primes.utm.edu/largest.html */
	final private static long _MaxPrimeToFind;
	static {
		final long ell = Integer.MAX_VALUE + 1l;
		_MaxPrimeToFind = ell * ell - 1;
	}

	/** Returns 1 if n is 1, and a prime factor otherwise. */
	public static long getPrimeFactor(final long n) {
		if (n < 4) {
			return n;
		}
		if (n > _MaxPrimeToFind) {
			return -1L;
		}
		final int sqrt = (int) Math.sqrt(n);
		final BitSet toIgnore = new BitSet(sqrt);
		for (int k0 = 2;; k0 = toIgnore.nextClearBit(k0 + 1)) {
			if (k0 > sqrt) {
				return n;
			}
			if (n % k0 == 0) {
				return k0;
			}
			/** We'll ignore any multiple of k0 up to sqrt. */
			final int maxMultiple = sqrt / k0;
			for (int k1 = 1; k1 <= maxMultiple; ++k1) {
				toIgnore.set(k1 * k0);
			}
		}
	}

	public static long[][] getPrimeFactorsWithCounts(long n) {
		final ArrayList<long[]> factorsWithCounts = new ArrayList<>();
		while (n > 1) {
			final long prime = getPrimeFactor(n);
			int count = 0;
			do {
				n /= prime;
				++count;
			} while (n % prime == 0);
			factorsWithCounts.add(new long[]{prime, count});
		}
		return factorsWithCounts.toArray(new long[factorsWithCounts.size()][]);
	}

	public static long[] getAllDivisors(final long n) {
		final long[][] primeFactorsWithCounts = getPrimeFactorsWithCounts(n);
		final int nPrimeFactors = primeFactorsWithCounts.length;
		/** Build a table of the primes' powers. */
		int nDivisors = 1;
		final long[][] primePowers = new long[nPrimeFactors][];
		for (int k0 = 0; k0 < nPrimeFactors; ++k0) {
			final long prime = primeFactorsWithCounts[k0][0];
			final int nPowers = (int) (primeFactorsWithCounts[k0][1] + 1);
			primePowers[k0] = new long[nPowers];
			primePowers[k0][0] = 1;
			for (int k1 = 1; k1 < nPowers; ++k1) {
				primePowers[k0][k1] = primePowers[k0][k1 - 1] * prime;
			}
			nDivisors *= nPowers;
		}
		final long[] divisors = new long[nDivisors];
		for (int k1 = 0; k1 < nDivisors; ++k1) {
			int divisor = 1;
			int k11 = k1;
			for (int k0 = 0; k0 < nPrimeFactors; ++k0) {
				final int nPowers = primePowers[k0].length;
				final int power = k11 % nPowers;
				k11 /= nPowers;
				divisor *= primePowers[k0][power];
			}
			divisors[k1] = divisor;
		}
		Arrays.sort(divisors);
		return divisors;
	}

	/** Numbers of interest: 36028796784082931l, 189812531 */
	final private static boolean _MaxValueIsPrime = getHighestPrimeAtMost(
			Integer.MAX_VALUE) == Integer.MAX_VALUE;

	public static int getHighestPrimeAtMost(final int nIn) {
		if (nIn == Integer.MAX_VALUE && _MaxValueIsPrime) {
			return Integer.MAX_VALUE;
		}
		int n = nIn;
		if (n < 4) {
			return n;
		}
		int sqrt = (int) Math.sqrt(n);
		final BitSet comps = new BitSet(sqrt + 1);
		for (int prime = 2; prime <= sqrt; prime = comps.nextClearBit(prime + 1)) {
			if (n % prime == 0) {
				DOWNWARD : for (;;) {
					n = comps.previousClearBit(n - 1);
					if (n < 4) {
						return n;
					}
					/**
					 * n appears to be a prime. Check against the previous primes up to prime.
					 */
					sqrt = (int) Math.sqrt(n);
					for (int prime2 = 2; prime2 <= prime; prime2 = comps.nextClearBit(prime2 + 1)) {
						if (n % prime2 == 0) {
							/** n is definitely not prime. */
							continue DOWNWARD;
						}
						if (prime2 >= sqrt) {
							/** n is definitely prime. */
							return n;
						}
					}
					/**
					 * We got through all primes up to prime for n. We're now checking n up to its
					 * sqrt.
					 */
					break;
				}
			}
			for (int primePower = prime;; primePower *= prime) {
				if (primePower <= 0 || primePower > sqrt) {
					break;
				}
				final int top1 = sqrt / primePower;
				for (int oldComp = comps.nextSetBit(1);;) {
					if (oldComp < 0 || oldComp > top1) {
						break;
					}
					comps.set(oldComp * primePower);
					oldComp = comps.nextSetBit(oldComp + 1);
				}
				for (int oldPrime = comps.nextClearBit(2);;) {
					if (oldPrime < 0 || oldPrime > top1 || oldPrime >= prime) {
						break;
					}
					comps.set(oldPrime * primePower);
					oldPrime = comps.nextClearBit(oldPrime + 1);
				}
				if (primePower > prime) {
					comps.set(primePower);
				}
			}
		}
		return n;
	}

	public static BitSet getNPrimesAtLeastM(final int n, final int m) {
		BitSet bitSet = null;
		for (int bound = m * n;; bound *= n) {
			bitSet = getPrimesUpTo(bound);
			bitSet.clear(0, m);
			if (bitSet.cardinality() >= n) {
				break;
			}
		}
		int k = bitSet.nextSetBit(0);
		for (int count = 0; count < n; k = bitSet.nextSetBit(k + 1), ++count) {
		}
		if (k >= 0) {
			bitSet.clear(k, bitSet.length());
		}
		return bitSet;
	}

	public static BitSet getPrimesUpTo(int n) {
		final boolean askingAboutMaxValue = n == Integer.MAX_VALUE;
		if (askingAboutMaxValue) {
			--n;
		}
		final BitSet primes = new BitSet(1 + n);
		if (n < 2) {
			return primes;
		}
		final int topIntOfInterest = Math.min(n, Integer.MAX_VALUE);
		primes.set(2, n + 1);
		for (int k = primes.nextSetBit(2); k >= 0; k = primes.nextSetBit(k + 1)) {
			for (int kk = k;;) {
				if (kk > topIntOfInterest - k) {
					break;
				}
				kk += k;
				primes.clear(kk);
			}
		}
		if (askingAboutMaxValue && _MaxValueIsPrime) {
			primes.nextSetBit(Integer.MAX_VALUE);
		}
		return primes;
	}

	public static int getPi(final int n) {
		return getPrimesUpTo(n).cardinality();
	}

	public static void main(final String[] args) {
		/** Test getDivisors. */
		final long n0 = (1L << 24) - 1;
		final long[] divisors = getAllDivisors(n0);
		System.out.printf("[%d]\n%s", n0, GetStrings.getString(divisors));

		/** Test pi(n)/(n/ln(n)). */
		final int n = Integer.MAX_VALUE / 2;
		final int piOfN = getPi(n);
		final double nOverLogOfN = n / Math.log(n);
		System.out.printf("\nn[%d], pi(n)[%d], n/ln(n)[%f], pi(n)/(n/ln(n)[%f]", //
				n, piOfN, nOverLogOfN, piOfN / nOverLogOfN);

		/** Test getNPrimesAtLeast. */
		System.out.printf("\n%s", getNPrimesAtLeastM(5, 20));

		/** Test getNPrimesAtLeast. */
		final int nTries = 1 << 6;
		final int inc = 1 << 24;
		int smallN = Integer.MAX_VALUE;
		for (int k = 0; k < nTries; ++k) {
			final int bigPrime = getHighestPrimeAtMost(smallN);
			final boolean isPrime = bigPrime == smallN;
			System.out.printf("\n%d.\t%d: %s", //
					k, smallN, //
					isPrime
							? "Y"
							: String.format("N→%d(%d)", //
									bigPrime, getPrimeFactor(smallN)));
			smallN -= inc + 1;
		}
		System.out.printf("\n\n");
		long bigN = Integer.MAX_VALUE;
		bigN *= 1 << 24;
		for (int k = 0; k < nTries; ++k) {
			final long factor = getPrimeFactor(bigN);
			final boolean isPrime = factor == 1;
			System.out.printf("\n%d.\t%d: %s", k, bigN,
					isPrime ? "Y" : String.format("N(%d)", factor));
			bigN -= inc + 1;
		}
		bigN = 189812531;
		final long factor = getPrimeFactor(bigN);
		final boolean isPrime = factor == 1;
		System.out.printf("\n%d: %s", bigN, isPrime ? "Y" : String.format("N(%d)", factor));
		bigN -= inc + 1;
	}

}
