import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.github.xuchen93.demo.r2dbc.dto.UserImport;
import com.github.xuchen93.demo.r2dbc.table.entity.TestUser;

import java.util.List;

/**
 * @author xuchen.wang
 * @date 2025/11/6
 */
public class Demo {
	public static void main(String[] args) {
		List<TestUser> testUsers = TestUserGenerator.generateTestUsers(100);
		UserImport userImport = new UserImport();
		userImport.setBufferSize(10);
		userImport.setConcurrency(10);
		userImport.setUserList(testUsers);
		System.out.println(JSONUtil.toJsonPrettyStr(HttpUtil.createPost("http://localhost:8080/r2dbc/user/cleanUser").body("{}").execute().body()));
		System.out.println(JSONUtil.toJsonPrettyStr(HttpUtil.createGet("http://localhost:8080/r2dbc/user/timeCostClean").execute().body()));
		System.out.println(JSONUtil.toJsonPrettyStr(HttpUtil.createPost("http://localhost:8080/r2dbc/user/import").body(JSONUtil.toJsonStr(userImport)).execute().body()));
		System.out.println(JSONUtil.toJsonPrettyStr(HttpUtil.createGet("http://localhost:8080/r2dbc/user/timeCost").execute().body()));
	}
}
