package model;

public class Admin extends AppUser {

    public Admin() {}

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public boolean canEditCafe() {
        return true;
    }

    @Override
    public boolean canDeleteReviewBy(int reviewUserId) {
        return true;
    }

    @Override
    public String getRole() {
        return "admin";
    }
}