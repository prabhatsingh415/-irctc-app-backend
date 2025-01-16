package org.example.entities;

public class User {
    private final String name;
    private final String eMail;
    private final String hashedPassWord;

    public String getHashedPassWord() {
        return hashedPassWord;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return eMail;
    }

    public User(String name, String eMail, String hashedPassWord) {
        this.name = name;
        this.eMail = eMail;
        this.hashedPassWord = hashedPassWord;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' +
                ", eMail='" + eMail + '\'' +
                ", hashedPassWord='" + hashedPassWord + '\'' +
                '}';
    }
}
