package model;

public abstract class AppUser {
    protected int id;
    protected String username;
    protected String password;

    public AppUser() {}

    public AppUser(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public abstract boolean canEditCafe();
    public abstract boolean canDeleteReviewBy(int reviewUserId);
    public abstract String getRole();
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}