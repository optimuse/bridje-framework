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

package org.bridje.vfs;

/**
 * A functional interface to navigates files in the vfs tree.
 */
@FunctionalInterface
public interface VirtualFileVisitor
{
    /**
     * Called when a match is hit in the navigation method.
     * <p>
     * @param file The file matched.
     */
    public void visit(VirtualFile file);
}