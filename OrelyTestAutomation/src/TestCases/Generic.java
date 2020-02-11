package TestCases;

import lu.luxtrust.vasco.OTPGenerator;
import org.ekayukta.test.ui.framework.filereader.PropertyFileReader;
import org.ekayukta.test.ui.framework.helper.ResourceHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Generic {
    public String generateOTP(String strVIRTUAL_TOKEN) {
        String tempOTP = null;
        try {
            String basePath =  ResourceHelper.getSpecificResourcePath("OTPGeneratorVacman").toAbsolutePath().toString();

            String strDPXKey = PropertyFileReader.getPropertyValue("DpxKey");
            String OTPDpxFile = basePath + File.separator + PropertyFileReader.getPropertyValue("OTPDpxFile");
            Path path = Paths.get(OTPDpxFile);
            Path ptbasePath = Paths.get(basePath);
            if (PropertyFileReader.prop.getProperty("OTPGeneratorPath")==null) {
                addLibraryPath(PropertyFileReader.getPropertyValue("OTPGeneratorVacman"));
            }
            PropertyFileReader.prop.setProperty("OTPGeneratorPath","true");
            System.out.println(System.getProperty("java.library.path"));
            OTPGenerator.setPathDpx(path.toString());
            OTPGenerator.setDpxKey(strDPXKey);
            OTPGenerator.setDefaultApplicationName("MONITORING");
            tempOTP = OTPGenerator.GenerateOTP(strVIRTUAL_TOKEN);
            return tempOTP;
        } catch (Throwable EX) {
            return tempOTP;
        }
    }
    public static void addLibraryPath(String pathToAdd) throws Exception{
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[])usrPathsField.get(null);

        //check if the path to add is already present
        for(String path : paths) {
            if(path.equals(pathToAdd)) {
                return;
            }
        }
        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length-1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }
}
