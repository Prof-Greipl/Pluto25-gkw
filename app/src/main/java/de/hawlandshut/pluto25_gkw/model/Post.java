package de.hawlandshut.pluto25_gkw.model;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;


public class Post {
    private static final String TAG = "xxx Post.java";

    public String uid;
    public String email;
    public String title;
    public String body;
    public Date createdAt;
    public String firestoreKey;
    
    public Post(String uid,
                String email,
                String title,
                String body,
                String firestoreKey,
                Date createdAt
    ) {
        this.uid = uid;
        this.email = email;
        this.title = title;
        this.body = body;
        this.firestoreKey = firestoreKey;
        this.createdAt = createdAt;
    }

    // Convert a documentSnapshot to a post instance
    public static Post fromDocument(DocumentSnapshot document) {
        Log.d(TAG, "Converting : " + document.getData());
        String uid = (String) document.get("uid");
        String email = (String) document.get("email");
        String title = (String) document.get("title");
        String firebaseKey = document.getId(); // Document id

        String end = firebaseKey.substring(firebaseKey.length() - 4);

        String body = (String) document.get("body") + " (.." + end + ")"; // Test!

        // Manage Date...
        Date createdAt;
        Timestamp h_date = (Timestamp) document.get("createdAt");
        if (h_date == null) {
            //Use local date as substitute until servertime is here
            createdAt = new Date();
        } else {
            // We have a value for the server timestamp and use this.
            createdAt = h_date.toDate();
        }
        return new Post(uid, email,  title, body, firebaseKey, createdAt);
    }
}