package com.euler.tdk.euler0566.tmk;

import java.util.ArrayList;
import java.util.Scanner;

public class Cake {
	final private static double _Eps = 1.0e-10;

	private class Cell {
		private double _start;
		private boolean _pink;
		private Cell _pvs, _nxt;

		public Cell(final double start, final boolean pink) {
			_start = start;
			_pink = pink;
			_pvs = _nxt = null;
		}

		private double computeLength() {
			if (_nxt == this) {
				return 1d;
			}
			final double nxtStart = _nxt._start;
			if (nxtStart >= _start) {
				return nxtStart - _start;
			}
			return (1d - _start) + nxtStart;
		}

		public String getString() {
			final double nxtStart = this == _nxt ? (_start + 1d) : _nxt._start;
			return String.format("[%.3f:%.3f(%.3f)],%s", //
					_start, nxtStart, computeLength(), _pink ? "PINK" : "GRAY");
		}

		@Override
		public String toString() {
			return getString();
		}
	}

	private Cell _current;

	public Cake() {
		_current = new Cell(/* start= */0d, /* pink= */true);
		_current._nxt = _current._pvs = _current;
	}

	private Cell getContainingCell(final double d) {
		for (Cell c = _current;; c = c._nxt) {
			final double cStart = c._start;
			if (areWithinEps(cStart, d)) {
				return c;
			}
			final Cell nxt = c._nxt;
			final double nxtStart = nxt._start;
			if (areWithinEps(nxtStart, d)) {
				return nxt;
			}
			if (cStart < nxtStart) {
				if (d <= cStart || d >= nxtStart) {
					continue;
				}
			} else if (nxtStart <= d && d <= cStart) {
				continue;
			}
			final Cell newC = new Cell(d, c._pink);
			nxt._pvs = newC;
			newC._nxt = nxt;
			c._nxt = newC;
			newC._pvs = c;
			return newC;
		}
	}

	public int countFlips(final double[] lengths) {
		final int nLengths = lengths.length;
		double start = 0d;
		for (int nFlips = 0;; ++nFlips) {
			/** Find startC. */
			final Cell startC = getContainingCell(start);
			start = startC._start;
			_current = startC;
			/** Find firstNotToFlipC. */
			final double length = lengths[nFlips % nLengths];
			double end = (start + length) % 1d;
			final Cell firstNotToFlipC = getContainingCell(end);
			end = firstNotToFlipC._start;
			_current = firstNotToFlipC;

			final ArrayList<Double> lengthsList = new ArrayList<>();
			final ArrayList<Boolean> pinksList = new ArrayList<>();
			for (Cell c = startC; c != firstNotToFlipC; c = c._nxt) {
				lengthsList.add(c.computeLength());
				pinksList.add(c._pink);
			}
			final int nToFlip = lengthsList.size();
			int k = 0;
			for (Cell c = startC; c != firstNotToFlipC; c = c._nxt) {
				final int kX = nToFlip - 1 - k;
				c._pink = !pinksList.get(kX);
				final double lengthX = lengthsList.get(kX);
				if (k < nToFlip - 1) {
					c._nxt._start = (c._start + lengthX) % 1d;
				}
				++k;
			}
			/** Kill startC if its pink value matches its predecessor's. */
			if (true) {
			} else {
				final Cell pvsC = startC._pvs;
				if (pvsC._pink == startC._pink) {
					final Cell firstNxtC = startC._nxt;
					firstNxtC._pvs = pvsC;
					pvsC._nxt = firstNxtC;
				}
			}
			/** Check if we're done. */
			if (_current._pink) {
				boolean fail = false;
				for (Cell c = _current._nxt; c != _current; c = c._nxt) {
					if (c._pink != _current._pink) {
						fail = true;
						break;
					}
				}
				if (!fail) {
					return nFlips + 1;
				}
			}
			start = end;
		}
	}

	private static boolean areWithinEps(double d0, double d1) {
		if (d0 > d1) {
			final double d = d0;
			d0 = d1;
			d1 = d;
		}
		return d1 - d0 <= _Eps || 1d - d1 + d0 <= _Eps;
	}

	public String getString() {
		final String f = "%s  %d. %s";
		String s = String.format(f, //
				"", 0, _current.getString());
		int k = 1;
		for (Cell c = _current._nxt; c != _current; c = c._nxt) {
			s += String.format(f, "\n", k++, c.getString());
		}
		return s;
	}

	@Override
	public String toString() {
		return getString();
	}

	public static void main(final String[] args) {
		final String[] cannedStrings = {
				"Print", //
				"", //
		};
		cannedStrings[1] = //
				String.format("Cycle %.12f %.12f %.12f", //
						1d / 10d, 1d / 14d, Math.sqrt(1d / 16d));
		final int nCanned = cannedStrings.length;
		final Cake cake = new Cake();
		try (final Scanner sc = new Scanner(System.in)) {
			for (int iTest = 0;; ++iTest) {
				int nFlips = -1;
				if (iTest > 0) {
					System.out.println();
				}
				final String s;
				if (iTest < nCanned) {
					s = cannedStrings[iTest].toUpperCase();
				} else {
					System.out.printf("%d. Enter Q(uit), " + //
							" P(rint)," + //
							" CY(cle) <len1> <len2> ...", //
							iTest);
					s = sc.nextLine().toUpperCase();
				}
				if (s.startsWith("Q")) {
					break;
				} else if (s.startsWith("P")) {
				} else {
					final String[] fields = s.trim().split("[\\s,\\[\\]]+");
					final int nFields = fields.length;
					if (s.startsWith("CY")) {
						final ArrayList<Double> lengthsList = new ArrayList<Double>();
						for (int k = 1; k < nFields; ++k) {
							try {
								lengthsList.add(Double.parseDouble(fields[k]));
							} catch (final NumberFormatException e) {
							}
						}
						final int nLengths = lengthsList.size();
						final double[] lengths = new double[nLengths];
						for (int k = 0; k < nLengths; ++k) {
							lengths[k] = lengthsList.get(k);
						}
						nFlips = cake.countFlips(lengths);
					}
				}
				if (nFlips < 0) {
					System.out.printf("%s\n%s\n", s, cake.getString());
				} else {
					System.out.printf("%s\n     nFlips[%d]\n%s\n", s, nFlips, cake.getString());
				}
			}
		} catch (final Exception e) {
		}
	}
}
