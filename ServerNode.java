import java.util.HashSet;

/**
 * ServerNode class
 *
 * @author me
 */ 
public class ServerNode {

    private String name; /* This server name */
    private String sid;  /* This server id (SID) */
    private String description; /* This erver description */

    private Long ts; /* This server joining timestamp */

    private Integer distance; /* This server distance in hops from here */

    private Boolean serverPeerResponded = false; /* is set true once the peer server has responded to our SERVER */
    private Boolean isThePeer           = false; /* Is the server the peer from here */
    private Boolean hasEOS              = false; /* Has the server reached EOS */

    private HashSet<UserNode>   localUsers = new HashSet<>(); /* Set of the server local users */
    private HashSet<ServerNode> childNodes = new HashSet<>(); /* Set of the servers connected to this one */

    private ServerNode parentNode = null; /* Server that has introduced this server, from our point of view */

    /**
     * Constructor called tp declare new servers on the network usually following SINFO or SERVER
     * @param serverName
     * @param serverDistance
     * @param serverId
     * @param serverDescription
     */
    public ServerNode(String serverName, Integer serverDistance, String serverId, String serverDescription) {
        this.name        = serverName;
        this.distance    = serverDistance;
        this.sid         = serverId;
        this.description = serverDescription;
        this.isThePeer   = false;
    } 

    /**
     * Constructor called to declare the peer (1st remote server added)
     * @param serverId network server id
     */
    public ServerNode(String serverId) {
        this.sid       = serverId;
        this.isThePeer = false;
    } 

    /**
     * Sets if the server has done syncing
     * @param eos true or false
     */
    public void setEOS(Boolean eos) {
        this.hasEOS = eos;
    }

    /**
     * Sets if the server is the peer connected to CService
     * @param peer true or false
     */
    public void setPeer(Boolean peer) {
        this.isThePeer = peer;
    }

    /**
     * Sets the server name (as appear in /MAP)
     * @param name server name
     */
    public void setName(String name) {
        this.name = name;
    } 

    /**
     * Sets the server description (as seen in /LINKS)
     * @param desc
     */
    public void setDescription(String desc) {
        this.description = desc;
    }  

    /**
     * Sets the server hop count to CService
     * @param dist distance in hops
     */
    public void setDistance(Integer dist) {
        this.distance = dist;
    }  

    /**
     * Sets if the server peer has responded (1st message sent fron the peer)
     * @param serverPeerResponded true or false
     */
    public void setPeerResponded(Boolean serverPeerResponded) {
        this.serverPeerResponded = serverPeerResponded;
    }
    
    /**
     * Returns the server name (as listed in /map)
     * @return server name
     */
    public String getName() {
        return this.name;
    }

    public UserNode getLocalUser(UserNode node) {
        if (this.localUsers.contains(node) == true) return node;
        else return null;
    }

    public HashSet<UserNode> getLocalUsers() {
        return this.localUsers;
    }

    public void addLocalUser(UserNode node) {
        this.localUsers.add(node);
    }

    public void removeLocalUser(UserNode node) {
        this.localUsers.remove(node);
    }

    /**
     * Returns the server network SID
     * @return server SID
     */
    public String getSid() {
        return this.sid;
    }

    public void setParent(ServerNode server) {
        this.parentNode = server;
    }

    public ServerNode getParent() {
        return this.parentNode;
    }

    public void addChildNode(ServerNode server) {
        this.childNodes.add(server);
    }

    public void delChildNode(ServerNode server) {
        this.childNodes.remove(server);
    }

    public HashSet<ServerNode> getChildNodes() {
        return this.childNodes;
    }

    /**
     * Returns if the server is the one directly connected to CServive (peer)
     * @return true or false
     */
    public Boolean isPeer() {
        return this.isThePeer;
    }

    /**
     * Returns if the server has done syncing to the network
     * @return true or false
     */
    public Boolean hasEOS() {
        return this.hasEOS;
    }
    /**
     * Gets if the peer server has responded (sent 1st message)
     * @return true or false
     */
    public Boolean hasPeerResponded() {
        return this.serverPeerResponded;
    }

    /**
     * Returns the server description as listed in /LINKS
     * @return server description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the server timestamp
     * @return server timestamp
     */
    public Long getTS() {
        return ts;
    }

    /**
     * Returns the hop count from the server to CService
     * @return hop count from the server to CService
     */
    public Integer getDistance() {
        return distance;
    }
}