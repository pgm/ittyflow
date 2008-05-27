package com.github.ittyflow.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;

import com.github.ittyflow.analysis.retvalues.ClassVistorImpl;



public class AnalysisUtil {
	private static String getPathToClass(Class <?> c) {
		return c.getName().replace(".", "/")+".class";
	}
	
	private static InputStream getClassAsInputStream(Class <?> c) {
		String classPath = getPathToClass(c);
		InputStream is = c.getClassLoader().getResourceAsStream(classPath);
		return is;
	}
	
	/**
	 * Returns a list of structures containing method names and for each method
	 * the returned values.  It does this by evaluating all paths and finding the 
	 * set of all possible return types.   If the return value comes from outside of the 
	 * method (ie: the return value from another method or an input parameter) then
	 * the value is returned as unknown.
	 * 
	 * @param c the class to crawl.
	 * @return A collection of {@link MethodInfo}s
	 */
	public static List<MethodInfo> getMethodsAndReturnValues(Class<?> c) {
		InputStream is = getClassAsInputStream(c);
		ClassReader cr;
		try {
			cr = new ClassReader(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		ClassVistorImpl v = new ClassVistorImpl();
		cr.accept(v, 0);
		
		return v.getRetValues();
	}

	static SoftReference<Map<Class<?>,List<MethodInfo>>> classCache = new SoftReference<Map<Class<?>,List<MethodInfo>>>(null);
	
	public static synchronized List<MethodInfo> getMethodsAndReturnValuesAndCache(Class<?> c) {
		Map<Class<?>,List<MethodInfo>> _classCache = classCache.get();
		if(_classCache == null) {
			_classCache = new HashMap<Class<?>,List<MethodInfo>>();
			classCache = new SoftReference<Map<Class<?>,List<MethodInfo>>>(_classCache);
		}
		List<MethodInfo> info = _classCache.get(c);
		if(info == null) {
			info = getMethodsAndReturnValues(c);
			_classCache.put(c, info);
		}
		return info;
	}
	
	public static MethodInfo getMethodInfo(Class<?> c, Method method) {
		List<MethodInfo> all = getMethodsAndReturnValuesAndCache(c);
		for(MethodInfo methodInfo : all) {
			if(methodInfo.getMethod().equals(method.getName()))
				return methodInfo;
		}
		throw new RuntimeException("no "+method.getName()+" in "+c.getName());
	}
}
