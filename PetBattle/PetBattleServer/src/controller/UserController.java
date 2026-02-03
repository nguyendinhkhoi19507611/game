package controller;

import java.util.Random;
import bean.UserInfo;
import bean.UserLogin;
import game.DB;
import game.Player;
import game.PlayerMgr;
import pers.jc.network.SocketComponent;
import pers.jc.network.SocketMethod;
import pers.jc.sql.SQL;
import pers.jc.sql.Transaction;
import pers.jc.util.JCLogger;
import result.RequestResult;

@SocketComponent("UserController")
public class UserController {
	
	@SocketMethod
	public static RequestResult login(Player player, String username, String password) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RequestResult requestResult = new RequestResult();
		UserLogin userLogin = DB.curd.selectOne(UserLogin.class, new SQL(){{
			WHERE("username=" + PARAM(username));
		}});
		if (userLogin == null) {
			requestResult.setMsg("该账号未注册");
			return requestResult; 
		}
		if (!userLogin.getPassword().equals(password)) {
			requestResult.setMsg("\u0054\u00e0\u0069\u0020\u006b\u0068\u006f\u1ea3\u006e\u0020\u0068\u006f\u1eb7\u0063\u0020\u006d\u1ead\u0074\u0020\u006b\u0068\u1ea9\u0075\u0020\u006b\u0068\u00f4\u006e\u0067\u0020\u006b\u0068\u1edb\u0070");
			return requestResult; 
		}
		UserInfo userInfo = DB.curd.selectOne(UserInfo.class, new SQL(){{
			WHERE("id=" + PARAM(userLogin.getId()));
		}});
		if (userInfo == null) {
			requestResult.setMsg("\u004c\u1ea5\u0079\u0020\u0074\u0068\u00f4\u006e\u0067\u0020\u0074\u0069\u006e\u0020\u006e\u0067\u01b0\u1edd\u0069\u0020\u0064\u00f9\u006e\u0067\u0020\u0074\u0068\u1ea5\u0074\u0020\u0062\u1ea1\u0069");
		} else {
			synchronized ("login") {
				Player user = (Player) PlayerMgr.get(userInfo.getId());
				if (user == null) {
					player.id = userInfo.getId();
					player.userInfo = userInfo;
					PlayerMgr.add(player);
					requestResult.setCode(200);
					requestResult.setData(userInfo);
					requestResult.setMsg("\u0110\u0103\u006e\u0067\u0020\u006e\u0068\u1ead\u0070\u0020\u0074\u0068\u00e0\u006e\u0068\u0020\u0063\u00f4\u006e\u0067");
					JCLogger.info("(ID:" + userInfo.getId() + ")[" + userInfo.getNickname() + "]\u0110\u0103\u006e\u0067\u0020\u006e\u0068\u1ead\u0070\u0020\u0074\u0072\u00f2\u0020\u0063\u0068\u01a1\u0069");
				} else {
					requestResult.setMsg("\u0054\u00e0\u0069\u0020\u006b\u0068\u006f\u1ea3\u006e\u0020\u0111\u0061\u006e\u0067\u0020\u0111\u01b0\u1ee3\u0063\u0020\u0073\u1eed\u0020\u0064\u1ee5\u006e\u0067");
				}
			}
		}
		return requestResult;
	}
	
	@SocketMethod
	public RequestResult register(String username, String password) {
		RequestResult requestResult = new RequestResult();
		UserLogin userLogin = DB.curd.selectOne(UserLogin.class, new SQL(){{
			WHERE("username=" + PARAM(username));
		}});
		if (userLogin != null) {
			requestResult.setMsg("该账号已被注册");
			return requestResult;
		}
		UserInfo user_info = new UserInfo();
		new Transaction(DB.curd.getAccess()) {
			@Override
			public void run() throws Exception {
				UserLogin user_login = new UserLogin();
				user_login.setUsername(username);
				user_login.setPassword(password);
				insertAndGenerateKeys(user_login);
				user_info.setId(user_login.getId());
				user_info.setNickname("\u004e\u0067\u01b0\u1edd\u0069\u0020\u0063\u0068\u01a1\u0069" + user_login.getId());
				if (new Random().nextInt(100) < 50) {
					user_info.setGender(1);
					user_info.setAvatarUrl("Texture/Icon/HeadPhoto/6901");
				} else {
					user_info.setGender(2);
					user_info.setAvatarUrl("Texture/Icon/HeadPhoto/6902");
				}
				insert(user_info);
				commit();
			}
			@Override
			public void success() {
				JCLogger.info("(ID:" + user_info.getId() + ")[" + user_info.getNickname() + "]\u0110\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u0074\u0068\u00e0\u006e\u0068\u0020\u0063\u00f4\u006e\u0067");
				requestResult.setCode(200);
				requestResult.setMsg("\u0110\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u0074\u0068\u00e0\u006e\u0068\u0020\u0063\u00f4\u006e\u0067");
			}
			@Override
			public void fail() {
				requestResult.setMsg("\u0110\u0103\u006e\u0067\u0020\u006b\u00fd\u0020\u0074\u0068\u1ea5\u0074\u0020\u0062\u1ea1\u0069");
			}
		};
		return requestResult;
	}
}
