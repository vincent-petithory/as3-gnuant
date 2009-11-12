package gnu.as3.ant.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.taskdefs.Java;

import java.io.File;

public class Fdb extends Task  
{
	
	protected Java java;
	
	public Fdb()
	{
		super();
		this.compiler = FlexCompiler.FDB;
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
	}
	
	@Override
	public void execute()
	{
		if (file == null)
		{
			throw new BuildException("A file to debug must be specified");
		}
		java.createArg().setValue("run "+file);
		java.execute();
	}
	
	private String file;
	
	public void setFile(String value)
	{
		this.file = value;
	}
	
	
}
