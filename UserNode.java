/**
 * UserNode class
 *
 * Class to store network users:
 * - nick
 * - old nick (to handle nick changes)
 * - user/ident
 * - host, real host
 * - realname
 * - uniq id
 * - modes
 * - channels membership
 * - server
 * - certfp
 * - account
 * - auth status
 * - register status (for nick ownerships)
 * - timestamp
 *
 * @author me
 */ 
 
import java.util.HashMap;
import java.util.Map;

public class UserNode {
    
    public String userNick;
    public String userOldNick;
    public String userIdent;
    public String userHost;
    public String userRealHost;
    public String userRealName;
    public String userUniq;
    public String userModes;
    public String userCertFP;
    public String userAccount;

    public ServerNode userServer;

    public long userTS;

    //public ArrayList<String> userChanList = new ArrayList<String>();
    public Map<String, ChannelNode> userChanList = new HashMap<String, ChannelNode>();
    public Map<String, String> userChanModes = new HashMap<String, String>();

    public Boolean userAuthed;
    public Boolean userNickRegistered;

    public UserNode() {
        
    }
 
    public UserNode(String userNick, String userIdent, String userHost,
                    String userRealHost, String userRealName, String userUniq,
                    long userTS, String userModes) {
        this.userNick = userNick;
        this.userIdent = userIdent;
        this.userRealHost = userRealHost;
        if (userHost == null) {
            this.userHost = userRealHost;
        }
        else {
            this.userHost = userHost;
        }
        this.userRealName = userRealName;
        this.userUniq = userUniq;
        this.userTS = userTS;
        this.userModes = userModes;
        
    } 

    public void setUserNick(String nick) {
        this.userOldNick = this.userNick;
        this.userNick = nick;
    }
    public void setUserIdent(String ident) {
        this.userIdent = ident;
    }
    public void setUserHost(String host) {
        this.userHost = host;
    }
    public void setUserRealHost(String rhost) {
        this.userRealHost = rhost;
    }
    public void setUserRealName(String realName) {
        this.userRealName = realName;
    }
    public void setUserUniq(String uniq) {
        this.userUniq = uniq;
    }
    public void setUserModes(String modes) {
        this.userModes = modes;
    }
    public void setUserServer(ServerNode server) {
        this.userServer = server;
    }
    public void setUserCertFP(String certfp) {
        this.userCertFP = certfp;
    }
    public void setUserAccount(String account) {
        this.userAccount = account;
    }
    public void addUserToChan(String channel, ChannelNode chanObj, String mode) /*throws Exception*/ {
        //if (this.userChanList.contains(channel)) {
        //    throw new Exception("Cannot add the user inside a channel they already are in"); 
        //}
        //else {
        //    this.userChanList.add(channel);
        //}
        userChanList.put(channel, chanObj);
        userChanModes.put(channel, mode);
    }

    public void delUserFromChan(String channel) /*throws Exception*/ {
        //if (this.userChanList.contains(channel)) {
        //    throw new Exception("Cannot add the user inside a channel they already are in"); 
        //}
        //else {
        //    this.userChanList.add(channel);
        //}
        userChanList.remove(channel);
        userChanModes.remove(channel);
    }

    public String getUserChanMode(String chan) {
        return this.userChanModes.get(chan);
    }
    public void addUserChanMode(String chan, String modes) {
        this.userChanModes.replace(chan, this.userChanModes.get(chan) + modes);
    }

    public void delUserChanMode(String chan, String modes) {
        this.userChanModes.replace(chan, this.userChanModes.get(chan).replaceAll("[" + modes + "]", ""));
    }

    public void setUserAuthed(Boolean state) {
        this.userAuthed = state;
    }
    public void setUserNickRegistered(Boolean state) {
        this.userNickRegistered = state;
    }
    public void setUserTS(Integer userTS) {
        this.userTS = userTS;
    }


    public String getUserNick() {
        return this.userNick;
    }
    public String getUserOldNick() {
        return this.userOldNick;
    }
    public String getUserIdent() {
        return this.userIdent;
    }
    public String getUserHost() {
        return this.userHost;
    }
    public String getUserRealHost() {
        return this.userRealHost;
    }
    public String getUserRealName() {
        return this.userRealName;
    }
    public String getUserUniq() {
        return this.userUniq;
    }
    public String getUserModes() {
        return this.userModes;
    }
    public ServerNode getUserServer() {
        return this.userServer;
    }
    public String getUserCertFP() {
        return this.userCertFP;
    }
    public String getUserAccount() {
        return this.userAccount;
    }
    public Map<String, ChannelNode> getUserChanList() {
        return this.userChanList;
    }
    public Map<String, String> getUserChanModes() {
        return this.userChanModes;
    }
    public Boolean getUserAuthed() {
        return this.userAuthed;
    }
    public Boolean getUserNickRegistered() {
        return this.userNickRegistered;
    }
    public long getUserTS() {
        return this.userTS;
    }
}