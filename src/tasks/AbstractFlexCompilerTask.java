package gnu.as3.ant.tasks;

import org.apache.tools.ant.types.Commandline.Argument;

public abstract class AbstractFlexCompilerTask extends AbstractCompilerTask 
{
	
	public AbstractFlexCompilerTask(FlexCompiler compiler)
	{
		super(compiler);
		this.isAir = false;
	}
	
	private Boolean isAir;
	
	public void setAir(Boolean value)
	{
		this.isAir = value;
	}
	
	protected void createDefaultArgs()
	{
		java.createArg().setValue("+flexlib="+getFlexSDKLocation()+FlexSDK.FLEX_SDK_FRAMEWORKS_DIR);
	}
	
	@Override
	public void execute()
	{
		if (isAir)
		{
			java.createArg().setValue("+configname=air");
		}
		else
		{
			java.createArg().setValue("+configname=flex");
		}
		super.execute();
	}

}
