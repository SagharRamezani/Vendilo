package ir.ac.kntu.model;

public class Supporter extends User {
    private String userName;
    private SupportSection section;

    public Supporter(String firstName, String lastName, String userName, String password, SupportSection supportSection) throws Exception {
        super(firstName, lastName, password);
        this.userName = userName;
        this.section = supportSection;
        this.setRole(Role.Supporter);
    }

    public boolean hasAccessTo(RequestType type) {
        return switch (type) {
            case SETTINGS -> section == SupportSection.SETTINGS;
            case QUALITY_ISSUE -> section == SupportSection.QUALITY;
            default -> section == SupportSection.ORDER;
        };
    }

    public SupportSection getSection() {
        return section;
    }

    public void setSection(SupportSection section) {
        this.section = section;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getAccessLevel() {
        return Role.Supporter;
    }
}