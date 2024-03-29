# :rocket: Clarpse 
Clarpse is a multi-language architectural code analysis library for building better software tools.

![img](https://blog.upskillable.com/wp-content/uploads/2019/08/How-our-continuous-code-testing-culture-with-Codacy-helps-us-produce-outstanding-product11.png)

[![maintained-by](https://img.shields.io/badge/Maintained%20by-Hadii%20Technologies-violet.svg)](https://hadii.ca) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.hadii-tech/clarpse/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hadii-tech/clarpse) [![Java CI](https://github.com/hadii-tech/clarpse/actions/workflows/ci-cd.yml/badge.svg?branch=master)](https://github.com/hadii-tech/clarpse/actions/workflows/ci-cd.yml) [![codecov](https://codecov.io/gh/hadii-tech/clarpse/branch/master/graph/badge.svg)](https://codecov.io/gh/hadii-tech/clarpse) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

Clarpse facilitates the development of tools that operate over the higher level, architectural details of source code, which are exposed via an easy to use, object oriented API. Checkout the power of Clarpse in [striff-lib](https://github.com/hadii-tech/striff-lib).

# Features

 - Supports **Java** and **GoLang**. Development is currently underway for **JavaScript**(ES6 Syntax), **Python**, and **C#**. 
 - Light weight
 - Performant
 - Easy to use
 - Clean API built on top of AST
 - Support for parsing comments

# Terminology
| Term                | Definition                                                                                                                                                                  |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Component           | A language independent source unit of the code, typically represented by a class, method, interface, field variable, local variable, enum, etc ..                                                       |
| OOPSourceCodeModel  |                                                  A representation of a codebase through a collection of Component objects.                                                  |
| Component Reference | A reference between an original component to a target component, which typically exist in the form of import statements, variable declarations, method calls, and so on. |

# Getting Started
Execute `mvn generate-resources` to generate necessary Antlr files. Execute `mvn clean package assembly:single` to compile and build the entire project.

## Using The API
Clarpse abstracts source code into a higher level model in a **language-agnostic** way. This 
model focuses on the architectural properties of the original code. The code snippet below 
illustrates how this model can be generated from a `ProjectFiles` object which represents the 
source code to be analyzed.
```java
final String code = " package com.foo;  "
		       +  " public class SampleClass extends AbstractClass {                                                 "
		       +  "     /** Sample Doc Comment */                                              "
		       +  "     @SampleAnnotation                                                      "
		       +  "     public void sampleMethod(String sampleMethodParam) throws AnException {"   
		       +  "     SampleClassB.fooMethod();
		       +  "     }                                                                      "
		       +  " }                                                                          ";;
final ProjectFiles projectFiles = new ProjectFiles();
projectFiles.insertFile(new ProjectFile("SampleClass.java", code));
final ClarpseProject project = new ClarpseProject(projectFiles, Lang.JAVA);
CompileResult compileResult = project.result();
// Get the code model
OOPSourceCodeModel codeModel = compileResult.model();
// View any compile errors for any files
Set<ProjectFile> failures = compileResult.failures();
```
Note, the `ProjectFiles` object can be initialized from a local directory, a local zip file, or an 
input stream to a zip file - see `ProjectFilesTest.java` for more information.

Next, the compiled 
`OOPSourceCodeModel` is the polygot representation of our source code through a 
collection of `Component` objects. Details about these components and the relationships 
between them can be fetched in the following way:
```java
codeModel.components().forEach(component -> {
        System.out.println(component.name());
	System.out.println(component.type());           
	System.out.println(component.comment());        
	System.out.println(component.modifiers());      
	System.out.println(component.children());       
	System.out.println(component.sourceFile());
	...
	// Check out the Component class for a full list of component attributes that can be retrieved
    });
```
We can also get specific components by their unique name:
```java
Component mainClassComponent = codeModel.get("com.foo.java.SampleClass");
mainclassComponent.name();           // --> "SampleClass"
mainClassComponent.type();           // --> CLASS
mainClassComponent.comment();        // --> "Sample Doc Comment"
mainClassComponent.modifiers();      // --> ["public"]
mainClassComponent.children();       // --> ["foo.java.SampleClass.sampleMethod(java.lang.String)"]
mainClassComponent.sourceFile();     // --> "foo.java"
mainClassComponent.references();     // --> ["SimpleTypeReference: String", "TypeExtensionReference: com.foo.AbstractClass", "SimpleTypeReference: com.foo.SampleClassB"]
// Fetch the the inner method component
methodComponent = codeModel.get(mainClassComponent.children().get(0));
methodComponent.name();              // --> "sampleMethod"
methodComponent.type();              // --> METHOD
methodComponent.modifiers();         // --> ["public"]
methodComponent.children();          // --> ["com.foo.java.SampleClass.sampleMethod(String).sampleMethodParam"]
methodComopnent.codeFragment();      // --> "sampleMethod(String)"
methodComponent.sourceFile();        // --> "foo.java"
methodComponent.references();		 // --> ["SimpleTypeReference: String"]
```
# Contributing A Patch

- Submit an issue describing your proposed change.
- Fork the repo, develop and test your code changes.
- Run a local maven build using "clean package assembly:single" to ensure all tests pass and the jar is produced
- Update the versioning in the pom.xml and README.md using the x.y.z scheme:
	- x = main version number, Increase if introducing API breaking changes.
	- y = feature number, Increase this number if the change contains new features with or without bug fixes.
	- z = hotfix number, Increase this number if the change only contains bug fixes.
-  Submit a pull request.


