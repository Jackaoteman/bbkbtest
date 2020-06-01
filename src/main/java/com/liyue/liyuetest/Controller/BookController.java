package com.liyue.liyuetest.Controller;

import com.github.pagehelper.PageInfo;
import com.liyue.liyuetest.Common.Result;
import com.liyue.liyuetest.Domain.Company;
import com.liyue.liyuetest.Interface.BookInterface;
import com.liyue.liyuetest.Interface.Commit;
import com.liyue.liyuetest.Utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@Controller
@RequestMapping("/book")
public class BookController {

    @Value("userId")
    private  String userId;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    JtaTransactionManager jtaTransactionManager;


    private  static  final  String PREFXI="user";


    @Autowired
    BookInterface bookInterface;

    @RequestMapping("/user")
    @Commit
    public  String  user(String name){
        System.out.println("随机id="+userId);
        System.out.println(name);
      //  return  "login";
        return   PREFXI+"/user";
    }

    @RequestMapping("/pcLogin")
    public  Result  login(String username, String password, HttpSession session){


        ServletContext application = session.getServletContext();
        @SuppressWarnings("unchecked")
        Map<String, Object> loginMap = (Map<String, Object>) application.getAttribute("loginMap");
        if (loginMap == null) {
            loginMap = new HashMap<String, Object>();
        }
//        for (String key : loginMap.keySet()) {
//            if (login.getUser_id().equals(key)) {
//                if (session.getId().equals(loginMap.get(key))) {
//                    return redirectUrl.append("login.action?mesg=")
//                            .append(URLEncoder.encode(username + "在同一地点重复登录", "UTF-8")).toString();
//                } else {
//                    return redirectUrl.append("login.action?mesg=")
//                            .append(URLEncoder.encode(username + "异地已登录，请先退出登录", "UTF-8")).toString();
//                }
//            }
//        }
//        loginMap.put(login.getUser_id(),session.getId());
//        application.setAttribute("loginMap", loginMap);
//        session.setAttribute("user", user);
//        session.setMaxInactiveInterval(10*60);
        return  null;
    }



    @RequestMapping("/insert")
   // @Async
    @ResponseBody
    public Result  insertBook() throws SystemException, NotSupportedException {
        Company company=new Company();
        company.setName("卢本伟");
        company.setCreateUserId(1L);
        company.setCompanyId(1);
        company.setCreateTime(new Date());
        company.setState(4);
        int num = bookInterface.insert(company);
        Long id = company.getId();

        JedisUtil.add("bababus.com",company.toString());


        return  new Result(true,"000","保存成功",id);
    }

    @RequestMapping("/find")
    @ResponseBody
    public  Result  findBook(HttpServletResponse response){
        Cookie cookie = new Cookie("1", "liyue");
        cookie.setMaxAge(300);
        cookie.setPath("/");
        response.addCookie(cookie);
        Cookie cookie2 = new Cookie("love", "fanghuijuan");
        cookie2.setPath("/");
        cookie2.setMaxAge(10);
        response.addCookie(cookie2);

        Queue  queue=new PriorityBlockingQueue();

        PageInfo pageInfo = bookInterface.page(2, 10, "create_time", null);

        return  new Result(true,"000","查询成功",pageInfo);

    }

}
