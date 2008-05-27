/**
 * 
 */
package com.github.ittyflow.analysis.retvalues;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Value;

public class Interpreter extends BasicInterpreter {
	/**
	 * 
	 */
	private final MethodVisitorImpl findReturnValues;

	/**
	 * @param findReturnValues
	 */
	Interpreter(MethodVisitorImpl findReturnValues) {
		this.findReturnValues = findReturnValues;
	}

	protected void addReturnValue(Value value){
		if(value instanceof ValueImpl) {
			ValueImpl xValue = (ValueImpl)value;
			if(xValue == ValueImpl.NULL_VALUE) {
				this.findReturnValues.retValues.add(null);
			} else {
				this.findReturnValues.retValues.add(xValue.owner+"."+xValue.field);
			}
		} else {
			this.findReturnValues.retValues.add("unknown");
		}
	}
	
	public Value unaryOperation(final AbstractInsnNode insn, final Value value)
			throws AnalyzerException {
		switch (insn.getOpcode()) {
		case ARETURN:
			addReturnValue(value);
			return null;
		}

		return super.unaryOperation(insn, value);
	}

	public Value newOperation(final AbstractInsnNode insn) {

		switch (insn.getOpcode()) {
		case ACONST_NULL:
			return ValueImpl.NULL_VALUE;

		case LDC:
			Object cst = ((LdcInsnNode) insn).cst;
			if (cst instanceof Type) {
				return newValue(Type.getObjectType("java/lang/Class"));
			} else {
				return newValue(Type.getType(cst.getClass()));
			}
			
		case GETSTATIC:
			FieldInsnNode field = ((FieldInsnNode) insn);
			return ValueImpl.createStaticFinalValue(field.owner, field.name);
		}

		return super.newOperation(insn);
	}

}