package analysis.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import analysis.AnalysisUtil;
import analysis.MethodAndRetValues;

public class TestAnalysisUtil {

	@Test
	public void testGetReturnValues() {
		List<MethodAndRetValues> methods = AnalysisUtil.getMethodsAndReturnValues(SampleClass.class);
		assertEquals(4, methods.size());
		
		Map<String, MethodAndRetValues> map = new HashMap<String, MethodAndRetValues>();
		for(MethodAndRetValues method : methods) {
			map.put(method.getMethod(), method);
		}
		
		MethodAndRetValues method = map.get("returnsOneValue");
		assertHasReturnValues(method, new String[] {"value1"});
		
		method = map.get("returnsOneOfTwoValues");
		assertHasReturnValues(method, new String[] {"value1", "value2"});

		method = map.get("returnsValueOrNull");
		assertHasReturnValues(method, new String[] {"value1", null});
		
		method = map.get("returnsValueAndUnknown");
		assertHasReturnValues(method, new String[] {"value1", "unknown"});
	}

	public void assertHasReturnValues(MethodAndRetValues method, String [] values) {
		for(String value : values) {
			assertHasReturnValue(method, value);
		}
		assertEquals(values.length, method.getRetValues().size()) ;
	}
	
	public void assertHasReturnValue(MethodAndRetValues method, String value) {
		for(String curValue : method.getRetValues()) {
			if(curValue == null || value == null) {
				if (curValue == null && value == null)
					return;
				continue;
			}
			
			if(curValue.endsWith(value)) {
				return;
			}
		}
		throw new RuntimeException("Did not find value "+value);
	}
}
