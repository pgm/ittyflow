package com.github.ittyflow.util;

import java.net.URL;
import java.net.URLClassLoader;

public class IsolationClassloader extends URLClassLoader {

	public IsolationClassloader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// check if already loaded
		Class<?> loadedClass = this.findLoadedClass(name);

		if(loadedClass == null) {
			// if not, try to load from our path
			try {
				loadedClass = this.findClass(name);
			} catch (ClassNotFoundException ex) {
				// if that fails, delegate to our parent.
				loadedClass = super.loadClass(name);
			}
		}
		
		return super.loadClass(name);
	}

}
