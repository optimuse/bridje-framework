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

/**
 * Functional interface used by {@link ClassRepository} for navigating annotated 
 * classes in the inversion of control context.
 * <p>
 * @param <A> The annotation that classes must have in orther to be accept by
 *            the navigator.
 * @param <T> The type for the component that will be accepted by this navigator.
 */
@FunctionalInterface
public interface ClassNavigator<A extends Annotation, T>
{
    /**
     * This method is call when ever a class if found to have the given
     * annotation.
     * <p>
     * @param component  The class of the component found.
     * @param annotation The instance of the annotation of the component.
     */
    void accept(Class<T> component, A annotation);
}
