package com.euler.tdk.euler0205.tmk;

import java.util.Arrays;

public class Euler0205 {

	private static int getCount(final int targetSum, final int maxOnDie, final int nDice) {
		if (nDice == targetSum || nDice * maxOnDie == targetSum) {
			return 1;
		}
		if (targetSum < nDice || nDice * maxOnDie < targetSum) {
			return 0;
		}
		final int minUsesOfMaxOnDie, maxUsesOfMaxOnDie;
		if (maxOnDie == 1) {
			minUsesOfMaxOnDie = maxUsesOfMaxOnDie = nDice;
		} else {
			/** Compute the minimum and maximum numbers for using maxOnDie. */
			final int maxMinus1 = maxOnDie - 1;
			minUsesOfMaxOnDie = Math.max(0, targetSum - nDice * maxMinus1);
			maxUsesOfMaxOnDie = (targetSum - nDice) / maxMinus1;
		}
		int cum = 0;
		for (int k = minUsesOfMaxOnDie; k <= maxUsesOfMaxOnDie; ++k) {
			final int count = getCount(//
					targetSum - k * maxOnDie, maxOnDie - 1, nDice - k);
			final int nChooseK = getNChooseK(nDice, k);
			cum += nChooseK * count;
		}
		return cum;
	}

	private static int[] getCums(final int maxOnDie, final int nDice) {
		final int maxValue = maxOnDie * nDice;
		final int[] cums = new int[maxValue + 1];
		Arrays.fill(cums, 0);
		for (int targetSum = nDice; targetSum <= maxValue; ++targetSum) {
			final int count = getCount(targetSum, maxOnDie, nDice);
			cums[targetSum] = count + cums[targetSum - 1];
		}
		return cums;
	}

	public static double getProb(final int maxOnDieA, final int nDiceA, final int maxOnDieB, final int nDiceB) {
		final int[] cumsA = getCums(maxOnDieA, nDiceA);
		final int cumsALength = cumsA.length;
		final double denomA = cumsA[cumsALength - 1];
		final int[] cumsB = getCums(maxOnDieB, nDiceB);
		final int cumsBLength = cumsB.length;
		final double denomB = cumsB[cumsBLength - 1];

		double cumProb = 0d;
		for (int k = nDiceA; k <= nDiceA * maxOnDieA; ++k) {
			final double probA = (cumsA[k] - cumsA[k - 1]) / denomA;
			final int cumB = cumsB[Math.min(k - 1, cumsBLength - 1)];
			final double probB = cumB / denomB;
			cumProb += probA * probB;
		}
		return cumProb;
	}

	public static int getNChooseK(final int n, final int k) {
		if (k == n || k == 0) {
			return 1;
		}
		return getNChooseK(n - 1, k - 1) + getNChooseK(n - 1, k);
	}

	public static void main(final String[] args) {
		final int maxOnDieA = 4, nDiceA = 9;
		final int maxOnDieB = 6, nDiceB = 6;
		final double prob = getProb(maxOnDieA, nDiceA, maxOnDieB, nDiceB);
		System.out.printf("%.7f", prob);
	}
}
