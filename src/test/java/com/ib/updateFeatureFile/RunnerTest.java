package com.ib.updateFeatureFile;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;


public class RunnerTest {
	@Test
	public void test() throws Throwable  {
		PropertyConfigurator.configure(Constants.LOG_PROPERTY_FILE_PATH);
		Logger log = Logger.getLogger("LOG");
		log.debug("Start feature file updae");
		method me = new method();
		me.replaceString();
		me.updateExcel();
		me.renameFile();
		log.debug("Successfully updated feature file update");
	}
}
