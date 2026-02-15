package ir.ac.kntu.model;

import ir.ac.kntu.View;

public class Manager extends User {
    private String username;
    private int rank;

    public Manager(String firstName, String lastName, String pass, String username, int fatherRank) throws Exception {
        super(firstName, lastName, pass);
        this.username = username;
        setRank(fatherRank + 1);
        this.setRole(Role.Manager);
    }

    public boolean canManage(User target) {
        if (target instanceof Manager manager) {
            return this.rank < manager.rank;
        } else {
            return true;
        }
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) throws Exception {
        if (rank < 0){
            throw new Exception(View.BRIGHT_RED + "Rank out of range" + View.RESET);
        }
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public Role getAccessLevel() {
        return Role.Manager;
    }

    public void setUsername(String un) {
        this.username = un;
    }
}
