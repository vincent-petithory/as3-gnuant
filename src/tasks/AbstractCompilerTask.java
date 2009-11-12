package gnu.as3.ant.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.taskdefs.Java;

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline.Argument;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

public abstract class AbstractCompilerTask extends Task 
{
	
	protected Java java;
	
	public AbstractCompilerTask(FlexCompiler compiler)
	{
		super();
		this.compiler = compiler;
		this.failOnError = true;
		arguments = new ArrayList<Argument>();
	}
	
	protected FlexCompiler compiler;
	
	protected final String getCompilerPath()
	{
		return this.getFlexSDKLocation()+FlexSDK.FLEX_SDK_LIB_DIR+File.separator+compiler.getJar();
	}
	
	protected final Boolean isCompilerExists()
	{
		return new File(getCompilerPath()).exists();
	}
	
	protected final String getFlexSDKLocation()
	{
		if (_sdk == null)
		{
			_sdk = FlexSDK.getFlexSDKLocationFor(this.getProject());
		}
		return _sdk;
	}
	
	private String _sdk;
	
	@Override
	public void init()
	{
		if (!FlexSDK.isCompilersAvailableFor(this.getProject()))
		{
			throw new BuildException("Compilers are not available");
		}
		if (!isCompilerExists())
		{
			throw new BuildException("Compiler path of "+this.compiler.getName()+" is invalid");
		}
		java = new Java(this);
		java.setFork(true);
		java.setJar(new File(getCompilerPath()));
		
		createDefaultArgs();
	}
	
	@Override
	public void execute()
	{
		Argument[] args = getArguments();
		String finalArguments = "";
		String stringArg;
		for (Argument arg : args)
		{
			String[] parts = arg.getParts();
			stringArg = " ";
			for (String part : parts)
			{
				stringArg += " "+part;
			}
			finalArguments += stringArg;
		}
		java.createArg().setLine(finalArguments);
		log("Using Flex SDK "+FlexSDK.getSDKVersion(_sdk)+" at "+_sdk);
		java.setFailonerror(this.failOnError);
		java.execute();
	}
	
	private Boolean failOnError;
	
	public void setFailonerror(Boolean value)
	{
		this.failOnError = value;
	}
	
	protected abstract void createDefaultArgs();
	
	public Argument createArg()
	{
		Argument arg = new Argument();
		arguments.add(arg);
		return arg;
	}
	
	private List<Argument> arguments;
	
	protected Argument[] getArguments()
	{
		return arguments.toArray(new Argument[0]);
	}

}
