package gnu.as3.ant.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.resources.FileResource;

public class Astre extends Task 
{

	private Adl adlTask;
	
	public Astre()
	{
		super();
		this.failOnError = true;
	}
	
	@Override
	public void init()
	{
		adlTask.init();
	}
	
	@Override
	public void execute()
	{
		this.adlTask.setFailonerror(true);
		
		// TODO find dir of swf file
//		this.adlTask.setDir(true);
		
		// TODO create and replace content of xml using swf filename info
//		this.adlTask.setXml();
		super.execute();
	}
	
	private FileResource file;
	
	public void setFile(FileResource file)
	{
		this.file = file;
	}
	
	private Boolean failOnError;
	
	public void setFailonerror(Boolean value)
	{
		this.failOnError = value;
	}
	
}
