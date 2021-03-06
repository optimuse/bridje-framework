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

package org.bridje.orm.adpaters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.bridje.ioc.Component;
import org.bridje.orm.Column;
import org.bridje.orm.SQLAdapter;

/**
 * An SQLAdapter for LocalDateTime class.
 */
@Component
public class LocalDateTimeAdapter implements SQLAdapter
{
    @Override
    public Object serialize(Object value, Column column)
    {
        if(value instanceof LocalDateTime)
        {
            LocalDateTime ld = ((LocalDateTime)value);
            return Date.from(ld.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    @Override
    public Object unserialize(Object value, Column column)
    {
        if(value instanceof Date)
        {
            Date d = ((Date)value);
            return Instant.ofEpochMilli(d.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }
}
