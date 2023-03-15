package com.hyve.fun.errors;

public class UserNotAuthorizedException extends BadRequestAlertException{

    public UserNotAuthorizedException() {
        super("User not authorized", "User", "notAuthorized");
    }
}
