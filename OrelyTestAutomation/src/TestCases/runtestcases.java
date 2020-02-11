package TestCases;

import java.io.IOException;

import org.ekayukta.test.ui.framework.driverScript.ScriptRunner;

public class runtestcases {

        public static void main(String[] args) throws IOException {
           // Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            ScriptRunner obj = new ScriptRunner();
            obj.Run();
        }
    }