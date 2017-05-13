package com.example.qi.biubiunews.models;

/**
 * Created by qi on 17-4-25.
 */

public class Token {

    /**
     * is_admin : true
     * token : eyJhbGciOiJIUzI1NiIsImV4cCI6MTQ5NDY1OTQyMCwiaWF0IjoxNDk0MDU0NjIwfQ.eyJpZCI6OH0.hPEuYVwvEH-X5YjsgFqxIlsWgfX9Ok7PYjCotzDfy1A
     */

    private boolean is_admin;
    private String token;

    public Token(){

    }

    public Token(String token,boolean is_admin) {
        this.is_admin = is_admin;
        this.token = token;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "Token{" +
                "is_admin=" + is_admin +
                ", token='" + token + '\'' +
                '}';
    }
}
