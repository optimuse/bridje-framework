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

package org.bridje.vfs.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.MultiVFile;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;

class MemoryFolder extends AbstractResource implements VFolder
{
    private Map<String,VFolder> foldersMap;

    private Map<String, VFile> filesMap;

    private List<VFolder> folders;

    private List<VFile> files;

    MemoryFolder(Path path)
    {
        super(path);
    }

    @Override
    public VFolder findFolder(Path path)
    {
        if(foldersMap == null || path == null)
        {
            return null;
        }
        if(path.isLast())
        {
            if(path.isSelf())
            {
                return this;
            }
            else if(path.isParent())
            {
                return getParent();
            }
            return foldersMap.get(path.getName());
        }
        else
        {
            VFolder folder;
            if(path.isSelf())
            {
                folder = this;
            }
            else if(path.isParent())
            {
                folder = getParent();
            }
            else
            {
                folder = foldersMap.get(path.getFirstElement());
            }
            if(folder == null)
            {
                return null;
            }
            return folder.findFolder(path.getNext());
        }
    }

    @Override
    public VFile findFile(Path path)
    {
        if(path == null)
        {
            return null;
        }
        if(path.isLast())
        {
            if(filesMap == null || path.isSelf() || path.isParent())
            {
                return null;
            }
            return filesMap.get(path.getFirstElement());
        }
        else
        {
            if(foldersMap == null)
            {
                return null;
            }
            VFolder nextFolder;
            if(path.isSelf())
            {
                nextFolder = this;
            }
            else
            {
                if(path.isParent())
                {
                    nextFolder = getParent();
                }
                else
                {
                    nextFolder = foldersMap.get(path.getFirstElement());
                }
            }
            if(nextFolder == null)
            {
                return null;
            }
            return nextFolder.findFile(path.getNext());
        }
    }

    @Override
    public VFolder findFolder(String path)
    {
        return findFolder(new Path(path));
    }

    @Override
    public VFile findFile(String path)
    {
        return findFile(new Path(path));
    }

