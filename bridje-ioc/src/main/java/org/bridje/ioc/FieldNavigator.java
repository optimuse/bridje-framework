/*
 * Copyright 2015 Bridje Framework.
 *
 * Alejandro Ferrandiz (acksecurity[at]hotmail.com)
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

package org.bridje.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Functional interface used by {@link ClassRepository} for navigating annotated 
 * fields in the inversion of control context.
 * <p>
 * @param <A> The annotation that the fields must have in orther to be accept by
 *            the navigator.
 */
@FunctionalInterface
public interface FieldNavigator<A extends Annotation>
{
    /**
     * This method is call when ever a field in a component if found to have the
     * given annotation.
     * <p>
     * @param field      The field been found.
     * @param component  The class of the component that contains the found
     *                   field.
     * @param annotation The instance of the annotation of the field.
     */
    void accept(Field field, Class component, A annotation);
}
