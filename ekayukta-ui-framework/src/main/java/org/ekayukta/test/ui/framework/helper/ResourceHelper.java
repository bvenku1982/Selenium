package org.ekayukta.test.ui.framework.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceHelper {

	public static String getResourcePath(String resource) {
		String path = getBaseResourcePath();
		boolean val = path.endsWith("resources");

		if (resource.startsWith("resources") ==false){
			if (path.endsWith("resources") ==false){
				path =  path + "resources/";
			}
		}
		 path = path + resource;

		return path;
	}
	
	public static String getBaseResourcePath() {
		Path configfilepath =Paths.get("resources");
		String path = configfilepath.toAbsolutePath().toString();
		return path;
	}
	
	public static InputStream getResourcePathInputStream(String resource)  {
		FileInputStream strPath = null;
		try {
			strPath =  new FileInputStream(ResourceHelper.getResourcePath(resource));
		} catch (FileNotFoundException EX) {
			System.out.println("Exception in ResourceHelper > getResourcePathInputStream. Exception - " + EX.toString());
		}
		return strPath;
		
	}

	public static Path getSpecificResourcePath (String resource)  {
		Path ptPath = null;
		try {
			ptPath =  Paths.get("resources" + File.separator +  resource);
		} catch (Exception EX) {
			System.out.println("Exception in ResourceHelper > getSpecificResourcePath. Exception - " + EX.toString());
		}
		return ptPath;
	}
}
