package com.euler.tmk;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.stream.IntStream;

public class Euler0122 {

	public static void main(final String[] args) {
		final int maxPower = 200;

		/** Initialize the answer vector and which ones we have answers for. */
		final int[] mValues = new int[maxPower + 1];
		Arrays.fill(mValues, 0, 2, 0);
		Arrays.fill(mValues, 2, maxPower + 1, maxPower);

		/** Set the initial byProductsS to be just {1}. */
		HashSet<BitSet> byProductsS = new HashSet<BitSet>();
		final BitSet zeroMults = new BitSet(2);
		zeroMults.set(1);
		byProductsS.add(zeroMults);

		NMULTS_LOOP : for (int nMults = 1; nMults < maxPower; ++nMults) {
			final HashSet<BitSet> newByProductsS = new HashSet<BitSet>();
			for (final BitSet byProducts : byProductsS) {
				/**
				 * Compute the new powers and, for each one, use it to augment byProducts to form
				 * newByProducts.
				 */
				final BitSet newPowers = new BitSet();
				for (int k0 = byProducts.nextSetBit(1); k0 >= 0; k0 = byProducts
						.nextSetBit(k0 + 1)) {
					for (int k1 = byProducts.nextSetBit(1); k1 >= 0; k1 = byProducts
							.nextSetBit(k1 + 1)) {
						final int power = k0 + k1;
						if (power > maxPower) {
							break;
						}
						if (!byProducts.get(power)) {
							newPowers.set(power);
						}
					}
				}
				NewPower_LOOP : for (int newPower = newPowers.nextSetBit(
						2); newPower >= 0; newPower = newPowers.nextSetBit(newPower + 1)) {
					final BitSet newByProducts = (BitSet) byProducts.clone();
					newByProducts.set(newPower);
					if (newByProductsS.add(newByProducts)) {
						if (mValues[newPower] == maxPower) {
							mValues[newPower] = nMults;
							for (int k = 1; k <= maxPower; ++k) {
								if (mValues[k] == maxPower) {
									continue NewPower_LOOP;
								}
							}
							break NMULTS_LOOP;
						}
					}
				}
			}
			byProductsS = newByProductsS;
		}

		for (int k = 1; k <= maxPower; ++k) {
			System.out.printf("%sk[%d] mValue[%d]", k == 1 ? "" : (k % 5 == 1 ? "\n\n" : "\n"),
					k, mValues[k]);
		}
		final int sum = IntStream.of(mValues).sum();
		System.out.printf("\n\nsum[%d]", sum);
	}

}
