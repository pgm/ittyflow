package analysis;

import java.util.List;

public class MethodAndRetValues {
	private final String method;
	private final List<String> retValues;

	public MethodAndRetValues(String method, List<String> retValues) {
		super();
		this.method = method;
		this.retValues = retValues;
	}

	public String getMethod() {
		return method;
	}

	public List<String> getRetValues() {
		return retValues;
	}
}
