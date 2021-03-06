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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import org.bridje.orm.DbObject;
import org.bridje.orm.Entity;
import org.bridje.orm.Field;
import org.bridje.orm.Key;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;
import org.bridje.orm.TableNumberColumn;
import org.bridje.orm.TableRelationColumn;
import org.bridje.orm.TableStringColumn;

@Entity(table = "users")
public class User
{
    @DbObject
    public static Table<User> TABLE;

    @DbObject("id")
    public static TableNumberColumn<User, Long> ID;

    @DbObject("name")
    public static TableStringColumn<User> NAME;

    @DbObject("clasif")
    public static TableColumn<User, Character> CLASIF;
    
    @DbObject("enable")
    public static TableColumn<User, Boolean> ENABLED;

    @DbObject("counts")
    public static TableNumberColumn<User, Byte> COUNTS;
    
    @DbObject("age")
    public static TableNumberColumn<User, Short> AGE;

    @DbObject("mins")
    public static TableNumberColumn<User, Integer> MINS;

    @DbObject("year")
    public static TableNumberColumn<User, Long> YEAR;

    @DbObject("credit")
    public static TableNumberColumn<User, Float> CREDIT;

    @DbObject("money")
    public static TableNumberColumn<User, Double> MONEY;

    @DbObject("brithday")
    public static TableColumn<User, Date> BRIRTHDAY;

    @DbObject("updated")
    public static TableColumn<User, java.sql.Date> UPDATED;

    @DbObject("created")
    public static TableColumn<User, Timestamp> CREATED;

    @DbObject("hour")
    public static TableColumn<User, Time> HOUR;
    
    @DbObject("group")
    public static TableRelationColumn<User, Group> GROUP;

    @Key
    @Field
    private Long id;

    @Field
    private String name;
    
    @Field
    private Character clasif;
    
    @Field
    private Boolean enable;
    
    @Field
    private Byte counts;
    
    @Field
    private Short age;
    
    @Field
    private Integer mins;
    
    @Field
    private Long year;
    
    @Field
    private Float credit;
    
    @Field
    private Double money;
    
    @Field
    private Date brithday;
    
    @Field
    private java.sql.Date updated;
    
    @Field
    private Timestamp created;

    @Field
    private Time hour;
    
    @Field(column = "id_group")
    private Group group;

    public User()
    {
    }

    public User(Long id, String name)
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

    public Character getClasif()
    {
        return clasif;
    }

    public void setClasif(Character clasif)
    {
        this.clasif = clasif;
    }

    public Boolean getEnable()
    {
        return enable;
    }

    public void setEnable(Boolean enable)
    {
        this.enable = enable;
    }

    public Byte getCounts()
    {
        return counts;
    }

    public void setCounts(Byte counts)
    {
        this.counts = counts;
    }

    public Short getAge()
    {
        return age;
    }

    public void setAge(Short age)
    {
        this.age = age;
    }

    public Integer getMins()
    {
        return mins;
    }

    public void setMins(Integer mins)
    {
        this.mins = mins;
    }

    public Long getYear()
    {
        return year;
    }

    public void setYear(Long year)
    {
        this.year = year;
    }

    public Float getCredit()
    {
        return credit;
    }

    public void setCredit(Float credit)
    {
        this.credit = credit;
    }

    public Double getMoney()
    {
        return money;
    }

    public void setMoney(Double money)
    {
        this.money = money;
    }

    public Date getBrithday()
    {
        return brithday;
    }

    public void setBrithday(Date brithday)
    {
        this.brithday = brithday;
    }

    public java.sql.Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(java.sql.Date updated)
    {
        this.updated = updated;
    }

    public Timestamp getCreated()
    {
        return created;
    }

    public void setCreated(Timestamp created)
    {
        this.created = created;
    }

    public Time getHour()
    {
        return hour;
    }

    public void setHour(Time hour)
    {
        this.hour = hour;
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
        this.group = group;
    }
}
