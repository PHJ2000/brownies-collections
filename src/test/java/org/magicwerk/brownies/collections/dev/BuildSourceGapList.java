package org.magicwerk.brownies.collections.dev;

import org.magicwerk.brownies.collections.dev.BuildSource.Builder;
import org.magicwerk.brownies.collections.dev.BuildSource.FileBuilder;
import org.magicwerk.brownies.collections.dev.RefactorVisitor.RefactorMethod;
import org.magicwerk.brownies.core.files.PathTools;
import org.magicwerk.brownies.core.reflect.ClassTools;
import org.magicwerk.brownies.core.regex.RegexBuilder;
import org.magicwerk.brownies.javassist.sources.JavaParserTools;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * Create source files IntGapList, etc. out of file GapList
 * in package org.magicwerk.brownies.collections.primitive
 *
 * @author Thomas Mauch
 */
public class BuildSourceGapList extends FileBuilder {

	public BuildSourceGapList(Builder builder) {
		super(builder);
	}

	@Override
	public void build() {
		String srcClass = "org.magicwerk.brownies.collections.GapList";
		String src = readJavaFile(srcClass);
		CompilationUnit cu = readSource(src);

		visitor = new RefactorVisitor();
		addRefactor("int binarySearch", ", Comparator\\<\\? super K\\> comparator", "");
		addRefactor("int binarySearch", ", \\(Comparator\\<Object\\>\\) comparator", "");
		addRefactor(" sort", ", Comparator\\<\\? super E\\> comparator", "");

		visitor.addRefactorMethods(new RefactorMethod() {
			@Override
			public boolean match(MethodSource method) {
				return method.getName().equals("readObject");
			}

			@Override
			public boolean refactor(MethodSource method) {
				String src = method.getSource();
				src = substitute("new Object", src, "new {PRIMITIVE}");
				src = substitute(RegexBuilder.regexForLiteral("(E) ois.readObject()"), src, "ois.read{NAME}()");
				src = substitute(RegexBuilder.regexForLiteral("(E)"), src, "(int)");
				method.setSource(src);
				return false;
			}
		});
		visitor.addRefactorMethods(new RefactorMethod() {
			@Override
			public boolean match(MethodSource method) {
				return method.getName().equals("writeObject");
			}

			@Override
			public boolean refactor(MethodSource method) {
				String src = method.getSource();
				src = substitute(RegexBuilder.regexForLiteral("oos.writeObject"), src, "oos.write{NAME}");
				method.setSource(src);
				return false;
			}
		});
		visitor.addRefactorMethods(new RefactorMethod() {
			@Override
			public boolean match(MethodSource method) {
				return true;
			}

			@Override
			public boolean refactor(MethodSource method) {
				method.setSource(processMethod(method.getSource()));
				return true;
			}
		});

		src = writeSource(cu, visitor);

		src = processClass(src);
		src = applyTemplate(src);

		String typeName = applyTemplate("{NAME}GapList");
		String javaFile = typeName + ".java";

		cu = readSource(src);
		TypeDeclaration<?> type = JavaParserTools.getDeclaredType(cu, typeName);
		handleIListPrimitive(type);
		src = writeSource(cu);

		String dstFile = PathTools.getPath(
				srcDir,
				ClassTools.getPathFromClass(ClassTools.getParentPackage(srcClass)),
				"primitive",
				javaFile);
		setFile(dstFile);
		setFileContent(src);
	}

	void handleIListPrimitive(TypeDeclaration<?> type) {
		javaParserRefactor.addImport(type, "org.magicwerk.brownies.collections.IList");
		javaParserRefactor.addImport(type, "org.magicwerk.brownies.collections.GapList");

		String srcGapList = ""
				+ "	@Override\r\n"
				+ "	public IList<{WRAPPER}> toIList() { \r\n"
				+ "		int size = size(); \r\n"
				+ "		IList<{WRAPPER}> list = new GapList<>(size); \r\n"
				+ "		for (int i=0; i<size; i++) {\r\n"
				+ "			list.add(get(i)); \r\n"
				+ "		} \r\n"
				+ "		return list; \r\n"
				+ "	}\r\n";
		MethodDeclaration md = createMethod(srcGapList);
		type.getMembers().add(md);

		String srcObjGapList = ""
				+ "	@Override\r\n"
				+ "	public {NAME}ObjGapList asObjIList() { \r\n"
				+ "		return new {NAME}ObjGapList(this); \r\n"
				+ "	}\r\n";
		md = createMethod(srcObjGapList);
		type.getMembers().add(md);
	}

