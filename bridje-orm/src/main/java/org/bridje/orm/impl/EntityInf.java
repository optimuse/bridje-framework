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

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bridje.ioc.Ioc;
import org.bridje.orm.Column;
import org.bridje.orm.OrderBy;
import org.bridje.orm.OrderByType;
import org.bridje.orm.dialects.ColumnData;
import org.bridje.orm.dialects.TableData;

/**
 *
 * @param <T>
 */
class EntityInf<T> implements TableData
{
    private static final Logger LOG = Logger.getLogger(EntityInf.class.getName());

    private final Class<T> entityClass;

    private final String tableName;

    private final FieldInf keyField;

    private final List<FieldInf> fields;

    private final List<RelationInf> relations;

    private final Map<String, FieldInf<T, ?>> fieldsMap;

    public EntityInf(Class<T> entityClass, String tableName)
    {
        this.entityClass = entityClass;
        this.tableName = tableName;
        fields = new ArrayList<>();
        keyField = fillFields();
        relations = new ArrayList<>();
        if(keyField == null)
        {
            throw new IllegalArgumentException("The class " + entityClass.getName() + " does not have a valid key field.");
        }
        fieldsMap = new HashMap<>();
        fields.stream().forEach((fieldInf) -> fieldsMap.put(fieldInf.getField().getName(), fieldInf));
    }

    public Class<T> getEntityClass()
    {
        return entityClass;
    }

    public FieldInf getKeyField()
    {
        return keyField;
    }

    public List<FieldInf> getFields()
    {
        return fields;
    }

    public List<RelationInf> getRelations()
    {
        return relations;
    }

    @Override
    public String getTableName()
    {
        return tableName;
    }

    private FieldInf fillFields()
    {
        FieldInf keyField = null;
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            org.bridje.orm.Field fieldAnnot = declaredField.getAnnotation(org.bridje.orm.Field.class);
            if(fieldAnnot != null)
            {
                FieldInf fInf = new FieldInf(this, declaredField, declaredField.getType());
                if(fInf.isKey())
                {
                    if(keyField != null)
                    {
                        throw new IllegalArgumentException("Entity " + entityClass.getName() + " has more than one key field.");
                    }
                    keyField = fInf;
                }
                fields.add(fInf);
            }
        }
        return keyField;
    }

    protected void fillRelations()
    {
        OrmMetaInfService metainf = Ioc.context().find(OrmMetaInfService.class);
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            org.bridje.orm.Relation fieldAnnot = declaredField.getAnnotation(org.bridje.orm.Relation.class);
            if(fieldAnnot != null)
            {
                RelationInf fInf = new RelationInf(this, declaredField, metainf.findEntityInf(declaredField.getType()));
                relations.add(fInf);
            }
        }
    }

    public Stream<String> allFieldsStream()
    {
        return Stream.concat(
                    fields.stream().map((field) -> field.getColumnName()),
                    relations.stream().map((relation) -> relation.getColumnName())
                );
    }
    
    public String allFieldsCommaSep()
    {
        return Stream.concat(
                    fields.stream().map((field) -> field.getColumnName()),
                    relations.stream().map((relation) -> relation.getColumnName())
                ).collect(Collectors.joining(", "));
    }

    public <C> List<C> parseAllColumns(int index, Column<T, C> column, ResultSet rs, EntityContextImpl ctx)
    {
        List<C> result = new ArrayList<>();
        try
        {
            FieldInf<T, C> fieldInfo = findFieldInfo(column);
            while(rs.next())
            {
                C value = fieldInfo.castValue(column.getType(), parseEntityColumn(index, rs));
                if(value != null)
                {
                    result.add(value);
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public List<T> parseAllEntitys(ResultSet rs, EntityContextImpl ctx)
    {
        List<T> result = new ArrayList<>();
        try
        {
            while(rs.next())
            {
                T entity = parseEntityInternal(rs, ctx);
                if(entity != null)
                {
                    result.add(entity);
                    ctx.getEnittysCache().put(entity, findKeyValue(entity));
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public T parseEntity(ResultSet rs, EntityContextImpl ctx)
    {
        try
        {
            if(rs.next())
            {
                T result = parseEntityInternal(rs, ctx);
                if(result != null)
                {
                    ctx.getEnittysCache().put(result, findKeyValue(result));
                }
                return result;
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public T parseEntity(T entity, ResultSet rs, EntityContextImpl ctx)
    {
        try
        {
            if(rs.next())
            {
                fillEntity(entity, rs, ctx);
                ctx.getEnittysCache().put(entity, findKeyValue(entity));
                return entity;
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public <C> C parseColumn(int index, Column<T, C> column, ResultSet rs, EntityContextImpl ctx)
    {
        try
        {
            FieldInf<T, C> fieldInfo = findFieldInfo(column);
            if(rs.next())
            {
                return fieldInfo.castValue(column.getType(), parseEntityColumn(index, rs));
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    
    public int parseCount(ResultSet rs)
    {
        try
        {
            if(rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return -1;
    }

    private T parseEntityInternal(ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        T entity = buildEntityObject();
        fillEntity(entity, rs, ctx);
        return entity;
    }

    private <C> C parseEntityColumn(int index, ResultSet rs) throws SQLException
    {
        return (C)rs.getObject(index);
    }

    public T buildEntityObject()
    {
        try
        {
            return entityClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private void fillEntity(T entity, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        for (FieldInf fieldInf : fields)
        {
            Object value = rs.getObject(fieldInf.getColumnName());
            fieldInf.setValue(entity, value);
        }
        for (RelationInf relationInf : relations)
        {
            Object value = rs.getObject(relationInf.getColumnName());
            Class<?> rec = relationInf.getRelatedEntity().getEntityClass();
            Object relatedEntityObject = ctx.find(rec, value);
            relationInf.setValue(entity, relatedEntityObject);
        }
    }

    public <T> Object[] buildUpdateParameters(T entity, Object id)
    {
        List<Object> result = Stream.concat(
                            fields.stream().map((fi) -> fi.getValue(entity)),
                            relations.stream().map((fi) -> fi.getColumnValue(entity))
                        ).collect(Collectors.toList());
        result.add(id);
        return result.toArray();
    }
    
    public <T> Object[] buildInsertParameters(T entity)
    {
        List<Object> result = Stream.concat(
                            fields.stream().map((fi) -> fi.getValue(entity)),
                            relations.stream().map((fi) -> fi.getColumnValue(entity))
                        ).collect(Collectors.toList());
        return result.toArray();
    }

    public <T> Object findKeyValue(T entity)
    {
        return keyField.getValue(entity);
    }

    public String buildIdCondition()
    {
        return keyField.getColumnName() + " = ?";
    }

    public <C> FieldInf<T, C> findFieldInfo(Column<T, C> column)
    {
        return (FieldInf<T, C>)fieldsMap.get(column.getField());
    }    

    public String buildOrderBy(OrderBy orderBy)
    {
        return findFieldInfo(orderBy.getColumn()).getColumnName() + " " + (orderBy.getType() == OrderByType.ASC ? "ASC" : "DESC");
    }

    public int allFieldsCount()
    {
        return fields.size() + relations.size();
    }

    @Override
    public List<ColumnData> getColumns()
    {
        return Stream.concat(
                    fields.stream().map((field) -> (ColumnData)field),
                    relations.stream().map((relation) -> (ColumnData)relation)
                ).collect(Collectors.toList());
    }

    @Override
    public ColumnData getKeyColumn()
    {
        return keyField;
    }
}
