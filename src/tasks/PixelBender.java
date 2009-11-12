package gnu.as3.ant.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.taskdefs.ExecTask;

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Commandline.Argument;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;


import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class PixelBender extends Task 
{
	
	private ExecTask exec;
	
	public PixelBender()
	{
		super();
		this.failOnError = false;
		this.spawn = false;
		this.filesets = new ArrayList<FileSet>();
	}
	
	@Override
	public void execute()
	{
		// resolve compiler
		if (compiler == null)
		{
			throw new BuildException("A compiler must be set");
		}
		File compilerExec = new File(compiler);
		if (!compilerExec.exists())
		{
			compilerExec = new File(this.getProject().getBaseDir().toString()+File.separator+compiler);
			if (!compilerExec.exists())
			{
				throw new BuildException("PixelBender compiler cannot be found at "+compilerExec.toString());
			}
		}
		
		// input
		if (input == null && filesets.isEmpty())
		{
			throw new BuildException("An input file must be set");
		}
		else if (input != null && !filesets.isEmpty())
		{
			throw new BuildException("filesets and single inputs cannot be set at the same time; split in two tasks.");
		}
		else if (input != null)
		{
			if (output == null)
			{
				executePixelBender(new File(this.input).getAbsolutePath());
			}
			else
			{
				executePixelBender(new File(this.input).getAbsolutePath(),new File(this.output).getAbsolutePath());
			}
		}
		
		// use the fileset 
		if (!filesets.isEmpty())
		{
			Iterator<FileSet> iterator = this.filesets.iterator();
			FileSet kernels;
			while (iterator.hasNext())
			{
				kernels = iterator.next();
				Iterator kernelIterator = kernels.iterator();
				Resource currentResource;
				while (kernelIterator.hasNext())
				{
					currentResource = (Resource) kernelIterator.next();
					
					if (currentResource instanceof FileResource)
					{
						File kernelFile = ((FileResource) currentResource).getFile();
						if (kernelFile.exists())
						{
							executePixelBender(kernelFile.getAbsolutePath());
						}
					}
				}
			}
		}
		
		
		
	}
	
	protected void executePixelBender(String input, String output)
	{
		ExecTask exec = new ExecTask(this);
		exec.setExecutable(this.compiler);
		exec.createArg().setValue(input);
		exec.createArg().setValue(output);
		exec.setFailonerror(this.failOnError);
		exec.setSpawn(this.spawn);
		log("Compiling "+new File(input).getAbsolutePath());
		exec.execute();
		log("Created "+new File(output).getAbsolutePath());
	}
	
	protected void executePixelBender(String input)
	{
		final int index = input.lastIndexOf(".");
		String generatedOutput;
		if (index != -1)
		{
			generatedOutput= input.substring(0,index)+".pbj";
		}
		else
		{
			generatedOutput = "generated.pbj";
		}
		this.executePixelBender(input,generatedOutput);
	}
	
	private Boolean failOnError;
	
	public void setFailonerror(Boolean value)
	{
		this.failOnError = value;
	}
	
	private Boolean spawn;
	
	public void setSpawn(Boolean value)
	{
		this.spawn = value;
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
	
	private String compiler;
	
	public void setCompiler(String value)
	{
		this.compiler = value;
	}
	
	
	private List<FileSet> filesets;
	
	public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }
	
	
}
