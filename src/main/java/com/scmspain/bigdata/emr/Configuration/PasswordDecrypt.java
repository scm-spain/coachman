package com.scmspain.bigdata.emr.Configuration;

import org.jasypt.util.text.StrongTextEncryptor;

public class PasswordDecrypt
{
    private String salt;

    public PasswordDecrypt(String salt)
    {
        this.salt = salt;
    }

    public String decryptPassword(String password)
    {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(salt);

        return textEncryptor.decrypt(password);
    }
}
