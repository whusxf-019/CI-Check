package FaceServlet;
import java.io.IOException;

import FaceMatch.FaceMatch;
import Util.*;
public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			byte[] bytes1;
			bytes1 = FileUtil.readFileByBytes("C:\\Users\\Administrator\\Desktop\\a.jpg");

			String image1 = Base64Util.encode(bytes1);
			FaceMatch.match("001", "001", image1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
