package com.hxl.community3.provider;

import com.alibaba.fastjson.JSON;
import com.hxl.community3.dto.AccessTokenDTO;
import com.hxl.community3.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String str = response.body().string();
            String token = str.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser user = JSON.parseObject(string, GithubUser.class);

            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
