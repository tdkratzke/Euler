package com.euler.tdk.euler0566.tmk;

public class Interval {
	final double _start;
	boolean _pink;
	Interval _pvs, _nxt;

	public Interval(final double start, final boolean pink) {
		_start = start;
		_pink = pink;
		_pvs = _nxt = null;
	}

	public String getString() {
		return String.format("%.3f,%s", _start, _pink ? "PINK" : "GRAY");
	}

	@Override
	public String toString() {
		return getString();
	}
}