	String processClass(String src) {
		src = substitute("\\<E\\>", src, "");
		src = substitute("\\<EE\\>", src, "");
		src = substitute("E\\[\\]", src, "{PRIMITIVE}[]");
		src = substitute("GapList", src, "{NAME}GapList");
		src = substitute("IList", src, "I{NAME}List");
		src = substitute("Object\\[", src, "{PRIMITIVE}[");
		src = substitute("@SuppressWarnings\\(\".*\"\\)", src, "");
		src = substitute("package org.magicwerk.brownies.collections;", src,
				"package org.magicwerk.brownies.collections.primitive;\n" +
						"import org.magicwerk.brownies.collections.helper.ArraysHelper;\n" +
						"import org.magicwerk.brownies.collections.GapList;");

		// for mappedList()
		src = substitute("{NAME}GapList\\<R\\>", src, "GapList<R>");
		src = substitute("Function\\<E, R\\>", src, "Function<{WRAPPER}, R>");

		if ("char".equals(BuildSourceGapList.this.builder.getPrimitiveType())) {
			String add = "   // Special string methods \r\n"
					+ "\r\n"
					+ "	public static CharGapList create(String str) {\r\n"
					+ "		return new CharGapList(str);\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public CharGapList(String str) {\r\n"
					+ "		init(str);\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public void init(String str) {\r\n"
					+ "		char[] array = str.toCharArray();\r\n"
					+ "		init(array, array.length);\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	@Override\r\n"
					+ "	public String toString() {\r\n"
					+ "		if (start == 0 && values.length == size) {\r\n"
					+ "			return new String(values);\r\n"
					+ "		} else {\r\n"
					+ "			return new String(toArray());\r\n"
					+ "		}\r\n"
					+ "	}\r\n"
					+ "";
			int pos = src.lastIndexOf('}');
			src = src.substring(0, pos) + add + src.substring(pos);
		}
		return src;
	}

	String processMethod(String src) {
		src = substitute("E ", src, "{PRIMITIVE} ");
		src = substitute("E\\[", src, "{PRIMITIVE}[");
		src = substitute("E\\.\\.\\.", src, "{PRIMITIVE}...");
		src = substitute("\\<E\\>", src, "");
		src = substitute("\\<T\\>", src, "");
		src = substitute("\\(T\\)", src, "");
		src = substitute("T\\[", src, "{PRIMITIVE}[");
		src = substitute("Collection\\<\\?\\>", src, "Collection<{WRAPPER}>");
		src = substitute("\\? extends E", src, "{WRAPPER}");
		src = substitute("(?<!alues ==? )null", src, "{DEFAULT}");
		src = substitute("Object", src, "{PRIMITIVE}");
		src = substitute("{PRIMITIVE} clone", src, "Object clone");
		src = substitute("int clone", src, "Object clone");
		src = substitute("equals\\({PRIMITIVE} obj\\)", src, "equals(Object obj)");
		src = substitute("int\\[\\] newValues = 0", src, "{PRIMITIVE}[] newValues = null");
		src = substitute("Collection\\<\\? extends E\\>", src, "Collection<{WRAPPER}>");
		src = substitute(", comparator", src, "");
		src = substitute(", Comparator\\<\\? super K\\> comparator", src, "");
		src = substitute("\\<K\\>", src, "");
		src = substitute("K key", src, "{PRIMITIVE} key");
		src = substitute("Arrays\\.sort", src, "ArraysHelper.sort");
		src = substitute("Arrays\\.bin", src, "ArraysHelper.bin");
		src = substitute("if \\(elems != {DEFAULT}\\)", src, "if (elems != null)");
		src = substitute("if \\(elems != \\({PRIMITIVE}\\) 0\\)", src, "if (elems != null)");
		src = substitute("(coll != {DEFAULT})", src, "(coll != null)");
		src = substitute("(coll != \\({PRIMITIVE}\\) 0)", src, "(coll != null)");
		// return new BooleanGapList<>(false, false);
		src = substitute("\\<\\>\\(false, {DEFAULT}\\)", src, "(false, null)");
		// return new ByteGapList<>(false, (byte) 0);
		src = substitute("\\<\\>\\(false, \\({PRIMITIVE}\\) 0\\)", src, "(false, null)");
		return src;
	}

}
