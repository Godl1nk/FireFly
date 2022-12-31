package rip.firefly.webapi.user;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

public class User {
    public String uuid;
    public String username;

    public User(String username) {
        this.username = username;
  //      this.uuid = getUuid(username);
    }

   /*private static String getUuid(String username) {
        String[] x = {""};
        Unirest.get("https://api.mojang.com/users/profiles/minecraft/" + username).asJsonAsync(new Callback<JsonNode>(){

            @Override
            public void completed(HttpResponse<JsonNode> hr)
            {
                x[1] = hr.getBody().getObject().getString("id");
            }

            @Override
            public void failed(UnirestException ue)
            {

            }

            @Override
            public void cancelled()
            {

            }
        });
        return x[1];
    }*/
}
