package gnu.as3.ant.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FlexSDK extends Task 
{

	public FlexSDK()
	{
		super();
		this.override = true;
	}

	public static final String getSDKVersion(String flexSDKLocation)
	{
		String version = "unknown";
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(flexSDKLocation+File.separator+"flex-sdk-description.xml"));
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			int n = nodes.getLength();
			Node node;
			String cversion = "";
			String build = "";
			while (--n>-1)
			{
				node = nodes.item(n);
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					if (node.getNodeName().equals("version"))
					{
						cversion = node.getFirstChild().getNodeValue();
					}
					else if (node.getNodeName().equals("build"))
					{
						build = node.getFirstChild().getNodeValue();
					}
				}
			}
			version = cversion+" build "+build;
		}
		catch(ParserConfigurationException pce)
		{
			
		}
		catch(SAXException se)
		{
			
		}
		catch(IOException ioe)
		{
			
		}
		catch(NullPointerException npe)
		{
			
		}
		finally
		{
			return version;
		}
	}

	public static final String getFlexSDKLocationFor(Project project)
	{
		String property = project.getProperty(FLEX_SDK_LOCATION_PROPERTY);
		
		if (property == null)
		{
			property = project.getProperty("FLEX_HOME");
			
			if (property == null)
			{
				try 
				{
					property = System.getenv("FLEX_HOME");
					
					if (property == null)
					{
						String path	= System.getenv("PATH");
						
						if (path == null)
						{
							path = System.getenv("Path");
							
							if (path == null)
							{
								path = System.getenv("path");
								
							}
						}
						
						if (path != null)
						{
							// try to find flex SDK in the path
							
							String[] paths = path.split(File.pathSeparator);
							
							for (String pathElement : paths)
							{
								
								pathElement = pathElement.replace("\"", "");
								pathElement = pathElement.replace("*", "");
								pathElement = pathElement.replace("?", "");
								pathElement = pathElement.replace("<", "");
								pathElement = pathElement.replace(">", "");
								pathElement = pathElement.replace("|", "");
								
								if (isPathEligible(pathElement))
								{
									File eligibleFlexHome = new File(pathElement);
									while (eligibleFlexHome != null && !isDirectoryEligible(eligibleFlexHome))
									{
										eligibleFlexHome = eligibleFlexHome.getParentFile();
									}
									
									property = eligibleFlexHome.getAbsolutePath().toString();
									setProjectProperties(project, property);
									break;
								}
							}
						}
					}
				} catch (SecurityException se)
				{
					property = null;
				}
			}
		}
		else
		{
			setProjectProperties(project, property);
		}
		return property;
	}
	
	private static Boolean isDirectoryEligible(File directory)
	{
		Boolean exists = directory != null && directory.exists() && directory.isDirectory();
		if (exists)
		{
			File[] files = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name)
				{
					return name.contains("flex-sdk-description");
				}
			});
			if (files.length == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	private static Boolean isPathEligible(String path)
	{
		return path.toLowerCase().contains("flex") && path.toLowerCase().contains("sdk");
	}
	
	public static final Boolean isCompilersAvailableFor(Project project)
	{
		return getFlexSDKLocationFor(project) != null;
	}
	
	public static final String FLEX_SDK_LOCATION_PROPERTY = "flex.sdk.location";
	public static final String FLEX_SDK_LIB_DIR = File.separator+"lib";
	public static final String FLEX_SDK_FRAMEWORKS_DIR = File.separator+"frameworks";
	public static final String FLEX_SDK_BIN_DIR = File.separator+"bin";
	
	@Override
	public void execute()
	{
		// flex sdk location must be set
		if (this.flexSDKLocation == null)
		{
			throw new BuildException("flexsdklocation attribute must be set.");
		}
		// if properties are already set and we do not override, do nothing
		if (isResolved() && !override)
		{
			return;
		}
		
		setProjectProperties(this.getProject(), this.flexSDKLocation);
	}
	
	private static void setProjectProperties(Project project, String flexSDKLocation)
	{
		
		File sdkdir = new File(flexSDKLocation);
		File libdir = new File(sdkdir.getAbsolutePath().toString()+FLEX_SDK_LIB_DIR);
		
		if (!libdir.exists())
		{
			throw new BuildException("Flex SDK location is invalid or undefined.");
		}
		
		// Check if each compiler exists and log a message if one is not found
		FlexCompiler[] compilers = FlexCompiler.class.getEnumConstants();
		String compilerJar;
		for (FlexCompiler fc : compilers)
		{
			compilerJar = flexSDKLocation+FLEX_SDK_LIB_DIR+File.separator+fc.getJar();
			if (new File(compilerJar).exists())
			{
				project.setProperty("flex.compilers."+fc.name().toLowerCase(), compilerJar);
			}
			else
			{
				System.out.println("Warning: Flex compiler "+fc.name()+" could not be found.");
			}
		}
		project.setProperty(FLEX_SDK_LOCATION_PROPERTY, flexSDKLocation);
	}
	
	protected Boolean isResolved()
	{
		return this.getProject().getProperty(FLEX_SDK_LOCATION_PROPERTY) != null;
	}
	
	private String flexSDKLocation;
	
	public void setLocation(String value)
	{
		this.flexSDKLocation = value;
	}
	
	private Boolean override;
	
	public void setOverride(Boolean value)
	{
		this.override = value;
	}

}
