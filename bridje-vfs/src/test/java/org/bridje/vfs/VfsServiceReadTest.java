/*
 * Copyright 2015 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.vfs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import static org.junit.Assert.*;
import org.junit.Test;

public class VfsServiceReadTest
{
    private static final Logger LOG = Logger.getLogger(VfsServiceReadTest.class.getName());

    @Test
    public void testFindFiles()
    {
        String path = "/src/main";
        VfsService instance = Ioc.context().find(VfsService.class);
        instance.mount(new Path("/src"), new FileSource(new File("./src")));
        String expResult = "main";
        VFolder result = instance.findFolder(path);
        assertNotNull(result);
        assertEquals(expResult, result.getName());
    }

    @Test
    public void testRead() throws IOException
    {
        File f = new File("./target/tmptests/someprop.properties");
        if(f.exists())
        {
           f.delete();
        }
        f.getParentFile().mkdirs();
        f.createNewFile();
        Properties prop = new Properties();
        prop.put("prop1", "val1");
        prop.store(new FileWriter(f), "A properties file");

        VfsService vfsServ = Ioc.context().find(VfsService.class);
        vfsServ.mountFile("/tests", f.getParentFile());
        Properties readedProp = vfsServ.readFile("tests/someprop.properties", Properties.class);
        assertNotNull(readedProp);
        assertEquals(prop.get("prop1"), readedProp.getProperty("prop1"));
    }

    @Test
    public void testListFilesAndFolders()
    {
        VfsService vfsServ = Ioc.context().find(VfsService.class);
        VFolder otherFolder = vfsServ.findFolder("other");

        assertEquals(1, otherFolder.listFiles("*.txt").size());
        assertEquals(3, otherFolder.listFiles("**/*.txt").size());
        assertEquals(1, otherFolder.listFiles("*testfile*").size());
        assertEquals(3, otherFolder.listFiles("**/*testfile*").size());
        //assertEquals(3, otherFolder.listFolders("**/level1/**").size());
        assertTrue(otherFolder.listFiles("**/txt/*.txt").isEmpty());
    }
}
