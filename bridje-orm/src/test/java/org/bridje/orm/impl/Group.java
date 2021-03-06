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

package org.bridje.orm.impl;

import org.bridje.orm.DbObject;
import org.bridje.orm.Entity;
import org.bridje.orm.Field;
import org.bridje.orm.Key;
import org.bridje.orm.Table;
import org.bridje.orm.TableNumberColumn;
import org.bridje.orm.TableStringColumn;

@Entity(table = "group")
public class Group
{
    @DbObject
    public static Table<Group> TABLE;

    @DbObject("id")
    public static TableNumberColumn<Group, Long> ID;

    @DbObject("name")
    public static TableStringColumn<Group> NAME;

    @Key
    @Field
    private Long id;

    @Field(index = true)
    private String name;

    public Group()
    {
    }
    
    public Group(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
