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

package org.bridje.core.sql;

import java.io.StringWriter;

/**
 *
 */
public class Literal implements ColumnExpresion
{
    private final Object literal;

    public Literal(Object literal)
    {
        this.literal = literal;
    }
    
    @Override
    public Condition eq(ColumnExpresion exp)
    {
        return new BinaryCondition(this, SQLOperator.EQ, exp);
    }

    @Override
    public Condition gt(ColumnExpresion exp)
    {
        return new BinaryCondition(this, SQLOperator.GT, exp);
    }

    @Override
    public Condition ge(ColumnExpresion exp)
    {
        return new BinaryCondition(this, SQLOperator.GE, exp);
    }

    @Override
    public Condition lt(ColumnExpresion exp)
    {
        return new BinaryCondition(this, SQLOperator.LT, exp);
    }

    @Override
    public Condition le(ColumnExpresion exp)
    {
        return new BinaryCondition(this, SQLOperator.LE, exp);
    }

    @Override
    public void writeSQL(StringWriter sw)
    {
        if(literal == null)
        {
            sw.append("NULL");
        }
        else
        {
            sw.append(literal.toString());
        }
    }
}
