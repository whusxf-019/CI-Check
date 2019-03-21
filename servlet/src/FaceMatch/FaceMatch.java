package FaceMatch;

import java.util.*;

import PraseJson.PraceJson;
import PraseJson.ResultCode;
import Util.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/5.
 */

public class FaceMatch {
    private static List<String> getTokenList(String user_id, String group_id){
        String strByJson = FaceGetList.getUsers(user_id, group_id);
        JsonBean jsonBean = new Gson().fromJson(strByJson, JsonBean.class);
        String errorCode = jsonBean.getError_code();
        JsonBean.ResultBeen resultBeen = jsonBean.getResult();
        List<JsonBean.ResultBeen.FaceBeen> faceBeens = resultBeen.getFace_list();
        List<String> face_list = new ArrayList();
        for (int i = 0; i < faceBeens.size(); i++){
            face_list.add(faceBeens.get(i).getFace_token());
            System.out.println(faceBeens.get(i).getFace_token());
        }
        return face_list;
    }
    public static String match(String user_id, String group_id, String image){
    	String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        List<String> face_list = getTokenList(user_id, group_id);
        try {
            List<Map<String, Object>> images = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("image", image);
            map1.put("image_type", "BASE64");
            map1.put("face_type", "LIVE");
            map1.put("quality_control", "LOW");
            map1.put("liveness_control", "NORMAL");
            images.add(map1);
            for (int i = 0; i < face_list.size(); i++){
                Map<String, Object> map2 = new HashMap<>();
                map2.put("image", face_list.get(i));
                map2.put("image_type", "FACE_TOKEN");
                map2.put("face_type", "LIVE");
                map2.put("quality_control", "LOW");
                map2.put("liveness_control", "NORMAL");
                images.add(map2);
            }
            String param = GsonUtils.toJson(images);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
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
class JsonBean{
    private String error_code;
    private ResultBeen result;

    public ResultBeen getResult() {
        return result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setResult(ResultBeen result) {
        this.result = result;
    }

    public class ResultBeen{
        private List<FaceBeen> face_list;

        public List<FaceBeen> getFace_list() {
            return face_list;
        }

        public void setFace_list(List<FaceBeen> face_list) {
            this.face_list = face_list;
        }

        public class FaceBeen{
            private String face_token;

            public String getFace_token() {
                return face_token;
            }

            public void setFace_token(String face_token) {
                this.face_token = face_token;
            }
        }
    }
}