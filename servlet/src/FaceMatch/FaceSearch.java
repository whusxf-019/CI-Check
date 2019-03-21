package FaceMatch;

import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

import FaceMatch.JsonBeen.ResultBeen;
import FaceMatch.JsonBeen.ResultBeen.UserBeen;
import Util.*;
/**
* ��������
*/
public class FaceSearch {

    public static String search(String image, String group_id) {
        // ����url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", image);
            map.put("liveness_control", "NORMAL");
            map.put("group_id_list", group_id);
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // ע�������Ϊ�˼򻯱���ÿһ������ȥ��ȡaccess_token�����ϻ���access_token�й���ʱ�䣬 �ͻ��˿����л��棬���ں����»�ȡ��
            String accessToken = "24.def52aa57c73f9a038f14d60f6525ec3.2592000.1533280877.282335-11485419";

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println("search result:  "+result);
            String user_id = getResultUid(result);
            String score = getResultScore(result);
            if(Double.parseDouble(score) > 80) {
            	return "exist";
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static String getResultUid(String jsonStr) {
    	JsonBeen jsonBean = new Gson().fromJson(jsonStr, JsonBeen.class);
    	String errorCode = jsonBean.getErrorCode();
    	ResultBeen resultBeen = jsonBean.getResultBeen();
    	if(resultBeen!=null) {
	    	List<UserBeen> faceBeens = resultBeen.getUserBeen();
	    	if(errorCode.equals("0")) {
	    		String user_id = faceBeens.get(0).getUserId();
	    		return user_id;
	    	}
    	}
    	return "0";
    }
    
    private static String getResultScore(String jsonStr) {
    	JsonBeen jsonBean = new Gson().fromJson(jsonStr, JsonBeen.class);
    	String errorCode = jsonBean.getErrorCode();
    	ResultBeen resultBeen = jsonBean.getResultBeen();
		if(resultBeen!=null) {
    	List<UserBeen> faceBeens = resultBeen.getUserBeen();
	    	if(errorCode.equals("0")) {
	    		String score = faceBeens.get(0).getScore();
	    		return score;
	    	}else {
	    		return "0";
	    	}
		}
    	else {
    		return "0";
    	}
    }
}

class JsonBeen{
	private String error_code;
	private ResultBeen result;
	public ResultBeen getResultBeen() {
		return result;
	}
	public void setResultBeen(ResultBeen result) {
		this.result = result;
	}
	public String getErrorCode() {
		return error_code;
	}
	public void setResultBeen(String error_code) {
		this.error_code = error_code;
	}
	public class ResultBeen{
		private List<UserBeen> user_list;
		public List<UserBeen> getUserBeen() {
			return user_list;
		}
		public void setUserBeen(List<UserBeen> user_list) {
			this.user_list = user_list;
		}
		public class UserBeen{
			private String user_id;
			private String score;
			public String getUserId() {
				return user_id;
			}
			public void setUserId(String user_id) {
				this.user_id = user_id;
			}
			public String getScore() {
				return score;
			}
			public void setScore(String score) {
				this.score = score;
			}
		}
	}
}
