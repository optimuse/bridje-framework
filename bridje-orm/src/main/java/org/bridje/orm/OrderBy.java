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

package org.bridje.orm;

/**
 * Represents an order by statement that can be use in a query to order the
 * results.
 */
public interface OrderBy
{
    /**
     * Gets the type of the order by ASD or DESC.
     * @return ASC the order is ascending, DESC the order is descending.
     */
    OrderByType getType();

    /**
     * The column to be ordered.
     * 
     * @return A Column object witch represents the ordered column.
     */
    Column getColumn();
}
