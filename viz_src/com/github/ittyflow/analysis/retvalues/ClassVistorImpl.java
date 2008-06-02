package com.github.ittyflow.analysis.retvalues;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import com.github.ittyflow.analysis.AsmClassIntrospector;
import com.github.ittyflow.analysis.MethodInfo;




public class ClassVistorImpl implements ClassVisitor {
	String className;
	List<MethodInfo> retValues = new ArrayList<MethodInfo>();

	protected String getFilenameForClass() {
		String filteredClassName = className;
		int dollarIndex = filteredClassName.indexOf("$");
		if(dollarIndex > 0) {
			filteredClassName = filteredClassName.substring(0, dollarIndex);
		}
		
		return filteredClassName.replace(".", "/")+".java";
	}
	
	public void addComputedRetValues(String name, String descriptor, List<String> rets, int firstLine) {
		String filename = getFilenameForClass();
		
		retValues.add(new MethodInfo(name, AsmClassIntrospector.getParameterTypes(descriptor), rets.toArray(new String[rets.size()]), filename, firstLine));
	}

	public List<MethodInfo> getRetValues() {
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

	public MethodVisitor visitMethod(int access, String name, String descriptor,
			String sig, String[] exceptions) {

		// ignore constructor
		if (name.equals("<init>") || name.equals("<clinit>"))
			return null;

		return new MethodVisitorImpl(className, access, name, descriptor, this);
	}

	public void visitOuterClass(String arg0, String arg1, String arg2) {
	}

	public void visitSource(String arg0, String arg1) {
	}

}
