package com.fh.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.lang.Console;
import org.java_websocket.WebSocket;

/**
 * 在线管理
 *
 * @author FH
 * QQ 313596790
 * 2018-5-25
 */
public class OnlineChatServerPool {

    private static final Map<WebSocket, String> USER_CONNECTIONS = new HashMap<WebSocket, String>();

    private static WebSocket fhadmin = null;
    ;

    /**
     * 获取用户名
     *
     */
    public static String getUserByKey(WebSocket conn) {
        return USER_CONNECTIONS.get(conn);
    }

    /**
     * 获取在线总数
     *
     * @param
     */
    public static int getUserCount() {
        return USER_CONNECTIONS.size();
    }

    /**
     * 获取WebSocket
     *
     * @param user
     */
    public static WebSocket getWebSocketByUser(String user) {
        Set<WebSocket> keySet = USER_CONNECTIONS.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String cuser = USER_CONNECTIONS.get(conn);
                if (cuser.equals(user)) {
                    return conn;
                }
            }
        }
        return null;
    }

    /**
     * 向连接池中添加连接
     */
    public static void addUser(String user, WebSocket conn) {
        USER_CONNECTIONS.put(conn, user);    //添加连接
    }

    /**
     * 获取所有的在线用户
     *
     * @return
     */
    public static Collection<String> getOnlineUser() {
        List<String> setUsers = new ArrayList<String>();
        Collection<String> setUser = USER_CONNECTIONS.values();
        for (String u : setUser) {
            setUsers.add(u);
        }
        return setUsers;
    }

    /**
     * 移除连接池中的连接
     *
     */
    public static boolean removeUser(WebSocket conn) {
        if (USER_CONNECTIONS.containsKey(conn)) {
            USER_CONNECTIONS.remove(conn);    //移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向特定的用户发送数据
     *
     * @param message
     */
    public static void sendMessageToUser(WebSocket conn, String message) {
        if (null != conn) {
            conn.send(message);
        }
    }

    /**
     * 向所有的用户发送消息
     *
     * @param message
     */
    public static void sendMessage(String message) {
        Set<WebSocket> keySet = USER_CONNECTIONS.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String user = USER_CONNECTIONS.get(conn);
                if (user != null) {
                    conn.send(message);
                }
            }
        }
    }

    public static WebSocket getFhadmin() {
        return fhadmin;
    }

    public static void setFhadmin(WebSocket fhadmin) {
        OnlineChatServerPool.fhadmin = fhadmin;
    }
}
