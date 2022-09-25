package com.euler.tdk.euler0566.tmk;

import java.util.ArrayList;
import java.util.Scanner;

/** Assume range is [0d,1d). */
public class Cake {
	final private static double _Eps = 1.0e-10;
	final private static double _OneMinusHalfEps = 1d - _Eps / 2d;

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
			return String.format("start[%.3f] length[%.3f],%s", _start, computeLength(), _pink ? "PINK" : "GRAY");
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

	/** Merges c with c._pvs if they're the same color. */
	private static Cell mergeCellToPvs(final Cell c) {
		final Cell pvs = c._pvs;
		if (pvs == c) {
			return c;
		}
		if (pvs._pink != c._pink) {
			return null;
		}
		final Cell nxt = c._nxt;
		pvs._nxt = nxt;
		nxt._pvs = pvs;
		if (pvs._nxt == pvs) {
			pvs._start = 0d;
		}
		return pvs;
	}

	static ArrayList<Cell> gatherCellsOfStretch(final Cell cell, final double start, final double end) {
		final ArrayList<Cell> cells = new ArrayList<>();
		for (Cell c = cell;; c = c._nxt) {
			if (c.contains(start)) {
				cells.add(c.splitAt(start));
				break;
			}
		}
		for (Cell c = cells.get(0);; c = c._nxt) {
			if (areWithinEps(c._start, end)) {
				break;
			}
			cells.add(c);
			if (c.contains(end)) {
				c.splitAt(end);
				break;
			}
		}
		return cells;
	}

	Cell flipStretch(final double start, final double end) {
		return flipStretch(_current, start, end);
	}

	static Cell flipStretch(final Cell cell, final double start, final double end) {
		final ArrayList<Cell> toFlip = gatherCellsOfStretch(cell, start, end);
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
			c._pink = toFlip.get(kX)._pink;
			if (k < nToFlip - 1) {
				toFlip.get(k + 1)._start = convertTo01(c._start + lengths[kX]);
			}
		}
		return toFlip.get(nToFlip - 1)._nxt;
	}

	Cell colorStretch(final double start, final double end, final boolean pink) {
		return colorStretch(_current, start, end, pink);
	}

	static Cell colorStretch(final Cell cell, final double start, final double end, final boolean pink) {
		final ArrayList<Cell> toColor = gatherCellsOfStretch(cell, start, end);
		final int nToColor = toColor.size();
		for (int k = 0; k < nToColor; ++k) {
			toColor.get(k)._pink = pink;
		}
		return toColor.get(nToColor - 1)._nxt;
	}

	void consolidate() {
		final double start = _current._start;
		boolean movedOn = false;
		for (Cell c = _current;;) {
			final Cell pvs = c._pvs;
			if (pvs == c) {
				_current = c;
				_current._start = 0d;
				return;
			}
			if (pvs._pink == c._pink) {
				pvs._nxt = c._nxt;
				c._nxt._pvs = pvs;
			} else {
				movedOn = true;
			}
			if (movedOn && pvs.contains(start)) {
				break;
			}
			c = pvs;
		}
	}

	private boolean forward() {
		_current = _current._nxt;
		return true;
	}

	private boolean back() {
		_current = _current._pvs;
		return true;
	}

	private boolean splitCurrentAt(final double start) {
		final Cell c = _current.splitAt(start);
		if (c == null) {
			return false;
		}
		_current = c;
		return true;
	}

	private boolean mergeCurrentToPvs() {
		final Cell c = mergeCellToPvs(_current);
		if (c == null) {
			return false;
		}
		_current = c;
		return true;
	}

	private static double convertTo01(final double d) {
		double dd;
		if (d >= 0d) {
			dd = d % 1d;
		} else {
			dd = 1d - (-d % 1d);
		}
		return dd >= _OneMinusHalfEps ? 0d : dd;
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

	public static void main(final String[] args) {
		final Cake cake = new Cake();
		try (final Scanner sc = new Scanner(System.in)) {
			ITEST_LOOP: for (int iTest = 0;; ++iTest) {
				if (iTest > 0) {
					System.out.println();
				}
				/** Convenient to have boolean response. */
				boolean response = false;
				switch (iTest) {
				case 0:
					response = true;
					break;
				case 1:
					response = cake.splitCurrentAt(0d);
					break;
				case 2:
					response = cake.splitCurrentAt(0.5);
					break;
				default:
					System.out.printf("%d. Enter Q(uit), " + //
							"FO(rward), B(ack), M(erge to pvs)," + //
							" \"S(plit at) <d>\"," + //
							" \"FL(ip) <start> <end>\", or" + //
							" \"C(olor) <start> <end> <Pink or Gray>\": ", //
							iTest);
					final String s = sc.nextLine().toUpperCase();
					if (s.startsWith("Q")) {
						break ITEST_LOOP;
					} else if (s.startsWith("FO")) {
						response = cake.forward();
					} else if (s.startsWith("B")) {
						response = cake.back();
					} else if (s.startsWith("M")) {
						response = cake.mergeCurrentToPvs();
					} else {
						final String[] fields = s.trim().split("[\\s,\\[\\]]+");
						final int nFields = fields.length;
						if (nFields < 2) {
							response = false;
						} else {
							final double start = convertTo01(Double.parseDouble(fields[1]));
							if (s.startsWith("S")) {
								boolean responseX = false;
								try {
									responseX = cake.splitCurrentAt(start);
								} catch (final Exception e) {
								}
								response = responseX;
							} else if (nFields < 3) {
								response = false;
							} else {
								final double end = convertTo01(Double.parseDouble(fields[2]));
								if (s.startsWith("FL")) {
									response = cake.flipStretch(start, end) != null;
								} else if (nFields < 4) {
									response = false;
								} else {
									final boolean pink = fields[3].toUpperCase().startsWith("P");
									response = cake.colorStretch(start, end, pink) != null;
								}
							}
						}
					}
				}
				System.out.printf("Response[%b]\n%s\n", response, cake.getString());
			}
		} catch (final Exception e) {
		}
	}

}
