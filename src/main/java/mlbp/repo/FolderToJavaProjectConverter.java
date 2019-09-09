package mlbp.repo;

import java.util.Vector;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import mlbp.parser.ClassParser;
import mlbp.parser.CodeParser;
import mlbp.repo.beans.ClassBean;
import mlbp.repo.beans.PackageBean;

public class FolderToJavaProjectConverter {

    public static ClassBean convert(String sourceCode) {
        CodeParser codeParser = new CodeParser();
        Vector<PackageBean> packages = new Vector<PackageBean>();
        ClassBean classBean = null;

        CompilationUnit parsed;
        try {
            parsed = codeParser.createParser(sourceCode);
            TypeDeclaration typeDeclaration = (TypeDeclaration) parsed.types().get(0);

            Vector<String> imports = new Vector<String>();

            for (Object importedResource : parsed.imports())
                imports.add(importedResource.toString());

            PackageBean packageBean = new PackageBean();
            packageBean.setName(parsed.getPackage().getName().getFullyQualifiedName());

            classBean = ClassParser.parse(typeDeclaration, packageBean.getName(), imports);

            packageBean.addClass(classBean);
            packages.add(packageBean);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
            // System.exit(-1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
            // System.exit(-1);
        }
        return classBean;
    }
}
