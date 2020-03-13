package tech.fsdn.javatoshell2;


import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import tech.fsdn.javatoshell2.utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JupyterApi {

    public static String notebookPath = "/helloPython.ipynb";
    public static String base = "http://localhost:8888";
    public static String ipPort = "localhost:8888";
    //    public static String base = "http://106.74.18.63:8888";
//    public static String ipPort = "106.74.18.63:8888";
    public static String url = null;
//    public static Map params = new HashMap<String, String>();
    public static List<Header> list = new ArrayList();
//    headers = {'Authorization': 'Token 2082c5e4a429cfe1d42e28db338e386a19d9249713d37bd3'}

    //            #用post方法会创建一个新的terminal
    public static String params = "{\"path\":\"/helloPython.ipynb\",\"type\":\"notebook\",\"name\":\"\",\"kernel\":{\"id\":null,\"name\":\"python3\"}}";

    public static void main(String[] args) throws IOException {
        url = base + "/api/sessions";
        Header header = new BasicHeader("Authorization", "Token e3bef71b29e1593d5d2bd5bd6897541bf2c96ddf16cc675e");
        list.add(header);
        Header[] headers = new Header[list.size()];
        String response = HttpClientUtil.JsonPostInvoke(url,params, list.toArray(headers));
        JSONObject resJson = JSONObject.fromObject(response);
        JSONObject kernelJson = resJson.getJSONObject("kernel");
        String kernelId = kernelJson.getString("id");
        String sessionId = resJson.getString("id");
        System.out.println(kernelId);
        System.out.println(sessionId);
    }
}
