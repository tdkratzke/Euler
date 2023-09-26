package com.euler.tmk;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.stream.IntStream;

public class Euler0122 {

	public static void main(final String[] args) {
		final int maxPower = 365;

		/** Initialize the answer vector and which ones we have answers for. */
		final int[] m = new int[maxPower + 1];
		Arrays.fill(m, 0);
		final BitSet completed = new BitSet(maxPower + 1);
		completed.set(1);

		/** Set the initial sets to be just { {1} }. */
		HashSet<BitSet> sets = new HashSet<BitSet>();
		final BitSet initialSet = new BitSet(2);
		initialSet.set(1);
		sets.add(initialSet);

		final int[] setCounts = new int[maxPower - 1];
		setCounts[0] = 1;

		K0_LOOP : for (int nMults = 1; nMults < maxPower; ++nMults) {
			if (nMults <= 5) {
				int setNumber = 1;
				for (final BitSet newSet : sets) {
					if (nMults == 1 && setNumber == 1) {
					} else if (nMults > 1 && setNumber == 1) {
						System.out.printf("\n\n\n");
					} else if (setNumber % 5 == 1) {
						System.out.printf("\n\n");
					} else {
						System.out.printf("\n");
					}
					System.out.printf("%d mults, set# %d: %s", nMults - 1, setNumber++,
							newSet.toString());
				}
			} else if (nMults == 6) {
				System.out.println("\n\n\n\nPress Enter key to continue...");
				try {
					System.in.read();
				} catch (final Exception e) {
				}
			}
			final HashSet<BitSet> newSets = new HashSet<BitSet>();
			/**
			 * For each set set, compute the new powers that would result from an additional
			 * multiplication, and for each new power newPower, use newPower to augment set and
			 * store newSet in newSets.
			 */
			for (final BitSet set : sets) {
				for (int k1 = set.nextSetBit(1); k1 >= 0; k1 = set.nextSetBit(k1 + 1)) {
					for (int k2 = set.nextSetBit(1); k2 >= 0; k2 = set.nextSetBit(k2 + 1)) {
						final int newPower = k1 + k2;
						if (newPower > maxPower) {
							break;
						}
						if (set.get(newPower)) {
							continue;
						}
						if (!completed.get(newPower)) {
							m[newPower] = nMults;
							completed.set(newPower);
							final int nToGo = maxPower - completed.cardinality();
							if (nMults > 9) {
								System.out.printf(
										"\nm(%03d) = %d, have %d new sets, %d unknown m-values.", newPower,
										nMults, newSets.size(), nToGo);
								if (nToGo <= 20) {
									final BitSet toGo = (BitSet) completed.clone();
									toGo.flip(0, maxPower + 1);
									toGo.set(0, false);
									System.out.printf("\t%s", toGo.toString());
								}
							}
							if (completed.nextClearBit(2) == maxPower + 1) {
								setCounts[nMults] = newSets.size();
								break K0_LOOP;
							}
						}
						final BitSet newSet = (BitSet) set.clone();
						newSet.set(newPower);
						newSets.add(newSet);
					}
				}
			}
			sets = newSets;
			setCounts[nMults] = sets.size();
		}

		String s = "";
		for (int k = 0; setCounts[k] > 0; ++k) {
			s += String.format("%sAfter %d mults, we have %d sets.", k == 0 ? "" : "\n", k,
					setCounts[k]);
		}

		for (int k = 1; k <= maxPower; ++k) {
			s += String.format("%sm(%03d) = %d", k % 5 == 1 ? "\n\n" : "\n", k, m[k]);
		}
		final int sum = IntStream.of(m).sum();
		s += String.format("\n\nsum[%d]", sum);
		try (PrintStream ps = new PrintStream(new File("out.txt"))) {
			ps.printf("%s", s);
		} catch (final IOException e) {
		}
		System.out.printf("%s", s);
	}

}
