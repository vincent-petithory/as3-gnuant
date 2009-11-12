package gnu.as3.ant.tasks;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Commandline.Argument;

public class Optimizer extends AbstractFlexCompilerTask 
{
	
	public Optimizer()
	{
		super(FlexCompiler.OPTIMIZER);
	}
	
	@Override
	public Argument createArg()
	{
		throw new BuildException("The arg element is not supported");
	}
	
	@Override
	public void execute()
	{
		if (input == null)
		{
			throw new BuildException("an input file must be set");
		}
		
		java.createArg().setLine("-input "+input);
		if (output != null)
		{
			java.createArg().setLine("-output "+output);
		}
		if (metadata != null)
		{
			java.createArg().setLine("-keep-as3-metadata "+metadata);
		}
		super.execute();
	}
	
	private String input;
	
	public void setInput(String value)
	{
		this.input = value;
	}
	
	private String output;
	
	public void setOutput(String value)
	{
		this.output = value;
	}
	
	private String metadata;
	
	public void setKeepmetadata(String value)
	{
		this.metadata = value;
	}
	
}
