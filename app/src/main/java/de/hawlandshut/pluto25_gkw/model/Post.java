package de.hawlandshut.pluto25_gkw.model;

import java.util.Date;
import java.util.HashMap;

public class Post {
    public String uid;
    public String email;
    public String title;
    public String body;
    public Date createdAt;
    public String firestoreKey;
    public HashMap sys;

    public Post(String uid,
                String email,
                String title,
                String body,
                String firestoreKey,
                Date createdAt,
                HashMap sys
    ) {
        this.uid = uid;
        this.email = email;
        this.title = title;
        this.body = body;
        this.firestoreKey = firestoreKey;
        this.createdAt = createdAt;
        this.sys = sys;
    }
}