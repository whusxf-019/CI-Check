package FaceMatch;

/**
 * Created by Administrator on 2018/7/5.
 */
import java.util.*;
import Util.*;
public class FaceGetList {


    public static String getUsers(String user_id, String group_id) {
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/getlist";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("group_id", group_id);
            map.put("user_id", user_id);

            String param = GsonUtils.toJson(map);

            
            String accessToken = "24.def52aa57c73f9a038f14d60f6525ec3.2592000.1533280877.282335-11485419";

            String result = HttpUtil.post(url, accessToken, "application/json", param);

            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}