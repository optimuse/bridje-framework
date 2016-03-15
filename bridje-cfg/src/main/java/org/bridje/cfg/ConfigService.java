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

package org.bridje.cfg;

/**
 * A service for access the configurations of applications in multiple
 * environments.
 * <p>
 * Configuration classes must be serializable in xml format.
 */
public interface ConfigService extends ConfigRepositoryContext
{
    /**
     * Set the context to filter repositories
     *
     * @param context The context to filter
     * @return The ConfigService object to fluent
     */
    ConfigRepositoryContext createRepoContext(String context);
}
