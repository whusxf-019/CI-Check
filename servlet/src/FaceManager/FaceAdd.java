package FaceManager;


import java.util.*;

import FaceMatch.FaceSearch;
import PraseJson.PraceJson;
import PraseJson.ResultCode;
import Util.*;
public class FaceAdd {

    public static String add(String image, String groupID, String userID) {

        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("image", image);
            map.put("group_id", groupID);
            map.put("user_id", userID);
            map.put("user_info", "abc");
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");
            String id = FaceSearch.search(image, groupID);
            
            if(FaceSearch.search(image, "001").equals("exist")) {
            	return "exist";
            }
            if(!FaceSearch.search(image, "001").equals("0")) {
            	return "0";
            }
            		
            
            String param = GsonUtils.toJson(map);


            String accessToken = "24.def52aa57c73f9a038f14d60f6525ec3.2592000.1533280877.282335-11485419";

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            PraseJson.ResultCode resultCode = PraceJson.prace(result);
            String errorCode = resultCode.getErrorCode();
            ResultCode.Result r = resultCode.getResult();
            if(errorCode.equals("0")){
                System.out.println(r.getFace_token());
                return r.getFace_token();
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
