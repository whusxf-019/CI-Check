package PraseJson;
import com.google.gson.Gson;

public class PraceJson {
	public static ResultCode prace(String jsonStr) {
		ResultCode rc = GsonUtil.parseJsonWithGson(jsonStr, ResultCode.class);
		System.out.println(rc);
		return rc;
	}
}

class GsonUtil {
	// 将Json数据解析成相应的映射对象
	public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
		Gson gson = new Gson();
		T result = gson.fromJson(jsonData, type);
		return result;
	}

}