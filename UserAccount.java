import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
* User account class
* @author me
*/
public class UserAccount {

    private SqliteDb sqliteDb;

    private Config config;

    private Integer userAccountId;
    private Integer userAccountFlags;

    private String userAccountEmail;    
    private String userAccountName;

    private HashSet<String> userAccountCertFP;

    private Long userAccountRegTS;

    private UUID confirmCode = null;


    interface AuthPassCheck {
        Boolean checkPass(HashMap<String, String> userParam, String userInput);
    }
    interface AuthCertfpCheck {
        Boolean checkCertFp(HashMap<String, String> userParam, String userInput);
    }

    /**
     * HS of the UserNodes loggued with the UserAccount
     * Table to map the SIDs loggued with that UserAccount
     */
    private HashSet<UserNode> attachedUserNodes = new HashSet<UserNode>();

    /**
     * HM of the previously authed SIDs to the UserAccount, from the db
     * Table used to restore auth after Chanserv disconnect
     */
    //private HashMap<String, Integer> attachedLoginTokens;

    private HashMap<String, Integer> userChanlev = null;// = new HashMap<String, Integer>();

    /**
     * Constructor for UserAccount
     * @param sqliteDb database
     * @param userAccountName user account name
     * @param userAccountId user account id
     * @param userFlags user flags
     * @param userAccountEmail user account email
     * @param userAccountCertFP user account certfp
     * @param userAccountRegTS user account registration TS
     */
    public UserAccount(SqliteDb sqliteDb, String userAccountName, Integer userAccountId, Integer userFlags, String userAccountEmail, HashSet<String> userAccountCertFP, Long userAccountRegTS) {
        this.sqliteDb = sqliteDb;
        this.userAccountName = userAccountName;
        this.userAccountId = userAccountId;
        this.userAccountFlags = userFlags;
        this.userAccountEmail = userAccountEmail;
        this.userAccountCertFP = userAccountCertFP;
        this.userAccountRegTS = userAccountRegTS;

        try {
            this.userChanlev = sqliteDb.getUserChanlev(this); 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: could not retrieve chanlev");
        }

        //try { this.attachedLoginTokens = sqliteDb.getUserLoginTokens(userAccountId); }
        //catch (Exception e) {
        //    e.printStackTrace();
        //    System.out.println("Error: could not retrieve tokens");
        //}
    }

    /**
     * Constructor for UserAccount
     * @param sqliteDb database
     * @param userAccountName user account name
     * @param userFlags user flags
     * @param userAccountEmail user account email
     * @param userAccountRegTS user account registration TS
     */
    public UserAccount(SqliteDb sqliteDb, String userAccountName, Integer userFlags, String userAccountEmail, Long userAccountRegTS) {
        this.sqliteDb = sqliteDb;
        this.userAccountName = userAccountName;
        this.userAccountFlags = userFlags;
        this.userAccountEmail = userAccountEmail;
        this.userAccountRegTS = userAccountRegTS;

        try {
            this.userChanlev = sqliteDb.getUserChanlev(this); 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: could not retrieve chanlev");
        }

        try { this.userAccountId = sqliteDb.getId(userAccountName); }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: could not retrieve user id");
        }

