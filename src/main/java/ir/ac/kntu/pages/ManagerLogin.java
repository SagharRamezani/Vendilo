
package ir.ac.kntu.pages;
import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Manager;

public class ManagerLogin extends Page {
    private final PageManager pageManager;
    public ManagerLogin(PageManager pm){
        super(PageTitle.MANAGER_LOGIN);
        this.pageManager = pm;
    }
    @Override
    public void show() throws Exception {
        try{
            String u = View.getStringInput("Username: ");
            String p = View.getStringInput("Password: ");
            Manager m = pageManager.getUserManager().authManager(u,p);
            if(m!=null){
                System.out.println(View.BRIGHT_GREEN + "Welcome, Manager." + View.RESET);
                pageManager.navigateToPage(PageTitle.MANAGER_HOME, m);
            }else{
                throw new Exception("Invalid credentials");
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            pageManager.back();
        }
    }
}
