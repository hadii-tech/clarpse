package com.clarity.test.java;

import com.clarity.compiler.ClarpseProject;
import com.clarity.compiler.Lang;
import com.clarity.compiler.RawFile;
import com.clarity.compiler.SourceFiles;
import com.clarity.sourcemodel.OOPSourceCodeModel;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Tests accuracy of Java Component cyclomatic complexity attribute. See {@link com.clarity.sourcemodel.Component}.
 */
public class CycloTest {

    @Test
    public void simpleCycloTest() throws Exception {
        final String code = "public class test {\n" +
                "    void aMethod() {\n" +
                "        if (2 > 4 || (5 < 7 && 5 < 7)) {\n" +
                "            return true;\n" +
                "        } else {\n" +
                "            for (String s : t) {\n" +
                "                throw new Exception();\n" +
                "                return false;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        final SourceFiles rawData = new SourceFiles(Lang.JAVA);
        rawData.insertFile(new RawFile("file2", code));
        final ClarpseProject parseService = new ClarpseProject(rawData);
        final OOPSourceCodeModel generatedSourceModel = parseService.result();
        assertTrue(generatedSourceModel.getComponent("test.aMethod()").get().cyclo() == 8);
    }


    @Test
    public void switchStmtCycloTest() throws Exception {
        final String code = "public class Test {\n" +
                "    public Test() {\n" +
                "        switch (s) {\n" +
                "            case \"a\": System.out.println(); break;\n" +
                "            case \"b\": System.out.println(); break;\n" +
                "            default: break; " +
                "        } \n" +
                "    }\n" +
                "}";
        final SourceFiles rawData = new SourceFiles(Lang.JAVA);
        rawData.insertFile(new RawFile("file2", code));
        final ClarpseProject parseService = new ClarpseProject(rawData);
        final OOPSourceCodeModel generatedSourceModel = parseService.result();
        assertTrue(generatedSourceModel.getComponent("Test.Test()").get().cyclo() == 3);
    }

    @Test
    public void complexCycloTest() throws Exception {
        final String code = "public class test {\n" +
                "    boolean aMethod() {\n" +
                "        while (2 > 4) {\n" +
                "            for (int i = 0; i < 3 && 2 == 3; i++) {\n" +
                "                if (i = 3); \n" +
                "                   try {return false; } catch (Exception e) {}\n" +
                "            }\n" +
                "        }\n" +
                "        return true;" +
                "    }\n" +
                "}";
        final SourceFiles rawData = new SourceFiles(Lang.JAVA);
        rawData.insertFile(new RawFile("file2", code));
        final ClarpseProject parseService = new ClarpseProject(rawData);
        final OOPSourceCodeModel generatedSourceModel = parseService.result();
        assertTrue(generatedSourceModel.getComponent("test.aMethod()").get().cyclo() == 8);
    }

    @Test
    public void ignoreOperatorsInComments() throws Exception {
        final String code = "public class test {\n" +
                "    boolean aMethod() {\n" +
                "        while (2 > 4) { // && || \n" +
                "        }\n" +
                "        return true;" +
                "    }\n" +
                "}";
        final SourceFiles rawData = new SourceFiles(Lang.JAVA);
        rawData.insertFile(new RawFile("file2", code));
        final ClarpseProject parseService = new ClarpseProject(rawData);
        final OOPSourceCodeModel generatedSourceModel = parseService.result();
        assertTrue(generatedSourceModel.getComponent("test.aMethod()").get().cyclo() == 4);
    }
}
