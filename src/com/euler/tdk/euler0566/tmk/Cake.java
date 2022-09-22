package com.euler.tdk.euler0566.tmk;

import java.util.Scanner;

/** Assume range is [0d,1d). */
public class Cake {
	final private static double _Eps = 1.0e-10;
	final private static double _HalfEps = 0.5d * _Eps;
	final private static double _OneMinusHalfEps = 1d - _HalfEps;
	private Interval _current;

	public Cake() {
		_current = null;
	}

	public boolean insertAfterCurrent(double start, final boolean pink) {
		start = convertTo01(start);
		if (_current == null) {
			final Interval interval = new Interval(0d, pink);
			_current = interval._pvs = interval._nxt = interval;
			return true;
		}
		final double crrntStart = _current._start;
		if (isSameAs(crrntStart, start)) {
			_current._pink = pink;
			return false;
		}
		final Interval nxt = _current._nxt;
		final double nxtStart = nxt._start;
		if (isSameAs(start, nxtStart)) {
			nxt._pink = pink;
			return false;
		}
		if (_current != nxt) {
			final boolean currentWraps = nxtStart < crrntStart;
			if (!currentWraps) {
				if (start < crrntStart || start > nxtStart) {
					return false;
				}
			} else {
				if (start < crrntStart && start > nxtStart) {
					return false;
				}
			}
		}
		final Interval interval = new Interval(start, pink);
		_current._nxt = nxt._pvs = interval;
		interval._pvs = _current;
		interval._nxt = nxt;
		_current = interval;
		return true;
	}

	public boolean forward() {
		if (_current != null) {
			_current = _current._nxt;
			return true;
		}
		return false;
	}

	public boolean back() {
		if (_current != null) {
			_current = _current._pvs;
			return true;
		}
		return false;
	}

	public boolean removeCurrent() {
		if (_current == null) {
			return false;
		}
		final Interval nxt = _current._nxt;
		if (nxt == _current) {
			_current = null;
			return true;
		}
		final Interval pvs = _current._pvs;
		pvs._nxt = nxt;
		nxt._pvs = pvs;
		_current = nxt;
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

	private static boolean isSameAs(final double d, final double dd) {
		if (Math.abs(d - dd) <= _HalfEps) {
			return true;
		}
		if (d >= _OneMinusHalfEps && dd <= _HalfEps) {
			return true;
		}
		if (dd >= _OneMinusHalfEps && d <= _HalfEps) {
			return true;
		}
		return false;
	}

	public String getString() {
		if (_current == null) {
			return "No Intervals";
		}
		String s = "";
		int k = 0;
		for (Interval interval = _current;; interval = interval._nxt) {
			if (k > 0 && interval == _current) {
				return s;
			}
			s += String.format("%s  %d. %s", //
					k == 0 ? "" : "\n", k++, interval.getString());
		}
	}

	@Override
	public String toString() {
		return getString();
	}

	public static void main(final String[] args) {
		/*
		 * Because System.in will be closed when we use try with resources as in the
		 * following, the scanner must be built only once, and the iTest loop must go
		 * inside the try with resources block.
		 */
		final Cake cake = new Cake();
		cake.insertAfterCurrent(0d, true);
		cake.insertAfterCurrent(0.5, false);
		try (final Scanner sc = new Scanner(System.in)) {
			for (int iTest = 0;; ++iTest) {
				if (iTest > 0) {
					System.out.println();
				}
				System.out.printf("%d. Enter \"Add <start> <pink>\", " + //
						"FO(rward), B(ack), D(elete), or \"Flip <start> <end>\". (Q = quit): ", //
						iTest);
				final String s = sc.nextLine().toUpperCase();
				if (s.startsWith("Q")) {
					break;
				}
				final boolean response;
				if (s.startsWith("D")) {
					response = cake.removeCurrent();
				} else if (s.startsWith("FO")) {
					response = cake.forward();
				} else if (s.startsWith("B")) {
					response = cake.back();
				} else {
					final String[] fields = s.trim().split("[\\s,\\[\\]]+");
					final int nFields = fields.length;
					if (nFields < 3) {
						response = false;
					} else if (s.startsWith("A")) {
						boolean responseX = false;
						try {
							final double start = Double.parseDouble(fields[1]);
							final boolean pink = Boolean.parseBoolean(fields[2]);
							responseX = cake.insertAfterCurrent(start, pink);
						} catch (final Exception e) {
						}
						response = responseX;
					} else {
						response = false;
					}
				}
				System.out.printf("Response[%b]\n%s\n", response, cake.getString());
			}
		} catch (final Exception e) {
		}
	}
}
