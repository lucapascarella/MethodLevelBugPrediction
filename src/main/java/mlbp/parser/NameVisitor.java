package mlbp.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class NameVisitor extends ASTVisitor {
	
	private Collection<String> names = new ArrayList<String>();
	
	public NameVisitor() {}
	
	public NameVisitor(Collection<String> pNames) {
		names=pNames;
	}
	
	public boolean visit(SimpleName pNameNode) {
		names.add(pNameNode.toString());
		return true;
	}
	
	public boolean visit(TypeDeclaration pClassNode) {
		return false;
	}
	
	public Collection<String> getNames() {
		return names;			
	}
	
}
