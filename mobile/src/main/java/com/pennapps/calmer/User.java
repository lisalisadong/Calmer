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

    public Integer getStandingBPM() {
        return getInt("standingBPM");
    }

    public void setStandingBPM(Integer n) {
        put("standingBPM", n);
    }

    public Integer getSittingBPM() {
        return getInt("sittingBPM");
    }

    public void setSittingBPM(Integer n) {
        put("sittingBPM", n);
    }


    public Integer getActiveBPM() {
        return getInt("activeBPM");
    }

    public void setActiveBPM(Integer n) {
        put("activeBPM", n);
    }

    public String getUserName() {
        return getString("username");
    }

    public void setUserName(String s) {
        put("username", s);
    }
}
