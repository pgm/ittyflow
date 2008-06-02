package com.github.ittyflow.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

public class ParameterTypeReader {
	public static class MethodVisitorForStuff extends MethodAdapter   {

		public MethodVisitorForStuff(boolean isStatic, String name) {
			super(new EmptyVisitor());
		}

		@Override
		public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
			
		}
	}

	
	public static class ClassVisitorForStuff extends ClassAdapter {
		List<MethodNode> nodes = new ArrayList<MethodNode>();
		
		public ClassVisitorForStuff() {
			super(new EmptyVisitor());
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			
			MethodNode node = new MethodNode(access,
	                  name,
	                  desc,
	                  signature,
	                  exceptions);
			nodes.add(node);
			
			return node;
		}

		@Override
		public void visitEnd() {
			// now that we've got all the nodes
			for(MethodNode node : nodes) {
				boolean isStatic = (node.access & Opcodes.ACC_STATIC) != 0;
				Type [] argTypes = Type.getArgumentTypes(node.desc);
				int start = 0;
				if(!isStatic)
					start ++;
				
				for(int i = 0; i<argTypes.length; i++) {
					LocalVariableNode local = (LocalVariableNode) node.localVariables.get(i+start);
					if(local.index != i+start) {
						throw new RuntimeException("index mismatch");
					}
					
					String t = null;
					if(local.signature != null) {
						Type paramType = Type.getObjectType(local.signature);
						t = paramType.getClassName();
					}
					MethodParameters p = new MethodParameters(local.name, t);
					
					System.out.println("method "+node.name+" "+p.getName()+" "+p.getType());
				}
			}
				
			super.visitEnd();
		}
		
		
	}

	public static void getMethodParameters(Class<?> c) {
		InputStream is = AsmClassIntrospector.getClassAsInputStream(c);
		ClassReader cr;
		try {
			cr = new ClassReader(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		ClassVisitorForStuff v = new ClassVisitorForStuff();
		cr.accept(v, 0);
		
	}
	

}
