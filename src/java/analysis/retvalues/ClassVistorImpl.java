package analysis.retvalues;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import analysis.MethodAndRetValues;


public class ClassVistorImpl implements ClassVisitor {
	String className;
	List<MethodAndRetValues> retValues = new ArrayList<MethodAndRetValues>();

	public void addComputedRetValues(String name, List<String> rets) {
		retValues.add(new MethodAndRetValues(name, rets));
	}

	public List<MethodAndRetValues> getRetValues() {
		return retValues;
	}

	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		className = name;
	}

	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		return null;
	}

	public void visitAttribute(Attribute arg0) {
	}

	public void visitEnd() {
	}

	public FieldVisitor visitField(int arg0, String arg1, String arg2,
			String arg3, Object arg4) {
		return null;
	}

	public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String sig, String[] exceptions) {

		// ignore constructor
		if (name.equals("<init>") || name.equals("<clinit>"))
			return null;

		return new MethodVisitorImpl(className, access, name, desc, this);
	}

	public void visitOuterClass(String arg0, String arg1, String arg2) {
	}

	public void visitSource(String arg0, String arg1) {
	}

}
