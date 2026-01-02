package model;

public class RegularUser extends AppUser {

    public RegularUser() {}

    public RegularUser(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public boolean canEditCafe() {
        return false;
    }

    @Override
    public boolean canDeleteReviewBy(int reviewUserId) {
        return this.id == reviewUserId;
    }

    @Override
    public String getRole() {
        return "user";
    }
}