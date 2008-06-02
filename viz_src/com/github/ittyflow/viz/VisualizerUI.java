package com.github.ittyflow.viz;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import com.github.ittyflow.Workflow;
import com.github.ittyflow.Workflow.TargetedMethod;
import com.github.ittyflow.analysis.AsmClassIntrospector;
import com.github.ittyflow.analysis.MethodInfo;
import com.github.ittyflow.util.Visualizer;

import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class VisualizerUI implements Visualizer {

	AsmClassIntrospector introspector = new AsmClassIntrospector();
	
	public static Object lookupRetValue(String name) {
		int pos = name.lastIndexOf(".");
		String classPath = name.substring(0, pos);
		String fieldName = name.substring(pos+1);
		String className = classPath.replace("/", ".");
		Object f;
		try {
			Class<?> c = VisualizerUI.class.getClassLoader().loadClass(className);
			f = c.getField(fieldName).get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return f;
	}
	
	public static class MethodEdge {
		MethodInfo info;
		
		public MethodEdge(MethodInfo info) {
			this.info = info;
		}
		
		public String toString() {
			return info.getMethod();
		}
		
		public String getDetails() {
			StringBuilder sb = new StringBuilder();
			for(String p: info.getParameterTypes()) {
				if(sb.length() > 0)
					sb.append(",\n\t");
				sb.append(p);
			}
			return info.getMethod()+"("+sb+")"+"\n"+info.getFilename()+":"+info.getFirstLine();
		}
	}
	
	public <W,T> void visualize(Workflow<W,T>workflow) {
		WorflowGraph g = new WorflowGraph();

		Map <W, StateDescriptor> xlat = new HashMap<W, StateDescriptor>();
		
		for(W state : workflow.getStates() ) {
			StateDescriptor desc = new StateDescriptor(state.toString());
			xlat.put(state, desc);
			g.addVertex(desc);
		}

		for(W state : workflow.getStates() ) {
			Collection<TargetedMethod<T>> targetedMethods = workflow.getTransitionsForState(state);
			for(TargetedMethod<T> tm : targetedMethods) {
				MethodInfo methodInfo = introspector.getMethodInfo(tm.getTarget().getClass(), tm.getMethod());

				for(String retValue : methodInfo.getRetValues() ) {
					if(retValue == null)
						continue;
					
					W destState = (W)lookupRetValue(retValue);
					
//					System.out.println("adding "+xlat.get(state) + "->" + xlat.get(destState));
					
					g.addEdge(new MethodEdge(methodInfo), xlat.get(state), xlat.get(destState));
				}
			}
		}
		
		final PersistedLayout<StateDescriptor, MethodEdge> layout =  new PersistedLayout<StateDescriptor, MethodEdge>(g);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		layout.read();
		
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		final VisualizationViewer<StateDescriptor,MethodEdge> vv = new VisualizationViewer<StateDescriptor,MethodEdge>(layout);
		
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setVertexShapeTransformer(new ConstantTransformer(new Rectangle2D.Float(-130,-10,260,20)));
		
		vv.getRenderer().setVertexLabelRenderer(new BasicVertexLabelRenderer<StateDescriptor,MethodEdge>(Position.CNTR));
		vv.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
		vv.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<StateDescriptor,MethodEdge>(0.5, 0.5));
		vv.getRenderContext().setVertexFillPaintTransformer(new ConstantTransformer(new Color(255, 230, 200)));
		vv.getRenderContext().setVertexLabelTransformer(new Transformer<StateDescriptor, String>() {
			public String transform(StateDescriptor i) {
				return i.toString();
			}
		}
		);
		
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<MethodEdge, String>() {

			public String transform(MethodEdge i) {
				return i.toString();
			}
		});
		
		vv.setEdgeToolTipTransformer(new Transformer<MethodEdge,String>() {

			public String transform(MethodEdge edge) {
				StringBuilder sb = new StringBuilder();
				
				sb.append(edge.info.getFilename())
				  .append("(")
				  .append(edge.info.getFirstLine())
				  .append("):\n")
				  .append(edge.info.getMethod())
				  .append("(");
				boolean first = true;
				for(String param : edge.info.getParameterTypes()) {
					if(!first)
						sb.append(", ");
					first = false;
					sb.append(param);
				}
				sb.append(")");
				return sb.toString();
			}
			
		});
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		//.setPickSupport(new RadiusPickSupport());
		final DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);
		vv.addKeyListener(gm.getModeKeyListener());

        Container panel = new JPanel(new BorderLayout());
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        
        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.PAGE_AXIS));
        final JRadioButton pickButton = new JRadioButton("Pick"); 
        final JRadioButton transformButton = new JRadioButton("Transform"); 
        ButtonGroup group = new ButtonGroup();
        group.add(pickButton);
        group.add(transformButton);
        
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gzsp, controlPane);
		vv.setPreferredSize(new Dimension((int)screenSize.getWidth() - 200, (int)screenSize.getHeight() - 100));
		split.setDividerLocation(.95);
        panel.add(split);

        
        TitledBorder border = BorderFactory.createTitledBorder("Mouse mode");
        JComboBox box = gm.getModeComboBox();
        box.setBorder(border);
        controlPane.add(box);
        
        border = BorderFactory.createTitledBorder("Selection details");
        final JTextArea area = new JTextArea();
        controlPane.add(area);
        area.setBorder(border);
		JFrame frame = new JFrame("Workflow");
		frame.setLocationByPlatform(true);

		ItemListener selectionListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Set<StateDescriptor> states = vv.getPickedVertexState().getPicked();
				Set<MethodEdge> edges = vv.getPickedEdgeState().getPicked();

				if(states.size() == 0 && edges.size() == 0) {
						area.setText("No selection");
				} else if(states.size() == 1 && edges.size() == 0){
					area.setText(states.iterator().next().toString());
				} else if(states.size() == 0 && edges.size() == 1) {
					area.setText(edges.iterator().next().getDetails());
				} else {
					area.setText(""+edges.size()+" edges and "+states.size()+" states selected");
				}
			}
		};		
		
		vv.getPickedEdgeState().addItemListener(selectionListener);
		vv.getPickedVertexState().addItemListener(selectionListener);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("closed");
				layout.write();
				System.exit(0);
			}
		});
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
