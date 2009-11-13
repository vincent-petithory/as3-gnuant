as3-gnuant


## Ant tasks 

usage:
	
### flexsdk 

		<taskdef file="as3gnuTasks.tasks" classpath="as3gnuTasks.jar" />
		<flexsdk location="/home/vincent/dev/flex_sdk/" />

Declares the task definition and the location of the flex sdk

		<taskdef file="as3gnuTasks.tasks" classpath="as3gnuTasks.jar" />

Declares the task definition. The location of the Flex sdk will be resolved in the following order :
	- if a project property 'flex.sdk.location exists, it is used.
	- if a project property 'FLEX_HOME' exists, it is used
	- if a 'FLEX_HOME' environnement var exists, it is used
	- if a 'Path', 'PATH', or 'path' exists, the gnu tasks will try to find the flex sdk in it. 
	  Typically, you would have set the bin directory of the Flex SDK for an easy access in a shell/command prompt


### mxmlc 

		<mxmlc>
			<arg line="-file-specs Main.as" />
		</mxmlc>

Calls mxmlc and compiles the file Main.as

		<mxmlc air="true">
			<arg line="-file-specs Main.as" />
		</mxmlc>

Calls mxmlc and compiles the file Main.as for air. It will call air-config.xml instead of flex-config.xml

Note: On Linux, you must have downloaded the separate AIR SDK for Linux and, it is assumed you renamed the adl binary file to adl_lin. That name is the one expected by Flex Builder for Linux, so that everything works in both use cases.

*compc* and *asdoc* work the same.

### adl 

		<adl xml="application.xml" />

Calls adl with the application descriptor application.xml.

		<adl xml="application.xml" dir="./bin" arguments="foo bar \"foo bar\"" />

Causes adl to run in the ./bin directory and with three command line arguments : 'foo' , 'bar' and 'foo bar'
	

## gnu targets 

This project is *not* an implementation of gnu tools like autoconf, automake, 
for as3 projects.
They are blank projects with build files that have standard gnu target 
names and behavior.

A base directory tree structure is also provided, but you are not required to 
follow it. 

Basically, the core files are : 

 * ./build.xml : it contains a subset of the standard gnu targets 
 adapted for AS3. It is important to note that some targets are really specific 
 to programs written in C/C++, so they are not relevant in AS3 (like 'install').
 The build file also contains some properties that the developer is likely to 
 change (version numbers, project name, compile time properties, etc.)
 
 * ./build.properties : it contains properties that will not be edited by the 
 developer, unless he wishes to change the directory tree structure.
 
There are others files in the directory tree that are dependant 
of the build.xml file. 


Targets that are not gnu : 

For the library blank project, there is a samples directory, which contains 
samples (!!) you can run with an ant command like : 

		ant sample -Dsample=MySampleClass

## FAQ:

Well, that build system will never fit exactly my needs, 
so why use it instead of making my own ?

It will save your time spent to set up your project. As everything provided is 
the expected standard behavior of every project, you are likely to add things 
specific to your projects rather than tweak or delete things. 

I am already using my own build system on a project, 
should I change to this one ?

No, because your build system is already set up. You will loose time to adapt 
your project to that new build system, since your settings are probably 
different from that one (otherwise, you would not have 
thought of switching to it).
But you may not be satisfied by your current build system, or it may lack some 
functionalities (e.g it does not generate tar.gz 
distribution files automatically). 

Why choosing this build system, and not another one ?
This build system uses gnu standard target names and behavior, which has 
those two advantages : 

 * Other developers and contributors of your project know how to build, clean, 
 create distribution files, etc. in your project. 
 As the main developer, you don't have to teach them the commands 
 to do those common tasks. 
 As a contributor, you are ready to start developing as soon as you get the 
 project sources.
 * Users of your project will now how to build it without looking how to do it.


Supported standard gnu targets :

 * all (the default)
 * dist
 * distclean
 * check
 * clean
