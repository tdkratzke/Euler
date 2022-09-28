package com.euler.tdk.euler0566.tmk;

import java.util.ArrayList;
import java.util.Scanner;

/** Assume range is [0d,1d). */
public class Cake {
	final private static double _Eps = 1.0e-7;
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
			if (areWithinEps(_start, d)) {
				return true;
			}
			final double nxtStart = _nxt._start;
			if (areWithinEps(nxtStart, d)) {
				return false;
			}
			if (_start < nxtStart) {
				return _start <= d && d < nxtStart;
			}
			return _start <= d || d < nxtStart;
		}

		/** Returns the Cell that starts at start. */
		private Cell splitAt(final double start) {
			if (areWithinEps(_start, start)) {
				return this;
			}
			if (!contains(start)) {
				return null;
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

	void flip(final double startX, final double endX) {
		final double start = convertTo01(startX);
		final double end = convertTo01(endX);
		if (areWithinEps(start, end)) {
			if (startX != endX) {
				for (Cell c = _current;;) {
					c._pink = !c._pink;
					c = c._nxt;
					if (c == _current) {
						break;
					}
				}
			}
			return;
		}
		final Cell firstC;
		for (Cell c = _current;; c = c._nxt) {
			if (c.contains(start)) {
				firstC = c.splitAt(start);
				break;
			}
		}
		final Cell firstNotToFlipC;
		for (Cell c = firstC;; c = c._nxt) {
			if (c.contains(end)) {
				firstNotToFlipC = c.splitAt(end);
				break;
			}
		}
		final ArrayList<Cell> toFlipList = new ArrayList<>();
		for (Cell c = firstC; c != firstNotToFlipC; c = c._nxt) {
			toFlipList.add(c);
		}
		final int nToFlip = toFlipList.size();
		final double[] lengths = new double[nToFlip];
		final boolean[] pinks = new boolean[nToFlip];
		for (int k = 0; k < nToFlip; ++k) {
			final Cell c = toFlipList.get(k);
			lengths[k] = c.computeLength();
			pinks[k] = c._pink;
		}

		for (int k = 0; k < nToFlip; ++k) {
			final int kX = nToFlip - 1 - k;
			final Cell c = toFlipList.get(k);
			c._pink = !pinks[kX];
			if (k < nToFlip - 1) {
				toFlipList.get(k + 1)._start = convertTo01(c._start + lengths[kX]);
			}
		}
		final Cell pvsC = firstC._pvs;
		if (pvsC._pink == firstC._pink) {
			final Cell firstNxtC = firstC._nxt;
			firstNxtC._pvs = pvsC;
			pvsC._nxt = firstNxtC;
		}
		final Cell lastC = firstNotToFlipC._pvs;
		if (lastC._pink == firstNotToFlipC._pink) {
			final Cell nxtC = firstNotToFlipC._nxt;
			lastC._nxt = nxtC;
			nxtC._pvs = lastC;
			_current = lastC;
		} else {
			_current = firstNotToFlipC;
		}
	}

	boolean isUniColored() {
		for (Cell c = _current._nxt; c != _current; c = c._nxt) {
			if (c._pink != _current._pink) {
				return false;
			}
		}
		return true;
	}

	boolean isWeird() {
		boolean haveSeenCurrent = false;
		boolean haveWrapped = false;
		for (Cell c = _current;; c = c._nxt) {
			if (c == _current) {
				if (haveSeenCurrent) {
					return false;
				}
				haveSeenCurrent = true;
			}
			if (c._nxt._start < c._start) {
				if (haveWrapped) {
					return true;
				}
				haveWrapped = true;
			}
			final double len = c.computeLength();
			if (len < 10d * _Eps) {
				return true;
			}
		}
	}

	int countFlips(final double[] lengths, final int maxNFlips) {
		final int nLengths = lengths.length;
		double start = 0d;
		for (int nFlips = 1;; ++nFlips) {
			final double length = lengths[nFlips % nLengths];
			final double end = convertTo01(start + length);
			flip(start, end);
			if ((isUniColored() && _current._pink) || //
					(maxNFlips > 0 && nFlips >= maxNFlips)) {
				return nFlips;
			}
			if (isWeird()) {
				return -1;
			}
			start = end;
		}
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

	static double _minAreDiff = 1d;

	private static boolean areWithinEps(double d0, double d1) {
		if (d0 > d1) {
			final double d = d0;
			d0 = d1;
			d1 = d;
		}
		final double delta0 = d1 - d0;
		if (delta0 <= _Eps) {
			return true;
		}
		final double delta1 = 1d - d1 + d0;
		if (delta1 <= _Eps) {
			return true;
		}
		final double delta = Math.min(delta0, delta1);
		if (delta < _minAreDiff) {
			System.out.printf("\nminAreDiff:%.12f->%.12f", _minAreDiff, delta);
			_minAreDiff = delta;
		}
		return false;
	}

	public String getString() {
		final String f = "%s  %d. %s";
		String s = String.format(f, //
				"", 0, _current.getString());
		int k = 1;
		for (Cell cell = _current._nxt; cell != _current; cell = cell._nxt) {
			s += String.format(f, //
					"\n", k++, cell.getString());
		}
		return s;
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
		final String[] cannedStrings02 = {
				"ZZ", //
				"F 0.2 0.8", //
				"F 0.4 0.6", //
		};
		final String[] cannedStrings = {
				"ZZ", //
				"", //
		};
		cannedStrings[1] = String.format("CYcle with max -1 %.12f %.12f %.12f", //
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
							" S(plit at) <d>," + //
							" F(lip) <start> <end>, or" + //
							" CO(lor) <start> <end> <Pink or Gray>: ", //
							" CY(cle) <len1> <len2> ...", //
							" CY(cle with Max) <maxNFlips> <len1> <len2> ...", //
							iTest);
					s = sc.nextLine().toUpperCase();
				}
				if (s.startsWith("Q")) {
					break;
				} else if (s.startsWith("Z")) {
				} else {
					final String[] fields = s.trim().split("[\\s,\\[\\]]+");
					final int nFields = fields.length;
					if (s.startsWith("CY")) {
						final int doublesStartAt, maxNFlips;
						if (fields[1].equalsIgnoreCase("with")) {
							doublesStartAt = 4;
							maxNFlips = Integer.parseInt(fields[3]);
						} else {
							doublesStartAt = 1;
							maxNFlips = -1;
						}
						final ArrayList<Double> lengthsList = new ArrayList<Double>();
						for (int k = doublesStartAt; k < nFields; ++k) {
							try {
								final Double d = Double.parseDouble(fields[k]);
								if (0d < d && d < 1d) {
									lengthsList.add(d);
								}
							} catch (final NumberFormatException e) {
							}
						}
						final int nLengths = lengthsList.size();
						final double[] lengths = new double[nLengths];
						for (int k = 0; k < nLengths; ++k) {
							lengths[k] = lengthsList.get(k);
						}
						nFlips = cake.countFlips(lengths, maxNFlips);
					} else {
						final double start = convertTo01(Double.parseDouble(fields[1]));
						if (s.startsWith("S")) {
							try {
								cake._current.splitAt(start);
							} catch (final Exception e) {
							}
						} else {
							final double end = convertTo01(Double.parseDouble(fields[2]));
							if (s.startsWith("F")) {
								cake.flip(start, end);
							} else if (s.startsWith("CO")) {
								final boolean pink = fields[3].toUpperCase().startsWith("P");
								cake.setColor(start, end, pink);
							}
						}
					}
				}
				if (nFlips > 0) {
					System.out.printf("s[%s]\n     nFlips[%d]\n%s\n", s, nFlips, cake.getString());
				} else {
					System.out.printf("s[%s]\n%s\n", s, cake.getString());
				}
			}
		} catch (

		final Exception e) {
		}
	}

}
