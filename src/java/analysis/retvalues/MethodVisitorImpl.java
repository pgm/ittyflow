package analysis.retvalues;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;



public class MethodVisitorImpl extends MethodAdapter {
	String owner;
	MethodNode node;
	
	final ClassVistorImpl parent;
	final List<String> retValues = new ArrayList<String>();
	final String name;
	final String descriptor;
	
	boolean hasSeenLineNumber = false;
	int minLineNumber;
	
	public MethodVisitorImpl(String owner, int access, String name,
			String descriptor, ClassVistorImpl parent) {
			super(new MethodNode(access, name, descriptor, null, null));
			this.name = name;
			this.owner = owner;
			this.node = (MethodNode)mv;
			this.parent = parent;
			this.descriptor = descriptor;
	}

	
	@Override
	public void visitEnd() {
		Analyzer a = new Analyzer(new Interpreter(this));
		try {
			a.analyze(owner, node);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		parent.addComputedRetValues(name, descriptor, retValues, minLineNumber);
	}

	@Override
	public void visitLineNumber(int lineNo, Label label) {
		if(!hasSeenLineNumber || minLineNumber < lineNo)
			minLineNumber = lineNo;
		super.visitLineNumber(lineNo, label);
	}
	
}
