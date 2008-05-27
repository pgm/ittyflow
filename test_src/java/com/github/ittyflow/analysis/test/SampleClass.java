package com.github.ittyflow.analysis.test;

public class SampleClass {
	static final Object value1 = new Object();
	static final Object value2 = new Object();
	
	public Object returnsOneValue() {
		return value1;
	}
	
	public Object returnsOneOfTwoValues(boolean which) {
		if(which) {
			return value1;
		} else {
			return value2;
		}
	}
	
	public Object returnsValueOrNull(boolean which) {
		if(which) {
			return value1;
		} else {
			return null;
		}
	}
	
	public Object returnsValueAndUnknown(String v) {
		if(v == null) {
			return value1;
		}
		
		return Integer.parseInt(v);
	}
}
