package PraseJson;
import java.util.List;

public class ResultCode {
	private String error_code;
	private String error_msg;
	private Result result;



	public class face_list {
		private String face_token;

		public String getFace_token() {
			return face_token;
		}

		public void setFace_token(String face_token) {
			this.face_token = face_token;
		}
	}

	public class Result {
		private String score;
		private String face_token;
		private List<FaceList> faceLists;
		public class FaceList{
			private String face_token;

			public void setFace_token(String face_token) {
				this.face_token = face_token;
			}

			public String getFace_token() {
				return face_token;
			}
		}

		public List<FaceList> getFaceLists() {
			return faceLists;
		}

		public void setFaceLists(List<FaceList> faceLists) {
			this.faceLists = faceLists;
		}

		public String getScore() {
			return score;
		}
		public void setFace_token(String face_token) {
			this.face_token = face_token;
		}
		public void setScore(String score) {
			this.score = score;
		}
		public String getFace_token(){
			return face_token;
		}
		@Override
		public String toString() {
			return "score = " + score + ", face_token = " + face_token;
		}
	}
	public String getErrorMsg() {
		return error_msg;
	}
	@Override
	public String toString() {
		return "ResultCode:[error_code = " + error_code + ", error_msg = " + error_msg + ", result = " + result + "]";
	}
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return error_code;
	}
	public Result getResult(){
		return result;
	}
}
