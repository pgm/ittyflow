package sample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import analysis.AnalysisUtil;
import analysis.MethodAndRetValues;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import flow.Workflow;
import flow.viz.PersistedLayout;

public class VizMain {
	public static class MethodEdge {
		String name;
		
		public MethodEdge(String s) {
			name = s;
		}
		
		public String toString() {
			return name;
		}
	}

	public static AssayRunState lookupRetValue(String name) {
		int pos = name.lastIndexOf(".");
		String classPath = name.substring(0, pos);
		String fieldName = name.substring(pos+1);
		String className = classPath.replace("/", ".");
		Object f;
		try {
			Class c = VizMain.class.getClassLoader().loadClass(className);
			f = c.getField(fieldName).get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return (AssayRunState)f;
	}
	
	public static void main(String[] args) {
		Workflow<AssayRunState, ITransitions> workflow = AssayRunWorkflowFactory.makeWorkflow();
		
		DirectedSparseGraph<AssayRunState,MethodEdge> g = new DirectedSparseGraph<AssayRunState, MethodEdge>();

		for(AssayRunState state : workflow.getStates() ) {
			g.addVertex(state);
		}

		for(AssayRunState state : workflow.getStates() ) {
			for(ITransitions t : workflow.getTransitionSets(state)) {
				List<MethodAndRetValues> result = AnalysisUtil.getMethodsAndReturnValues(t.getClass());
				for(MethodAndRetValues marv : result) {
					for(String retValue : marv.getRetValues() ) {
						if(retValue == null)
							continue;
						AssayRunState destState = lookupRetValue(retValue);
						g.addEdge(new MethodEdge(marv.getMethod()), state, destState);
					}
				}
			}
		}
		
		final PersistedLayout<AssayRunState, MethodEdge> layout =  new PersistedLayout<AssayRunState, MethodEdge>(g);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		layout.read();
		
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		VisualizationViewer<AssayRunState,MethodEdge> vv = new VisualizationViewer<AssayRunState,MethodEdge>(layout);
		
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setVertexShapeTransformer(new Transformer<AssayRunState, Shape>() {
			public Shape transform(AssayRunState state) {
				return new Rectangle2D.Float(-130,-10,260,20);
			}
		});
		
		vv.getRenderer().setVertexLabelRenderer(new BasicVertexLabelRenderer<AssayRunState,MethodEdge>(Position.CNTR));
		vv.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
		vv.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<AssayRunState,MethodEdge>(0.5, 0.5));
		vv.getRenderContext().setVertexFillPaintTransformer(new ConstantTransformer(new Color(255, 230, 200)));
		vv.getRenderContext().setVertexLabelTransformer(new Transformer<AssayRunState, String>() {
			public String transform(AssayRunState i) {
				return i.toString();
			}
		}
		);
		
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<MethodEdge, String>() {

			public String transform(MethodEdge i) {
				return i.toString();
			}
		});
		
		vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		//.setPickSupport(new RadiusPickSupport());
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);
		vv.addKeyListener(gm.getModeKeyListener());

        Container panel = new JPanel(new BorderLayout());
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        panel.add(gzsp);
		
		JFrame frame = new JFrame("Workflow");
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("closing");
				super.windowClosing(e);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("deact");
				super.windowDeactivated(e);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("closed");
				layout.write();
				System.exit(0);
			}
		});
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
	
	}
}
