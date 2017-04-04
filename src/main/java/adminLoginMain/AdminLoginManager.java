package adminLoginMain;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by Griffin on 4/1/2017.
 */
public class AdminLoginManager {
    private int loginAttempts;
    private String hashed1234 = "$2a$10$MyXFwAYccDvY6B8zXDYDrOldaF0kBrPI/ybyUXCOhC0.i7o7qaCBu";

    //uses BCrypt hash function to check password encryption
    public int verifyCredentials(String username, String password) {
        if((username.equals("Griffin")) && (BCrypt.checkpw(password, hashed1234))){
            return 1;
        }
        return 0;
    }

    private int updateInstructions() {
        return 0;
    }

    public int checkAdminPowers() {
        return 0;
    }
}