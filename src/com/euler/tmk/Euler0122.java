package com.euler.tmk;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.stream.IntStream;

public class Euler0122 {

	public static void main(final String[] args) {
		final int maxPower = 220;
		final int[] mValues = new int[maxPower + 1];
		mValues[0] = 0;
		mValues[1] = 0;
		Arrays.fill(mValues, 2, maxPower + 1, maxPower);

		HashSet<BitSet> bitSets = new HashSet<BitSet>();
		final BitSet trivial = new BitSet(2);
		trivial.set(1);
		bitSets.add(trivial);

		final BitSet completed = new BitSet(maxPower + 1);
		completed.set(1);

		NMULTS_LOOP : for (int nMults = 1; nMults < maxPower; ++nMults) {
			final HashSet<BitSet> newBitSets = new HashSet<BitSet>();
			for (final BitSet bitSet : bitSets) {
				/**
				 * Compute the new powers and, for each one, use it to augment bitSet to form a
				 * new BitSet of byproducts.
				 */
				final BitSet newPowers = new BitSet();
				for (int k0 = bitSet.nextSetBit(1); k0 >= 0; k0 = bitSet.nextSetBit(k0 + 1)) {
					for (int k1 = bitSet.nextSetBit(1); k1 >= 0; k1 = bitSet.nextSetBit(k1 + 1)) {
						final int k = k0 + k1;
						if (k > maxPower) {
							break;
						}
						if (!bitSet.get(k)) {
							newPowers.set(k);
						}
					}
				}
				for (int k = newPowers.nextSetBit(2); k >= 0; k = newPowers.nextSetBit(k + 1)) {
					final BitSet newBitSet = (BitSet) bitSet.clone();
					newBitSet.set(k);
					if (newBitSets.add(newBitSet)) {
						if (nMults < mValues[k]) {
							mValues[k] = nMults;
							completed.set(k);
							if (completed.nextClearBit(1) == maxPower + 1) {
								break NMULTS_LOOP;
							}
						}
					}
				}
			}
			bitSets = newBitSets;
		}

		for (int k = 1; k <= maxPower; ++k) {
			System.out.printf("%sk[%d] mValue[%d]", k > 1 ? "\n" : "", k, mValues[k]);
		}
		final int sum = IntStream.of(mValues).sum();
		System.out.printf("\n\nsum[%d]", sum);
	}

}
