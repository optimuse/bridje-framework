/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.web.view.widgets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.bridje.ioc.Component;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileAdapter;
import org.bridje.web.view.AbstractWebView;
import org.bridje.web.view.WebLayout;
import org.bridje.web.view.WebView;

/**
 * A service for reading web views, and keep track of all widgets classes
 * in the application.
 */
@Component
public class WidgetManager implements VFileAdapter
{
    private static final Logger LOG = Logger.getLogger(WidgetManager.class.getName());

    private Unmarshaller webViewUnmarsh;

    private Marshaller webViewMarsh;

    @PostConstruct
    private void init()
    {
        try
        {
            JAXBContext webViewJaxbCtx = JAXBContext.newInstance(findComponentsClasses());
            webViewUnmarsh = webViewJaxbCtx.createUnmarshaller();
            webViewMarsh = webViewJaxbCtx.createMarshaller();
        }
        catch (JAXBException | IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public String[] getExtensions()
    {
        return new String[]
        {
            "xml"
        };
    }

    @Override
    public Class<?>[] getClasses()
    {
        return new Class<?>[]
        {
            WebView.class, WebLayout.class
        };
    }

    @Override
    public boolean canHandle(VFile vf, Class<?> resultCls)
    {
        return vf.getName().endsWith(".view.xml")
                || vf.getName().endsWith(".layout.xml");
    }

    @Override
    public <T> T read(VFile vf, Class<T> resultCls) throws IOException
    {
        return (T) toWebView(vf);
    }

    @Override
    public <T> void write(VFile vf, T contentObj) throws IOException
    {
        writeWebView(vf, (WebView) contentObj);
    }

    private Class<?>[] findComponentsClasses() throws IOException
    {
        List<Class<?>> result = new ArrayList<>();
        result.add(WebView.class);
        result.add(WebLayout.class);
        List<URL> files = findModelsFiles();
        files.stream()
                .map((url) -> readFile(url))
                .forEach((prop) -> readClasses(result, prop));
        Class<?>[] arr = new Class<?>[result.size()];
        return result.toArray(arr);
    }

    private Properties readFile(URL url)
    {
        Properties prop = new Properties();
        try (InputStream is = url.openStream())
        {
            prop.load(is);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prop;
    }

    private List<URL> findModelsFiles() throws IOException
    {
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(WidgetProcessor.WIDGETS_RESOURCE_FILE);
        while (resources.hasMoreElements())
        {
            URL nextElement = resources.nextElement();
            urls.add(nextElement);
        }
        return urls;
    }

    private void readClasses(List<Class<?>> result, Properties prop)
    {
        prop.forEach((Object k, Object v) -> 
        {
            try
            {
                Class<?> cls = Class.forName((String) k);
                if (!result.contains(cls))
                {
                    result.add(cls);
                }
            }
            catch (ClassNotFoundException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        });
    }

    private AbstractWebView toWebView(VFile f)
    {
        AbstractWebView result = null;
        try (InputStream is = f.openForRead())
        {
            Object unmObj = webViewUnmarsh.unmarshal(is);
            if (unmObj instanceof AbstractWebView)
            {
                return (AbstractWebView) unmObj;
            }
        }
        catch (JAXBException | IOException ex)
        {
            LOG.log(Level.SEVERE, "Could not load the  view " + f.getPath() + " " + ex.getMessage(), ex);
        }
        return result;
    }

    private void writeWebView(VFile f, WebView view)
    {
        try (OutputStream os = f.openForWrite())
        {
            webViewMarsh.marshal(view, os);
        }
        catch (JAXBException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
