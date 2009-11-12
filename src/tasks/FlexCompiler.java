package gnu.as3.ant.tasks;


public enum FlexCompiler 
{
	
	MXMLC("mxmlc.jar", "mxmlc"), 
	COMPC("compc.jar", "compc"), 
	ASDOC("asdoc.jar", "asdoc"), 
	ADT("adt.jar", "adt"), 
	ASC("asc.jar", "asc"), 
	FDB("fdb.jar", "fdb"), 
	OPTIMIZER("optimizer.jar", "optimizer");
	
	private FlexCompiler(String jar, String name)
	{
		this.jar = jar;
		this.name = name;
	}
	
	private String jar;
	
	public String getJar()
	{
		return jar;
	}
	
	private String name;
	
	public String getName()
	{
		return jar;
	}
	
	

}
