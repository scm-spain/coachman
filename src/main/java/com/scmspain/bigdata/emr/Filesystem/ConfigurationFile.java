package com.scmspain.bigdata.emr.Filesystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigurationFile
{
    public InputStream getFileAsInputStream(String filename) throws FileNotFoundException
    {
        return (isAResourceFile(filename)) ? getResourceStream(filename) : getFileStream(filename);
    }

    public InputStreamReader getFileAsInputStreamReader(String filename) throws FileNotFoundException
    {
        return new InputStreamReader(getFileAsInputStream(filename));
    }

    private Boolean isAResourceFile(String filename)
    {
        return (null != getResourceStream(filename));
    }

    private InputStream getResourceStream(String filename)
    {
        try {
            return Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(filename);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private InputStream getFileStream(String filename)
    {
        try {
            return new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
