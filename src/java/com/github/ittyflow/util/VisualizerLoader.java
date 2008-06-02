package com.github.ittyflow.util;

import java.net.URL;

import com.github.ittyflow.Workflow;

public class VisualizerLoader {
	Visualizer visualizer;
	
	public VisualizerLoader(URL[] urls) {
		IsolationClassloader classLoader = new IsolationClassloader(urls, getClass().getClassLoader());
		
		try {
			visualizer = (Visualizer)classLoader.loadClass("com.github.ittyflow.viz.VisualizerUI").newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void visualize(Workflow<?,?> workflow) {
		visualizer.visualize(workflow);
	}
}
