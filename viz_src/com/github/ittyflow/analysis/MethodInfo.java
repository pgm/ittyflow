package com.github.ittyflow.analysis;

import java.io.Serializable;


public class MethodInfo implements Serializable {
	private static final long serialVersionUID = 6956069667631111667L;

	private final String method;
	private final String[] parameterTypes;
	private final String filename;
	private final int firstLine;
	private final String[] retValues;

	public MethodInfo(String method, String[] parameterTypes, String[] retValues, String filename, int firstLine) {
		super();
		this.method = method;
		this.retValues = retValues;
		this.filename = filename;
		this.firstLine = firstLine;
		this.parameterTypes = parameterTypes;
	}

	public MethodInfo(String method, String[] parameterTypes, String[] retValues) {
		this.method = method;
		this.retValues = retValues;
		this.filename = null;
		this.firstLine = -1;
		this.parameterTypes = parameterTypes;
	}

	public String getMethod() {
		return method;
	}

	public String[] getRetValues() {
		return retValues;
	}
	
	public String [] getParameterTypes() {
		return parameterTypes;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public int getFirstLine() {
		return firstLine;
	}
}