        //try { this.attachedLoginTokens = sqliteDb.getUserLoginTokens(userAccountId); }
        //catch (Exception e) {
        //    e.printStackTrace();
        //    System.out.println("Error: could not retrieve tokens");
        //}
    }

    /**
     * Adds the UserNode to the UserAccount login tracker
     * @param user User node
     */
    public void addUserAuth(UserNode user) throws Exception {
        if (this.attachedUserNodes.contains(user) == false) {
            this.attachedUserNodes.add(user);
        }
        else {
            throw new Exception("Cannot add the usernode to the list because it is already in there");
        }
    }

    /**
     * Removes the UserNode from the UserAccount login tracker
     * @param user User node
     * @throws Exception
     */
    public void delUserAuth(UserNode user) throws Exception {
        try {
            this.attachedUserNodes.remove(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Cannot remove the usernode from the logged list because the usernode is not is there.");
        }
    }

    /**
     * Sets the user chanlev
     * @param chanlev User chanlev
     */
    public void setUserChanlev(HashMap<String, Integer> chanlev) {
        //System.out.println("BFN");
        this.userChanlev = chanlev;
    }

    /**
     * Sets the user chanlev for the channel
     * @param channel Channel node
     * @param chanlev Chanlev
     */
    public void setUserChanlev(ChannelNode channel, Integer chanlev) {
        //System.out.println("BFL");
        if (this.userChanlev.containsKey(channel.getChanName()) == true) {
            if (chanlev != 0) {
                this.userChanlev.replace(channel.getChanName(), chanlev);
            }
            else {
                this.userChanlev.remove(channel.getChanName());
            }
        }
        else {
            if (chanlev != 0) {
                this.userChanlev.put(channel.getChanName(), chanlev);
            }
        }
    }

    /**
     * Clear the user chanlev for the channel
     * @param channel channel object
     */
    public void clearUserChanlev(ChannelNode channel) {
        //System.out.println("BFM");
        setUserChanlev(channel, 0);
    }

    /**
     * Fetches the user chanlev for all their known channels
     * @return Full user chanlev
     */
    public HashMap<String, Integer> getUserChanlev() {
        //this.userChanlev.forEach( (chan, chanlev) -> { System.out.println("BFJ chan=" + chan + " chanlev=" + chanlev); });
        return this.userChanlev;
    }

    /**
     * Fetches the user chanlev for that channel
     * @param channel Channel node
     * @return Chanlev of the user on that channel
     * @throws Exception
     */
    public Integer getUserChanlev(ChannelNode channel) {
        if (this.userChanlev.containsKey(channel.getChanName()) == true) {
            return this.userChanlev.get(channel.getChanName());
        }
        else return 0;
    }

    /**
     * Returns user account id
     * @return user account id
     */
    public Integer getUserAccountId() {
        return this.userAccountId;
    }

    /**
     * Returns the account registration timestamp
     * @return registration timestamp
     */
    public Long getRegTS() {
        return this.userAccountRegTS;
    }

    /**
     * Returns the user account name
     * @return user account name
     */
    public String getUserAccountName() {
        return this.userAccountName;
    }

    /**
     * Returns the user account certfp
     * @return user certfp
     */
    public HashSet<String> getCertFP() {
        return this.userAccountCertFP;
    }

    public void setCertFP(HashSet<String> certfpList) {
        this.userAccountCertFP = certfpList;
    }

    /**
     * Returns the user account email
     * @return user email
     */
    public String getUserAccountEmail() {
        return this.userAccountEmail;
    }

    /**
     * Returns the user flags
     * @return user flags
     */
    public Integer getUserAccountFlags() {
        return this.userAccountFlags;
    }

    /**
     * Returns the user logins (attached nicks to the account)
     * @return user nodes
     */
    public HashSet<UserNode> getUserLogins() {
        return this.attachedUserNodes;
    }

    /**
     * Sets the user flags
     * @param userflags user flags
     */
    public void setUserAccountFlags(Integer userflags) {
        this.userAccountFlags = userflags;
    }

    private Boolean auth(UserNode usernode, String inputValue, Integer authType) throws Exception {

        HashMap<String, String> inputParam;

        try {
            inputParam = sqliteDb.getUser(this);
        }
        catch (Exception e) {
            throw new Exception("(EE) auth: cannot get user account information for auth.");
        }
        
        AuthPassCheck checkPass = (userparam, inputpass) -> {

            String pwHash = null;

            try { 
                Base64.Decoder dec = Base64.getDecoder();
                KeySpec spec = new PBEKeySpec(inputpass.toCharArray(), dec.decode(userparam.get("salt")), 65536, 128);
                SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] hash = f.generateSecret(spec).getEncoded();
                Base64.Encoder enc = Base64.getEncoder();

                pwHash = enc.encodeToString(hash);
            }
            catch (Exception e) { e.printStackTrace(); }

            if (userparam.get("password").equals(pwHash)) return true;
            else return false;
        };

        AuthCertfpCheck checkCert = (userparam, inputcert) -> {
            if (userparam.get("certfp").matches("(.*)" + inputcert + "(.*)")) return true;
            else return false;
        };

        if (authType.equals(Const.AUTH_TYPE_PLAIN)) {  /* Plain auth (AUTH login pass) */
            return checkPass.checkPass(inputParam, inputValue);
        }
        else if (authType.equals(Const.AUTH_TYPE_CERTFP)) { /* Certfp auth (AUTH login) */
            return checkCert.checkCertFp(inputParam, inputValue);
        }
        else if (authType.equals(Const.AUTH_TYPE_SASL_PLAIN)) { /* SASL PLAIN auth */
            return checkPass.checkPass(inputParam, inputValue);
        }
        else if (authType.equals(Const.AUTH_TYPE_SASL_EXT)) { /* SASL EXTERNAL auth */
            return checkCert.checkCertFp(inputParam, inputValue);
        }
        else {
            return false;
        }
    }

    public void authUserToAccount(UserNode usernode, String inputChallenge, Integer authType) throws Exception {

        if (auth(usernode, inputChallenge, authType) == false) {
            Thread.sleep(config.getCServeAccountWrongCredWait() *1000);
            throw new Exception("(II) Auth failed (invalid credentials): " + this.getUserAccountName() + " by " + usernode.getUserNick());
        }

        if (Flags.isUserSuspended(this.getUserAccountFlags()) == true) {
            throw new Exception("(II) Auth failed (account suspended): " + this.getUserAccountName() + " by " + usernode.getUserNick());
        }

        usernode.setUserAuthed(true);
        usernode.setUserAccount(this);
        try {
            sqliteDb.addUserAuth(usernode, authType);
        }
        catch (Exception e) {
            throw new Exception("(EE) auth: Error finalizing the auth: nick = " + usernode.getUserNick() + ", account = " + this.getUserAccountName());
            //protocol.sendNotice(client, myUserNode, fromNick, "Error finalizing the auth.");
        }
    }

    public void deAuthUserFromAccount(UserNode usernode, Integer deAuthType) throws Exception {

        try { sqliteDb.delUserAuth(usernode, deAuthType, ""); }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("(EE) auth: Error finalizing the deauth, user may still be authed: nick = " + usernode.getUserNick() + ", account = " + this.getUserAccountName());
        }
        usernode.setUserAccount(null);
        usernode.setUserAuthed(false);

    }

    public void setConfigRef(Config config) {
        this.config = config;
    }

    public void setConfirmCode(UUID uuid) {
        this.confirmCode = uuid;
    }

    public UUID getConfirmCode() {
        return this.confirmCode;
    }
}
