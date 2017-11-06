package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.DAO;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;

@ServerEndpoint(value = "/signaling")
public class SignalingController {

    private static ArrayList<Session> sessionList = new ArrayList<Session>();

    private JSONParser JSON = new JSONParser();

    @OnOpen
    public void onOpen(Session session) {
        try {
            //Send to the current sessions the id of the new session
            JSONObject newSession = new JSONObject();
            newSession.put("new", session.getId());
            for (Session s : sessionList) {
                s.getBasicRemote().sendText(newSession.toJSONString());
            }

            //Send all current sessionsIds to the new session
            JSONObject msg = new JSONObject();
            msg.put("id", session.getId());

            JSONArray conn = new JSONArray();
            for (Session s : sessionList) {
                conn.add(s.getId());
            }
            msg.put("connections", conn);

            sessionList.add(session);
            session.getBasicRemote().sendText(msg.toJSONString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            sessionList.remove(session);
            JSONObject remove = new JSONObject();
            remove.put("remove", session.getId());
            for (Session s : sessionList) {
                s.getBasicRemote().sendText(remove.toJSONString());
            }
            DAO.getDAO().removeWsSession(session.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String msg) {

        try {
            JSONObject msgObj = (JSONObject) JSON.parse(msg);
            if (msgObj.get("to") != null) {
                sendTo(msg, (String) msgObj.get("to"));
            } else if (msgObj.get("me") != null) {
                Long me = (Long)msgObj.get("me");
                DAO.getDAO().insertWsId(me.intValue(), (String)msgObj.get("ws"));
            } else if (msgObj.get("is") == null) {
                send(msg);
            } else if (msgObj.get("is") != null){
                Session is = getSessionByJSPSession(((Long)msgObj.get("is")).intValue());
                    Session from = getSessionById((String) msgObj.get("from"));
                if(is == null) {
                    from.getBasicRemote().sendText("{\"is\": \"false\", \"id\": "+ getSessionById(msgObj.get("is").toString()) +"}");
                } else {
                    from.getBasicRemote().sendText("{\"is\": \"true\", \"id\": "+ getSessionById(msgObj.get("is").toString()) +"}");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(String msg) {
        for (Session session : sessionList) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendTo(String msg, String to) {
        try {
            getSessionById(to)
                    .getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Session getSessionById(String id) {
        Session session = null;
        for (Session s : sessionList) {
            if (s.getId().equals(id)) {
                return s;
            }
        }

        return session;
    }
    private Session getSessionByJSPSession(int userId) {
        String wsId = DAO.getDAO().getSessionByJSPSession(userId);
        if(wsId != null) {
            return getSessionById(wsId);
        } else {
            return null;
        }
    }
}
