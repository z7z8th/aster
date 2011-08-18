/*
 * Copyright (C) 2011 0xlab - http://0xlab.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authored by Kan-Ru Chen <kanru@0xlab.org>
 */

package org.zeroxlab.aster;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

import java.io.*;

public class AsterSuiteRunner extends TestCase {
    protected void setUp() {
    }
    protected static void addTest(TestSuite suite, File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                addTest(suite, new File(dir, children[i]));
            }
        } else if (dir.getName().endsWith(".ast")){
            final String path = dir.getAbsolutePath();
	    suite.addTest(new TestCase("Test " + dir.getPath()) {
	    	    protected void runTest() {
                        try {
                            (new AsterCommandManager()).run(path);
                        } catch(IOException e) {
                            System.err.printf(e.toString());
                        }
	    	    }
                    public String toString() {
                        return getName();
                    }
	    	});
        }
    }
    public static Test suite() {
	TestSuite suite = new TestSuite();
        addTest(suite, new File(System.getProperty("test.dir")));
	return suite;
    }
    public static void run(String directory) {
        System.setProperty("test.dir", directory);
	TestRunner.run(AsterSuiteRunner.class);
    }
}
