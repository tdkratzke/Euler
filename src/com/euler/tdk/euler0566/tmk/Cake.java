package com.euler.tdk.euler0566.tmk;

import java.util.ArrayList;
import java.util.Scanner;

/** Assume range is [0d,1d). */
public class Cake {
	final private static double _Eps = 1.0e-10;
	final private static double _OneMinusEps = 1d - _Eps;

	public class Cell {
		double _start;
		boolean _pink;
		Cell _pvs, _nxt;

		public Cell(final double start, final boolean pink) {
			_start = start;
			_pink = pink;
			_pvs = _nxt = null;
		}

		public boolean contains(final double d) {
			final double nxtStart = _nxt._start;
			if (_start < nxtStart) {
				return _start <= d && d < nxtStart;
			}
			return _start <= d || d < nxtStart;
		}

		/** Returns the Cell that starts at start. */
		private Cell splitAt(final double start) {
			if (!contains(start)) {
				return null;
			}
			if (areWithinEps(_start, start)) {
				return this;
			}
			final Cell newC = new Cell(start, _pink);
			_nxt._pvs = newC;
			newC._nxt = _nxt;
			_nxt = newC;
			newC._pvs = this;
			return newC;
		}

		public double computeLength() {
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

	Cell flip(final double start, final double end) {
		Cell startC = null;
		for (Cell c = _current;; c = c._nxt) {
			if (c.contains(start)) {
				startC = c.splitAt(start);
				break;
			}
		}
		final ArrayList<Cell> toFlip = new ArrayList<>();
		if (startC.contains(end)) {
			toFlip.add(startC.splitAt(end)._pvs);
		} else {
			toFlip.add(startC);
			for (Cell c = startC;; c = c._nxt) {
				if (c.contains(end)) {
					toFlip.add(c.splitAt(end)._pvs);
					break;
				}
				toFlip.add(c);
			}
		}
		final int nToFlip = toFlip.size();
		final double[] lengths = new double[nToFlip];
		final boolean[] pinks = new boolean[nToFlip];
		for (int k = 0; k < nToFlip; ++k) {
			final Cell c = toFlip.get(k);
			lengths[k] = convertTo01(c._nxt._start - c._start);
			pinks[k] = c._pink;
		}

		for (int k = 0; k < nToFlip; ++k) {
			final int kX = nToFlip - 1 - k;
			final Cell c = toFlip.get(k);
			c._pink = !pinks[kX];
			if (k < nToFlip - 1) {
				toFlip.get(k + 1)._start = convertTo01(c._start + lengths[kX]);
			}
		}
		final Cell nxt = toFlip.get(nToFlip - 1)._nxt;
		return nxt;
	}

	void setColor(final double start, final double end, final boolean pink) {
		Cell startC = null;
		for (startC = _current;; startC = startC._nxt) {
			if (startC.contains(start)) {
				break;
			}
		}
		Cell pvsC = null;
		if (startC._pink != pink) {
			startC = startC.splitAt(start);
			pvsC = startC._pvs;
		} else {
			for (pvsC = startC._pvs;; pvsC = pvsC._pvs) {
				if (pvsC._pink != pink) {
					break;
				}
				if (pvsC == startC) {
					_current._pvs = _current._nxt = _current;
					_current._start = 0d;
					return;
				}
			}
		}
		Cell nxtC = null;
		for (nxtC = startC;; nxtC = nxtC._nxt) {
			if (nxtC.contains(end)) {
				break;
			}
		}
		if (nxtC._pink != pink) {
			nxtC = nxtC.splitAt(end);
		} else {
			for (;; nxtC = nxtC._nxt) {
				if (nxtC._pink != pink) {
					break;
				}
				if (nxtC == startC) {
					_current._pvs = _current._nxt = _current;
					_current._start = 0d;
					return;
				}
			}
		}
		final Cell pinkC = pvsC._nxt;
		pinkC._pink = pink;
		pinkC._nxt = nxtC;
		nxtC._pvs = pinkC;
		if (nxtC._nxt == pvsC && nxtC._pink == pvsC._pink) {
			pvsC._nxt._pvs = nxtC;
			nxtC._nxt = pvsC._nxt;
		}
		_current = nxtC;
	}

	private static double convertTo01(final double d) {
		double dd;
		if (d >= 0d) {
			dd = d % 1d;
		} else {
			dd = 1d - (-d % 1d);
		}
		return dd >= _OneMinusEps ? 0d : dd;
	}

	private static boolean areWithinEps(double d0, double d1) {
		if (d0 > d1) {
			final double d = d0;
			d0 = d1;
			d1 = d;
		}
		if (d1 - d0 <= _Eps) {
			return true;
		}
		if (d0 <= _Eps || d1 >= 1d - _Eps) {
			return (1d - d1) + d0 <= _Eps;
		}
		return false;
	}

	public String getString() {
		String s = "";
		int k = 0;
		for (Cell cell = _current;; cell = cell._nxt) {
			if (k > 0 && cell == _current) {
				return s;
			}
			s += String.format("%s  %d. %s", //
					k == 0 ? "" : "\n", k++, cell.getString());
		}
	}

	@Override
	public String toString() {
		return getString();
	}

	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		final String[] cannedStrings01 = {
				"ZZ", //
				"C 0.25 0.75 Gray", //
				"C 0.8 0.2 Gray", //
				"C 0.25 0.8 G", //
				"C 0.5 0.8 PINK" //
		};
		final String[] cannedStrings = {
				"ZZ", //
				"F 0.2 0.8", //
				"F 0.4 0.6", //
		};
		final int nCanned = cannedStrings.length;
		final Cake cake = new Cake();
		try (final Scanner sc = new Scanner(System.in)) {
			for (int iTest = 0;; ++iTest) {
				if (iTest > 0) {
					System.out.println();
				}
				final String s;
				if (iTest < nCanned) {
					s = cannedStrings[iTest];
				} else {
					System.out.printf("%d. Enter Q(uit), " + //
							" \"S(plit at) <d>\"," + //
							" \"F(lip) <start> <end>\", or" + //
							" \"C(olor) <start> <end> <Pink or Gray>\": ", //
							iTest);
					s = sc.nextLine().toUpperCase();
				}
				if (s.startsWith("Q")) {
					break;
				} else if (s.startsWith("Z")) {
				} else {
					final String[] fields = s.trim().split("[\\s,\\[\\]]+");
					final int nFields = fields.length;
					if (nFields < 2) {
					} else {
						final double start = convertTo01(Double.parseDouble(fields[1]));
						if (s.startsWith("S")) {
							try {
								cake._current.splitAt(start);
							} catch (final Exception e) {
							}
						} else if (nFields < 3) {
						} else {
							final double end = convertTo01(Double.parseDouble(fields[2]));
							if (s.startsWith("F")) {
								cake.flip(start, end);
							} else if (nFields < 4) {
							} else {
								final boolean pink = fields[3].toUpperCase().startsWith("P");
								cake.setColor(start, end, pink);
							}
						}
					}
				}
				System.out.printf("s[%s]\n%s\n", s, cake.getString());
			}
		} catch (final Exception e) {
		}
	}

}
