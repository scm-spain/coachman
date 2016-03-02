package com.scmspain.bigdata.emr.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordDecryptTest
{
    private PasswordDecrypt passwordDecrypt;

    @Before
    public void setUp() throws Exception
    {
        passwordDecrypt = new PasswordDecrypt("blablablablablablablabla");
    }

    @After
    public void tearDown() throws Exception
    {
        passwordDecrypt = null;
    }

    @Test
    public void testDecryptPassword() throws Exception
    {
        assertEquals(
                "Decrypted password should be equal to given",
                "mytest",
                passwordDecrypt.decryptPassword("OueyN2UNEsUF1hOswT+3rw==")
        );
    }
}