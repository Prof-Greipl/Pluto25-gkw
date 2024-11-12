package de.hawlandshut.pluto25_gkw.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hawlandshut.pluto25_gkw.model.Post;

public class Testdata {
    public static ArrayList<Post> createPostList(int n){
        ArrayList<Post> postList = new ArrayList<Post>();
        String body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

        Calendar c = Calendar.getInstance();
        for (int i = 1; i <= n; i++ ) {
            c.setTime( new Date() );
            c.set(Calendar.SECOND, 0);
            c.add(Calendar.MINUTE, -i);
            postList.add(new Post(
                    "" + i,
                    "Author " + i,
                    "Title " + i,
                    body, "key" + i,
                    c.getTime(),
                    new HashMap() ));
        }
        return postList;
    }
}
