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

package org.bridje.core.impl.tpl;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.bridje.core.tpl.TemplateLoader;
import org.bridje.core.tpl.TplContext;
import org.bridje.core.tpl.TplEngine;
import org.bridje.core.tpl.TplEngineContext;

public class TplContextImpl implements TplContext
{
    private final Map<String,TplEngineContext> extContexts;

    public TplContextImpl(TemplateLoader loader, TplEngine[] engines)
    {
        extContexts = createEngineContexts(loader, engines);
    }

    @Override
    public void render(String template, Map data, OutputStream os)
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            tplEngCtx.render(template, data, os);
        }
    }

    @Override
    public void render(String template, Map data, Writer writer)
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            tplEngCtx.render(template, data, writer);
        }
    }

    @Override
    public String render(String template, Map data)
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            return tplEngCtx.render(template, data);
        }
        return null;
    }

    private String findExtension(String template)
    {
        int lastIndexOf = template.lastIndexOf(".");
        if(lastIndexOf > 0 && lastIndexOf < template.length())
        {
            return template.substring(lastIndexOf + 1);
        }
        return null;
    }

    private TplEngineContext findTplEngineContext(String template)
    {
        String ext = findExtension(template);
        if(ext != null && !ext.isEmpty())
        {
            return extContexts.get(ext);
        }
        return null;
    }

    private Map<String,TplEngineContext> createEngineContexts(TemplateLoader loader,TplEngine[] engines)
    {
        Map<String,TplEngineContext> result = new HashMap<>();
        for (TplEngine engine : engines)
        {
            if(engine.getExtension() != null && !engine.getExtension().isEmpty())
            {
                result.put(engine.getExtension(), engine.createContext(loader));
            }
        }
        return result;
    }
}
