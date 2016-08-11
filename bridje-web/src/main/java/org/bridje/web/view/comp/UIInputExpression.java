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

package org.bridje.web.view.comp;

import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.ioc.thls.Thls;

@XmlTransient
public class UIInputExpression extends UIExpression
{
    private String parameter;

    UIInputExpression(String expression)
    {
        super(expression);
    }

    public <T> void set(T value)
    {
        Thls.get(ElEnvironment.class).set(getExpression(), value);
    }
    
    public String getParameter()
    {
        if(parameter == null)
        {
            parameter = getExpression().substring(2, getExpression().length()-1);
        }
        return parameter;
    }
}
