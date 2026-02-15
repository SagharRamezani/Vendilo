package ir.ac.kntu.model;

import ir.ac.kntu.View;

public class Manager extends User {
    private String username;
    private int rank;
    private Manager parent;

    /**
     * Create a manager with an explicit rank (0 = strongest).
     */
    public Manager(String firstName, String lastName, String pass, String username, int rank) throws Exception {
        super(firstName, lastName, pass);
        this.username = username;
        setRank(rank);
        this.parent = null;
        this.setRole(Role.Manager);
    }

    /**
     * Create a child manager under a parent manager.
     */
    public Manager(String firstName, String lastName, String pass, String username, Manager parent) throws Exception {
        super(firstName, lastName, pass);
        this.username = username;
        this.parent = parent;
        setRank(parent == null ? 0 : parent.rank + 1);
        this.setRole(Role.Manager);
    }

    public boolean canManage(User target) {
        if (target instanceof Manager manager) {
            if (this == manager) {
                return false;
            }
            // Must be higher authority (smaller rank number)
            if (!(this.rank < manager.rank)) {
                return false;
            }
            // Extra safety: cannot manage ancestors, even if ranks were set wrong.
            return !isAncestor(manager);
        } else {
            return true;
        }
    }

    private boolean isAncestor(Manager maybeAncestor) {
        Manager cur = this.parent;
        while (cur != null) {
            if (cur == maybeAncestor) {
                return true;
            }
            cur = cur.parent;
        }
        return false;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) throws Exception {
        if (rank < 0) {
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

    public Manager getParent() {
        return parent;
    }

    public void setParent(Manager parent) {
        this.parent = parent;
    }
}
