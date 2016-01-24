package com.pennapps.calmer;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by Yujie on 1/23/16.
 */

@ParseClassName("_User")
public class User extends ParseUser {

    public String getEmail() {
        return getString("email");
    }

    @Override
    public void setEmail(String email) {
        super.put("email", email);
    }

    public Integer getRestingBPM() {
        return getInt("restingBPM");
    }

    public void setRestingBPM(int n) {
        put("restingBPM", n);
    }

    public Integer getExcitedBPM() {
        return getInt("ExcitedBPM");
    }

    public void setExcitedBPM(Integer n) {
        put("ExcitedBPM", n);
    }

    public String getUserName() {
        return getString("username");
    }

    public void setUserName(String s) {
        put("username", s);
    }
}
