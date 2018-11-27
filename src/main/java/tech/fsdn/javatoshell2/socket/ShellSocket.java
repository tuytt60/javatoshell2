package tech.fsdn.javatoshell2.socket;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.KnownHosts;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.fsdn.javatoshell2.authentication.AdvancedVerifier;
import tech.fsdn.javatoshell2.utils.KeyMapUtil;

import javax.swing.*;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qi.liu
 * @create 2018-11-27 10:29
 * @desc 描述:
 **/
@ServerEndpoint(value = "/websocket/{ipAddress}/{userName}/{userPass}")
@Component
public class ShellSocket {

    private static Map<String, ShellSocket> webSocketMap = new HashMap<>();

    private Session session;
    InputStream in = null;
    OutputStream out = null;
    Connection conn;
    int x = 90, y = 30;


    public ShellSocket() throws AWTException {
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("ipAddress") String ipAddress, @PathParam("userName") String userName, @PathParam("userPass") String userPass) {
        this.session = session;
        webSocketMap.put(session.getId(), this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                KnownHosts database = new KnownHosts();

                conn = new Connection(ipAddress);
                try {
                    String[] hostkeyAlgos = database.getPreferredServerHostkeyAlgorithmOrder(ipAddress);

                    if (hostkeyAlgos != null) {
                        conn.setServerHostKeyAlgorithms(hostkeyAlgos);
                    }

                    conn.connect(new AdvancedVerifier(database));
                    conn.isAuthMethodAvailable(userName, "password");
                    conn.authenticateWithPassword(userName, userPass);
                    ch.ethz.ssh2.Session sess = conn.openSession();

                    /**
                     * bash
                     * vt100
                     * dumb
                     */
                    sess.requestPTY("dumb", x, y, 0, 0, null);
                    sess.startShell();
                    in = sess.getStdout();
                    out = sess.getStdin();

                    new Thread(new Runnable() {

                        char[][] lines = new char[y][];
                        int posy = 0;
                        int posx = 0;

                        private void addText(byte[] data, int len) throws IOException {
                            for (int i = 0; i < len; i++) {
                                char c = (char) (data[i] & 0xff);

                                if (c == 8) // Backspace, VERASE
                                {
                                    if (posx < 0) {
                                        continue;
                                    }
                                    posx--;
                                    continue;
                                }

                                if (c == '\r') {
                                    posx = 0;
                                    continue;
                                }

                                if (c == '\n') {
                                    posy++;
                                    if (posy >= y) {
                                        for (int k = 1; k < y; k++) {
                                            lines[k - 1] = lines[k];
                                        }
                                        posy--;
                                        lines[y - 1] = new char[x];
                                        for (int k = 0; k < x; k++) {
                                            lines[y - 1][k] = ' ';
                                        }
                                    }
                                    continue;
                                }

                                if (c < 32) {
                                    continue;
                                }

                                if (posx >= x) {
                                    posx = 0;
                                    posy++;
                                    if (posy >= y) {
                                        posy--;
                                        for (int k = 1; k < y; k++) {
                                            lines[k - 1] = lines[k];
                                        }
                                        lines[y - 1] = new char[x];
                                        for (int k = 0; k < x; k++) {
                                            lines[y - 1][k] = ' ';
                                        }
                                    }
                                }

                                if (lines[posy] == null) {
                                    lines[posy] = new char[x];
                                    for (int k = 0; k < x; k++) {
                                        lines[posy][k] = ' ';
                                    }
                                }

                                lines[posy][posx] = c;
                                posx++;
                            }

                            StringBuffer sb = new StringBuffer(x * y);

                            for (int i = 0; i < lines.length; i++) {
                                if (i != 0) {
                                    sb.append('\n');
                                }

                                if (lines[i] != null) {
                                    sb.append(lines[i]);
                                }

                            }
                            ShellSocket item = webSocketMap.get(session.getId());
                            item.sendMessage(new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8").replaceAll("\\[3;1H",""));
                        }

                        @Override
                        public void run() {
                            byte[] buff = new byte[8192];
                            try {

                                while (true) {
                                    int len = in.read(buff);
                                    if (len == -1) {
                                        return;
                                    }
                                    addText(buff, len);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        SwingUtilities.invokeLater(r);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        webSocketMap.remove(session.getId());  //从set中删除
        conn.close();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, NoSuchFieldException, AWTException {
        System.out.println("来自客户端的消息:" + message);
        if (StringUtils.isEmpty(message)) {
            return;
        }
        try {
            out.write(KeyMapUtil.converKeyMap(message));
        }catch (Exception e){}
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        onClose(session);
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}
