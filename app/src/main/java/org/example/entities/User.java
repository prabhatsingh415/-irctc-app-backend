package org.example.entities;

public class User {
    private String name;
    private String eMail;
    private String password;
    private String hashedPassWord;

    public String getHashedPassWord() {
        return hashedPassWord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return eMail;
    }

    public User(String name, String eMail, String hashedPassWord) {
        this.name = name;
        this.eMail = eMail;
        this.hashedPassWord = hashedPassWord;
    }

    public void setMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' +
                ", eMail='" + eMail + '\'' +
                ", hashedPassWord='" + hashedPassWord + '\'' +
                '}';
    }

    public void setPassword(String hashedPassWord) {
        this.hashedPassWord= hashedPassWord;
    }
}
