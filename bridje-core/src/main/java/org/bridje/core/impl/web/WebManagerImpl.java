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

package org.bridje.core.impl.web;

import org.bridje.core.ioc.IocContext;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;
import org.bridje.core.web.WebManager;
import org.bridje.core.web.WebRequest;
import org.bridje.core.web.WebRequestHandler;
import org.bridje.core.web.WebResponse;

@Component
class WebManagerImpl implements WebManager
{
    @Inject
    private WebRequestHandler[] handlersList;

    @Inject
    private IocContext context;

    @Override
    public void proccess(WebRequest req, WebResponse resp)
    {
        IocContext reqContext = context.createChild("WEBREQUEST");
        WebRequestChainImpl chain = new WebRequestChainImpl(req, resp, reqContext, handlersList);
        chain.procced();
    }
    
}
