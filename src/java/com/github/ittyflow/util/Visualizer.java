package com.github.ittyflow.util;

import com.github.ittyflow.Workflow;

public interface Visualizer {
	public  <W,T> void visualize(Workflow<W,T>workflow);

}
