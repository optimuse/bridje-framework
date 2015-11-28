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

package org.bridje.maven.plugin;

/**
 *
 * @author Gilberto
 */
public class GenerateFileData
{
    private Object data;

    private String tplName;

    private String dest;

    public GenerateFileData(Object data, String tplName, String dest)
    {
        this.data = data;
        this.tplName = tplName;
        this.dest = dest;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public String getTplName()
    {
        return tplName;
    }

    public void setTplName(String tplName)
    {
        this.tplName = tplName;
    }

    public String getDest()
    {
        return dest;
    }

    public void setDest(String dest)
    {
        this.dest = dest;
    }
}