package ir.ac.kntu;

import ir.ac.kntu.model.Customer;
import ir.ac.kntu.model.Manager;
import ir.ac.kntu.model.SupportSection;
import ir.ac.kntu.model.Supporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    @Test
    void customerRegisterAndLoginByEmail() throws Exception {
        UserManager um = new UserManager();
        Customer c = new Customer("Ali", "Testi", "Abcd@1234", "ali@test.com", "09120009999");
        um.registerCustomer(c);

        Customer loggedIn = um.logInCustomerE("ali@test.com", "Abcd@1234");
        assertSame(c, loggedIn);

        assertThrows(Exception.class, () -> um.logInCustomerE("ali@test.com", "wrongPass"));
    }

    @Test
    void uniquenessPhoneAndEmail() throws Exception {
        UserManager um = new UserManager();
        um.registerCustomer(new Customer("A", "B", "Abcd@1234", "a@b.com", "09120000011"));
        assertThrows(Exception.class, () -> um.registerCustomer(new Customer("C", "D", "Abcd@1234", "a@b.com", "09120000012")));
        assertThrows(Exception.class, () -> um.registerCustomer(new Customer("C", "D", "Abcd@1234", "c@d.com", "09120000011")));
    }

    @Test
    void managerHierarchyCannotManageAncestor() throws Exception {
        Manager root = new Manager("Root", "Mng", "Admin@2025", "root", 0);
        Manager child = new Manager("Child", "Mng", "Abcd@1234", "child", root);
        assertFalse(child.canManage(root));
        assertTrue(root.canManage(child));
    }

    @Test
    void supporterLoginWorksWithHashedPassword() throws Exception {
        UserManager um = new UserManager();
        Supporter s = new Supporter("S", "P", "sup", "Supp*3344", SupportSection.ORDER);
        um.addSupporter(s);
        assertSame(s, um.logInSupporter("sup", "Supp*3344"));
        assertThrows(Exception.class, () -> um.logInSupporter("sup", "bad"));
    }
}
