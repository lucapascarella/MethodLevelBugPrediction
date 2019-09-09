package mlbp.parser;

import java.util.Collection;
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import mlbp.repo.beans.*;


public class ClassParser {

	public static ClassBean parse(TypeDeclaration pClassNode, String belongingPackage, Vector<String> imports) {
		int numberOfGetterOrSetter=0;
		
		// Instantiate the bean
		ClassBean classBean = new ClassBean();

		if(pClassNode.getSuperclassType() != null)
			classBean.setSuperclass(pClassNode.getSuperclassType().toString());
		else
			classBean.setSuperclass(null);

		// Set the name
		classBean.setName(pClassNode.getName().toString());
		classBean.setImports(imports);
		classBean.setBelongingPackage(belongingPackage);

		// Get the instance variable nodes 
		Collection<FieldDeclaration> instanceVariableNodes = new Vector<FieldDeclaration>();
		pClassNode.accept(new InstanceVariableVisitor(instanceVariableNodes));

		classBean.setTextContent(pClassNode.toString());

		Pattern newLine = Pattern.compile("\n");
		String[] lines = newLine.split(pClassNode.toString());

		classBean.setLOC(lines.length);

		// Get the instance variable beans from the instance variable nodes
		Collection<InstanceVariableBean> instanceVariableBeans = new Vector<InstanceVariableBean>();
		for (FieldDeclaration instanceVariableNode : instanceVariableNodes)
			instanceVariableBeans.add(InstanceVariableParser.parse(instanceVariableNode));

		// Set the collection of instance variables
		classBean.setInstanceVariables(instanceVariableBeans);

		// Get the method nodes
		Collection<MethodDeclaration> methodNodes = new Vector<MethodDeclaration>();
		pClassNode.accept(new MethodVisitor(methodNodes));

		// Get the method beans from the method nodes
		Collection<MethodBean> methodBeans = new Vector<MethodBean>();
		for (MethodDeclaration methodNode : methodNodes) {
			
			if((methodNode.getName().toString().contains("get") || (methodNode.getName().toString().contains("set")))) {
				if(methodNode.parameters().size() == 0) {
					numberOfGetterOrSetter++;
				}
			}
			
			MethodBean methodBean = MethodParser.parse(methodNode, instanceVariableBeans);
			methodBean.setBelongingClass(classBean);
			methodBeans.add(methodBean);
		}

		classBean.setNumberOfGetterAndSetter(numberOfGetterOrSetter);
		
		// Iterate over the collection of methods
		for (MethodBean classMethod : methodBeans) {

			// Instantiate a collection of class-defined invocations
			Collection<MethodBean> definedInvocations = new Vector<MethodBean>();

			// Get the method invocations
			Collection<MethodBean> classMethodInvocations = classMethod.getMethodCalls();

			// Iterate over the collection of method invocations
			for (MethodBean classMethodInvocation : classMethodInvocations) {
				definedInvocations.add(classMethodInvocation);
			}

			// Set the class-defined invocations
			classMethod.setMethodCalls(definedInvocations);
		}
		
		

		// Set the collection of methods
		classBean.setMethods(methodBeans);

		return classBean;

	}

	@SuppressWarnings("unused")
	private static MethodBean isInto(MethodBean pClassMethodInvocation, Collection<MethodBean> pMethodBeans) {

		// Iterate over the collection of methods
		for (MethodBean methodBean : pMethodBeans) {

			// If there is a method with the same name, return it
			if (methodBean.getName().equals(pClassMethodInvocation.getName()))
				return methodBean;
		}

		// No correspondence found
		return null;

	}
}
