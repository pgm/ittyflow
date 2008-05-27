package com.github.ittyflow.analysis.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.ittyflow.analysis.AnalysisUtil;
import com.github.ittyflow.analysis.MethodInfo;


public class TestAnalysisUtil {

	@Test
	public void testGetReturnValues() {
		List<MethodInfo> methods = AnalysisUtil.getMethodsAndReturnValues(SampleClass.class);
		assertEquals(4, methods.size());
		
		Map<String, MethodInfo> map = new HashMap<String, MethodInfo>();
		for(MethodInfo method : methods) {
			map.put(method.getMethod(), method);
		}
		
		MethodInfo method = map.get("returnsOneValue");
		assertHasReturnValues(method, new String[] {"value1"});
		
		method = map.get("returnsOneOfTwoValues");
		assertHasReturnValues(method, new String[] {"value1", "value2"});

		method = map.get("returnsValueOrNull");
		assertHasReturnValues(method, new String[] {"value1", null});
		
		method = map.get("returnsValueAndUnknown");
		assertHasReturnValues(method, new String[] {"value1", "unknown"});
	}

	public void assertHasReturnValues(MethodInfo method, String [] values) {
		for(String value : values) {
			assertHasReturnValue(method, value);
		}
		assertEquals(values.length, method.getRetValues().size()) ;
	}
	
	public void assertHasReturnValue(MethodInfo method, String value) {
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
