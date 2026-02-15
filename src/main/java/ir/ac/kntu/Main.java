package ir.ac.kntu;

import ir.ac.kntu.model.Manager;
import ir.ac.kntu.model.SupportSection;
import ir.ac.kntu.model.Supporter;
import ir.ac.kntu.model.User;
import ir.ac.kntu.util.SampleData;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            UserManager userManager = new UserManager();
            userManager.addManager(new Manager("Saghar", "Ramezani", "P@ss0123", "Saghar_RMZ", 0)); // GodFather
            userManager.addSupporter(new Supporter("Nahal", "Ramezani", "NahalR", "P@ssword456", SupportSection.ORDER));
            userManager.addSupporter(new Supporter("Soroush", "Khatibi", "SorenKh", "$Ecure789", SupportSection.ORDER));
            userManager.addSupporter(new Supporter("Toktam", "Khatibi", "TKH57", "Tkh&1357", SupportSection.QUALITY));
            userManager.addSupporter(new Supporter("Suzan", "Soroush", "SZNSRSH", "Suz@n1990", SupportSection.SETTINGS));
            List<User> initUsers = SampleData.users();
            for (User u : initUsers) {
                userManager.addUser(u); // generic add (check type inside UserManager)
            }
            PageManager pageManager = new PageManager(userManager);
            pageManager.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
