package analysis.retvalues;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;



public class MethodVisitorImpl extends MethodAdapter {
	String owner;
	MethodNode node;
	
	final ClassVistorImpl parent;
	final List<String> retValues = new ArrayList<String>();
	final String name;
	
	public MethodVisitorImpl(String owner, int access, String name,
			String desc, ClassVistorImpl parent) {
			super(new MethodNode(access, name, desc, null, null));
			this.name = name;
			this.owner = owner;
			this.node = (MethodNode)mv;
			this.parent = parent;
	}
	
	@Override
	public void visitEnd() {
		Analyzer a = new Analyzer(new Interpreter(this));
		try {
			a.analyze(owner, node);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		parent.addComputedRetValues(name, retValues);
	}
	
}
