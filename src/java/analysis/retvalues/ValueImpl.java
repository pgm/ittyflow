/**
 * 
 */
package analysis.retvalues;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

public class ValueImpl extends BasicValue {
	public static ValueImpl NULL_VALUE;

	static { 
		NULL_VALUE = new ValueImpl();
		NULL_VALUE.isNull = true;
	}
	
	String owner;
	String field;
	boolean isNull = false;
	
	private ValueImpl() {
		super(Type.getObjectType("java/lang/Object"));
	}
	
	public String toString() {
		return owner+"."+field;
	}
	
	public static ValueImpl createStaticFinalValue(String owner, String field) {
		ValueImpl v = new ValueImpl();
		v.owner = owner;
		v.field = field;
		return v;
	}
	
}
