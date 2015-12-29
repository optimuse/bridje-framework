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

package org.bridje.core.impl.vfs.cfg;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import org.bridje.core.vfs.VfsMountEntry;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MountEntryFactory
{
    @XmlAttribute
    private String on;

    public String getOn()
    {
        return on;
    }

    public void setOn(String on)
    {
        this.on = on;
    }

    public VfsMountEntry createMountEntry()
    {
        return new VfsMountEntry(on, getFileSystem(), getProperties());
    }
    
    public abstract String getFileSystem();

    public abstract Map<String, String> getProperties();
}