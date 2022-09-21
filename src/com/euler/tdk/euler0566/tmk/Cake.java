package com.euler.tdk.euler0566.tmk;

/** Assume range is [0d,1d). */
public class Cake {
	final private static double _Eps = 1.0e-10;
	final private static double _HalfEps = 0.5d * _Eps;
	final private static double _Big = 1d - _HalfEps;
	private Interval _current;

	public Cake() {
		_current = null;
	}

	public boolean insertAfterCurrent(double start, final boolean pink) {
		start = convertTo01(start);
		if (_current == null) {
			final Interval interval = new Interval(0d, pink);
			interval._pvs = interval._nxt = _current = interval;
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
		final boolean currentWraps = nxtStart < crrntStart;
		if (!currentWraps) {
			if (start < crrntStart || start > nxtStart) {
				return false;
			}
		} else {
			if (start < crrntStart || start > nxtStart) {
				return false;
			}
		}
		final Interval interval = new Interval(start, pink);
		_current._nxt = nxt._pvs = interval;
		interval._pvs = _current;
		interval._nxt = nxt;
		_current = interval;
		return true;
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
		return dd >= _Big ? 0d : dd;
	}

	private static boolean isSameAs(final double d, final double dd) {
		if (Math.abs(d - dd) <= _HalfEps) {
			return true;
		}
		if (d >= _Big && dd <= _HalfEps) {
			return true;
		}
		if (dd >= _Big && d <= _HalfEps) {
			return true;
		}
		return false;
	}

	public String getString() {
		if (_current == null) {
			return "No Intervals";
		}
		String s = "";
		for (Interval i = _current;; i = i._nxt) {
			s += String.format("\n\t%s", i.getString());
			i = i._nxt;
			if (i == _current) {
				return s;
			}
		}
	}

	@Override
	public String toString() {
		return getString();
	}

	public static void main(final String[] args) {
		final double x = convertTo01(-2d + 1.0e-10);
		final double y = (2d - 1.0e-10) % 1d;
		final int z = 0;
	}
}
