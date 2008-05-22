package analysis;

import java.util.List;

import org.objectweb.asm.Type;

public class MethodInfo {
	private final String method;
	private final String[] parameterTypes;
	private final String filename;
	private final int firstLine;
	private final List<String> retValues;

	public MethodInfo(String method, String descriptor, List<String> retValues, String filename, int firstLine) {
		super();
		this.method = method;
		this.retValues = retValues;
		this.filename = filename;
		this.firstLine = firstLine;

		Type [] types = Type.getArgumentTypes(descriptor);
		parameterTypes = new String[types.length];
		int i = 0;
		for(Type type : types) {
			parameterTypes[i++] = type.getClassName();
		}
	}

	public String getMethod() {
		return method;
	}

	public List<String> getRetValues() {
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