    @Override
    public List<VFolder> listFolders()
    {
        if(folders == null)
        {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(folders);
    }

    @Override
    public List<VFolder> listFolders(String query)
    {
        if (folders == null) return Collections.EMPTY_LIST;
        List<VFolder> result = new ArrayList<>();
        String path = getPath().toString();
        for (VFolder folder : folders)
        {
            if (query == null ||
                    folder.getPath().globMatches(query) ||
                    folder.getPathFrom(path).globMatches(query))
            {
                result.add(folder);
            }
            // recursive
            if (query != null && query.contains("/"))
            {
                result.addAll(folder.listFolders(query));
            }
        }

        return Collections.unmodifiableList(result);
    }

    @Override
    public List<VFile> listFiles()
    {
        if(files == null)
        {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(files);
    }

    @Override
    public List<VFile> listFiles(String query)
    {
        if(files == null)
        {
            return Collections.EMPTY_LIST;
        }
        List<VFile> result = new ArrayList<>();
        String path = getPath().toString();
        for (VFile file : files)
        {
            if(query == null ||
                file.getPath().globMatches(query) ||
                file.getPathFrom(path).globMatches(query))
            {
                result.add(file);
            }
        }
        // recursive
        if (query != null && query.contains("/") && folders != null)
        {
            for (VFolder vFolder : folders)
            {
                result.addAll(vFolder.listFiles(query));
            }
        }
        return Collections.unmodifiableList(result);
    }

    protected <T extends VFolder> T addFolder(T virtualFolder)
    {
        if(virtualFolder == null || virtualFolder.getName() == null || virtualFolder.getName().trim().isEmpty())
        {
            return null;
        }
        if(foldersMap == null)
        {
            foldersMap = new LinkedHashMap<>();
            folders = new ArrayList<>();
        }
        VFolder vf = foldersMap.get(virtualFolder.getName());
        if(vf != null)
        {
            if(vf instanceof ProxyFolder)
            {
                ((ProxyFolder)vf).add(virtualFolder);
            }
            else
            {
                folders.remove(vf);
                ProxyFolder pf = new ProxyFolder();
                pf.add(vf);
                pf.add(virtualFolder);
                foldersMap.put(pf.getName(), pf);
                folders.add(pf);
            }
        }
        else
        {
            foldersMap.put(virtualFolder.getName(), virtualFolder);
            folders.add(virtualFolder);
        }
        return virtualFolder;
    }

    protected void removeFolder(VFolder virtualFolder)
    {
        if(foldersMap != null)
        {
            folders.remove(foldersMap.get(virtualFolder.getName()));
            foldersMap.remove(virtualFolder.getName());
        }
    }

    protected <T extends VFile> T addFile(T virtualFile)
    {
        if(virtualFile == null || virtualFile.getName() == null || virtualFile.getName().trim().isEmpty())
        {
            return null;
        }
        if(filesMap == null)
        {
            filesMap = new HashMap<>();
            files = new ArrayList<>();
        }
        VFile vf = filesMap.get(virtualFile.getName());
        if(vf != null)
        {
            if(vf instanceof ProxyFile)
            {
                ((ProxyFile)vf).add(virtualFile);
            }
            else
            {
                files.remove(vf);
                ProxyFile pf = new ProxyFile();
                pf.add(vf);
                pf.add(virtualFile);
                filesMap.put(vf.getName(), pf);
                files.add(pf);
            }
        }
        else
        {
            filesMap.put(virtualFile.getName(), virtualFile);
            files.add(virtualFile);
        }
        return virtualFile;
    }

    protected void removeFile(VFile virtualFile)
    {
        if(filesMap == null)
        {
            return;
        }
        files.remove(filesMap.get(virtualFile.getName()));
        filesMap.remove(virtualFile.getName());
    }

    @Override
    public <T> T readFile(String path, Class<T> resultCls) throws IOException
    {
        VFile file = findFile(path);
        return Ioc.context().find(VfsServiceImpl.class).readFile(file, resultCls);
    }

    @Override
    public <T> T readFile(Path path, Class<T> resultCls) throws IOException
    {
        VFile file = findFile(path);
        return Ioc.context().find(VfsServiceImpl.class).readFile(file, resultCls);
    }

    @Override
    public <T> void writeFile(String path, T contentObj) throws IOException
    {
        VFile file = findFile(path);
        Ioc.context().find(VfsServiceImpl.class).writeFile(file, contentObj);
    }

    @Override
    public <T> void writeFile(Path path, T contentObj) throws IOException
    {
        VFile file = findFile(path);
        Ioc.context().find(VfsServiceImpl.class).writeFile(file, contentObj);
    }

    @Override
    public VFile createNewFile(String filePath) throws IOException
    {
        return createNewFile(new Path(filePath));
    }

    @Override
    public VFolder mkDir(String folderPath) throws IOException
    {
        return mkDir(new Path(folderPath));
    }

    @Override
    public boolean canCreateNewFile(String filePath)
    {
        return canCreateNewFile(new Path(filePath));
    }

    @Override
    public boolean canMkDir(String folderPath)
    {
        return canMkDir(new Path(folderPath));
    }

    @Override
    public VFile createNewFile(Path filePath) throws IOException
    {
        if(!filePath.isLast())
        {
            VFolder f = findFolder(filePath.getFirstElement());
            if(f != null)
            {
                return f.createNewFile(filePath.getNext());
            }
        }
        throw new IOException("Cannot create physical file here.");
    }

    @Override
    public VFolder mkDir(Path folderPath) throws IOException
    {
        if(!folderPath.isLast())
        {
            VFolder f = findFolder(folderPath.getFirstElement());
            if(f != null)
            {
                return f.mkDir(folderPath.getNext());
            }
        }
        throw new IOException("Cannot create physical folder here.");
    }

    @Override
    public boolean canCreateNewFile(Path filePath)
    {
        if(!filePath.isLast())
        {
            VFolder f = findFolder(filePath.getFirstElement());
            if(f != null)
            {
                return f.canCreateNewFile(filePath.getNext());
            }
        }
        return false;
    }

    @Override
    public boolean canMkDir(Path folderPath)
    {
        if(!folderPath.isLast())
        {
            VFolder f = findFolder(folderPath.getFirstElement());
            if(f != null)
            {
                return f.canMkDir(folderPath.getNext());
            }
        }
        return false;
    }

    @Override
    public <T> VFile createAndWriteNewFile(Path filePath, T contentObj) throws IOException
    {
        VFile file = createNewFile(filePath);
        if(!file.canOpenForWrite())
        {
            throw new IOException("The file cannot be open for writing");
        }
        writeFile(filePath, contentObj);
        return file;
    }

    @Override
    public <T> VFile createAndWriteNewFile(String filePath, T contentObj) throws IOException
    {
        return createAndWriteNewFile(new Path(filePath), contentObj);
    }

    @Override
    public <T> List<T> readAllFiles(String path, Class<T> resultCls) throws IOException
    {
        return readAllFiles(new Path(path), resultCls);
    }

    @Override
    public <T> List<T> readAllFiles(Path path, Class<T> resultCls) throws IOException
    {
        VfsServiceImpl vfsServ = Ioc.context().find(VfsServiceImpl.class);
        List<T> result = new ArrayList<>();
        VFile file = findFile(path);
        if(file instanceof MultiVFile)
        {
            MultiVFile mvf = (MultiVFile)file;
            List<VFile> fileList = mvf.getFiles();
            for (VFile f : fileList)
            {
                T content = vfsServ.readFile(f, resultCls);
                if(content != null)
                {
                    result.add(content);
                }
            }
        }
        else
        {
            T content = vfsServ.readFile(file, resultCls);
            if(content != null)
            {
                result.add(content);
            }
        }
        return result;
    }
}
