package gnu.as3.ant.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.taskdefs.ExecTask;

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Commandline.Argument;

import java.io.File;

public class Adl extends Task 
{
	
	private ExecTask exec;
	
	public Adl()
	{
		super();
		this.failOnError = true;
		this.help = false;
		this.nodebug = false;
		this.spawn = false;
	}
	
	private final String getCompilerPath()
	{
		return this.getFlexSDKLocation()+FlexSDK.FLEX_SDK_BIN_DIR+File.separator+getExecName();
	}
	
	private String getExecName()
	{
		if (_execName == null)
		{
			final String osName = System.getProperty("os.name").toLowerCase();
			if (osName.indexOf("windows") != -1)
			{
				_execName = "adl.exe";
			} 
			else if (osName.indexOf("linux") != -1)
			{
				_execName = "adl";
			}
			else if (osName.indexOf("mac") != -1)
			{
				_execName = "adl";
			}
			else 
			{
				_execName = "undefinedExecutable";
			}
		}
		return _execName;
	}
	
	private Boolean hasCompilerForOs()
	{
		return this.getExecName() != null;
	}
	
	private final Boolean isCompilerExists()
	{
		return new File(getCompilerPath()).exists();
	}
	
	private final String getFlexSDKLocation()
	{
		if (_sdk == null)
		{
			_sdk = FlexSDK.getFlexSDKLocationFor(this.getProject());
		}
		return _sdk;
	}
	
	private String _sdk;
	private String _execName;
	
	@Override
	public void init()
	{
		if (!FlexSDK.isCompilersAvailableFor(this.getProject()))
		{
			throw new BuildException("Compilers are not available");
		}
		if (!hasCompilerForOs())
		{
			throw new BuildException("ADL is not available for this OS ("+System.getProperty("os.name")+")");
		}
		if (!isCompilerExists())
		{
			throw new BuildException("ADL path is invalid");
		}
		
		exec = new ExecTask(this);
		exec.setExecutable(getCompilerPath());
	}
	
	@Override
	public void execute()
	{
		if (help)
		{
			exec.createArg().setValue("-help");
		}
		else
		{
			if (xml == null)
			{
				throw new BuildException("Application xml must be set");
			}
			if (publisherId != null)
			{
				exec.createArg().setLine("-pubid "+publisherId);
			}
			
			if (nodebug)
			{
				exec.createArg().setValue("-nodebug");
			}
			
			exec.createArg().setValue(xml);
			
			if (dir != null)
			{
				exec.createArg().setValue(dir);
			}
			if (arguments != null)
			{
				exec.createArg().setLine("-- "+arguments);
			}
			exec.setSpawn(spawn);
		}
		exec.setFailonerror(this.failOnError);
		exec.execute();
	}
	
	private Boolean failOnError;
	
	public void setFailonerror(Boolean value)
	{
		this.failOnError = value;
	}
	
	private Boolean help;
	
	public void setHelp(Boolean value)
	{
		help = value;
	}
	
	private String arguments;
	
	public void setArguments(String value)
	{
		this.arguments = value;
	}
	
	private String runtime;
	
	public void setRuntime(String value)
	{
		this.runtime = value;
	}
	
	private String publisherId;
	
	public void setPubid(String value)
	{
		this.publisherId = value;
	}
	
	private Boolean nodebug;
	
	public void setNodebug(Boolean value)
	{
		this.nodebug = value;
	}
	
	private String xml;
	
	public void setXml(String value)
	{
		this.xml = value;
	}
	
	private String dir;
	
	public void setDir(String value)
	{
		this.dir = value;
	}
	
	private Boolean spawn;
	
	public void setSpawn(Boolean value)
	{
		this.spawn = value;
	}

}
