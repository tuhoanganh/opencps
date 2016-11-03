
package org.opencps.notificationmgt.utils;

public class NotificationEventKeys {

	public class OFFICIALS {
		
		public static final String EVENT1= "101";//Hồ sơ mới gửi chờ tiếp nhận
		public static final String EVENT2= "102";//Hồ sơ được bổ sung thêm tài liệu
		public static final String EVENT3= "103";//Hồ sơ nhận thông báo thanh toán mới
		public static final String EVENT4= "104";//Hồ sơ có yêu cầu được rút
		public static final String EVENT5= "105";//Hồ sơ có thông báo lỗi kết quả trả về
		public static final String EVENT6= "106";//Hồ sơ chuyển tới cán bộ xử lý
		public static final String EVENT7= "107";//Hồ sơ sắp hết hạn xử lý
		public static final String EVENT8= "108";//Hồ sơ đã quá hạn xử lý
		public static final String EVENT9= "109";//Hồ sơ cần trả kết quả bởi bộ phận một cửa

	}

	public class USERS_AND_ENTERPRISE {
		
		public static final String EVENT1= "201";//Hồ sơ mới gửi chờ tiếp nhận
		public static final String EVENT2= "202";//Hồ sơ đã được tiếp nhận xử lý
		public static final String EVENT3= "203";//Hồ sơ bị từ chối tiếp nhận
		public static final String EVENT4= "204";//Hồ sơ có yêu cầu bổ sung
		public static final String EVENT5= "205";//Hồ sơ có yêu cầu thanh toán
		public static final String EVENT6= "206";//Hồ sơ đã hoàn thành xử lý
		public static final String EVENT7= "207";//Hồ sơ đã được trả kết quả
		public static final String EVENT8= "208";//Hồ sơ được chấp thuận rút
		public static final String EVENT9= "209";//Hồ sơ được xử lý cấp lại kết quả

	}

	public class ADMINTRATOR {
		public static final String EVENT1= "301";//Hồ sơ bị lỗi xử lý
		public static final String EVENT2= "302";//Hồ sơ bị delay trong hệ thống
	}
	
	public static final String GROUP1 = "/xu-ly-ho-so";
	public static final String GROUP2 = "/yeu-cau-thanh-toan";
	public static final String GROUP3 = "";
	public static final String GROUP4 = "";
	
}
