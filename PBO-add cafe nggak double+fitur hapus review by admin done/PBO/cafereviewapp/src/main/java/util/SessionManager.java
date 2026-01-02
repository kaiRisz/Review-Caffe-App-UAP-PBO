package util;

import model.AppUser;

public class SessionManager {
    private static AppUser currentUser = null;

    private SessionManager() {}

    public static void login(AppUser user) {
        currentUser = user;
    }

    public static AppUser getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}