import com.github.xuchen93.demo.r2dbc.table.entity.TestUser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestUserGenerator {

	// 随机数据生成器
	private static final Random random = new Random();
	// 常见姓氏和名字，用于生成随机姓名
	private static final String[] FAMILY_NAMES = {"张", "李", "王", "赵", "刘", "陈", "杨", "黄", "周", "吴"};
	private static final String[] FIRST_NAMES = {"伟", "芳", "娜", "秀英", "敏", "静", "强", "磊", "军", "洋"};
	// 邮箱域名
	private static final String[] EMAIL_DOMAINS = {"qq.com", "163.com", "gmail.com", "outlook.com", "company.com"};

	/**
	 * 生成N个测试用户对象
	 *
	 * @param count 生成数量（N）
	 * @return 测试用户列表
	 */
	public static List<TestUser> generateTestUsers(int count) {
		if (count <= 0) {
			throw new IllegalArgumentException("生成数量必须为正整数");
		}

		List<TestUser> userList = new ArrayList<>(count);
		LocalDateTime now = LocalDateTime.now();

		for (int i = 0; i < count; i++) {
			TestUser user = new TestUser();

			// 用户名：随机字符串 + 数字
			user.setUsername("user_" + UUID.randomUUID().toString().substring(0, 8) + "_" + (i + 1));

			// 昵称：随机姓氏 + 随机名字
			String nickname = FAMILY_NAMES[random.nextInt(FAMILY_NAMES.length)] +
					FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
			user.setNickname(nickname);

			// 年龄：18-60岁之间随机
			user.setAge(18 + random.nextInt(43)); // 43 = 60-18+1

			// 性别：0-未知 1-男 2-女（概率分布：未知20%，男40%，女40%）
			int gender;
			int genderRand = random.nextInt(10);
			if (genderRand < 2) {
				gender = 0;
			} else if (genderRand < 6) {
				gender = 1;
			} else {
				gender = 2;
			}
			user.setGender(gender);

			// 邮箱：用户名 + 随机域名
			String email = user.getUsername() + "@" + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
			user.setEmail(email);

			// 手机号：1开头 + 10位随机数字（模拟中国大陆手机号）
			StringBuilder phone = new StringBuilder("1");
			for (int j = 0; j < 10; j++) {
				phone.append(random.nextInt(10));
			}
			user.setPhone(phone.toString());

			// 状态：0-禁用（20%） 1-正常（80%）
			user.setStatus(random.nextInt(5) == 0 ? 0 : 1);

			// 过期时间：随机未来1年内或已过期3个月内

			// 最后登录时间：随机过去1年内，或未登录（null）
			if (random.nextInt(10) != 0) { // 90%概率有登录记录
				long loginDays = random.nextInt(365);
			}


			// 创建人：固定测试账号或随机
			user.setCreatedBy("system_generator");

			// 更新时间：创建时间之后，最多30天内
			if (random.nextBoolean()) { // 50%概率有更新
				user.setUpdatedBy("system_updater_" + random.nextInt(10));
			}

			// 删除标识：0-未删除（95%） 1-已删除（5%）
			user.setDeleted(random.nextInt(20) == 0 ? 1 : 0);

			userList.add(user);
		}

		return userList;
	}

	// 测试方法
	public static void main(String[] args) {
		List<TestUser> users = generateTestUsers(10);
		System.out.println("生成了 " + users.size() + " 个测试用户：");
		for (TestUser user : users) {
			System.out.println("用户名：" + user.getUsername() + "，昵称：" + user.getNickname() +
					"，状态：" + (user.getStatus() == 1 ? "正常" : "禁用"));
		}
	}
}
