package controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.User;
import service.UserDao;
import tool.Tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    UserDao userDao;
    @Autowired
    HttpServletRequest request;

    @RequestMapping("/loginlayui.action")
    @ResponseBody
    public Map<String, String> loginlayui(@RequestBody User user) {

        HttpSession session = request.getSession();
        User loginUser = userDao.login(user);
        Map<String, String> msg = new HashMap<>();
        if (loginUser != null) {
            session.setAttribute("user", loginUser);
            msg.put("msg", "success");
            return msg;
        } else {
            msg.put("msg", "false");
            return msg;
        }
    }

    @RequestMapping("/back.action")
    public String back() {
        return "back";
    }

    //点击列表一跳转至列表页面
    @RequestMapping("/userListPage.action")
    public String userListPage() {
        return "userList";
    }

    //不带分页的列表
    @RequestMapping("/userList.action")
    @ResponseBody
    public Map<String, Object> userList() {
        List<User> userList = userDao.getUserList();
        return Tool.testLayui(userList, 0, 0);
    }


    @RequestMapping("/userList2.action")
    @ResponseBody
    public Map<String, Object> UserList2(int page, int limit, User user) {
        HashMap<String, Object> map = new HashMap<>();
        int pagestart = (page - 1) * limit;
        map.put("pagestart", pagestart);
        map.put("size", limit);
        map.put("name", user.getName());//查询条件
        map.put("address", user.getAddress());
        List<User> userList = userDao.getUserList2(map);
        Integer pagecount = userDao.userCount();
        Map<String, Object> returnTable = Tool.testLayui(userList, page, limit);
        returnTable.put("count", pagecount);
        return returnTable;
    }


    @RequestMapping("/updateUserList.action")
    @ResponseBody
    public int updateUserList(User user) {
        return userDao.updateUserList(user);
    }

    @RequestMapping("/delete.action")
    public @ResponseBody
    int delete(String ids) {
        boolean d = ids.endsWith(",");
        if (d) {
            ids = ids.substring(0, ids.length() - 1);
        }
        String[] all = ids.split(",");

        int result = 0;
        for (String id : all) {
            result = userDao.delete(Integer.parseInt(id));
        }
        return result;
    }

    @RequestMapping("/add.action")
    public String add() {
        return "add";
    }

    @RequestMapping("/addUser.action")
    @ResponseBody
    public int addUser(@RequestBody User user) throws IOException {
        Map<String, Object> map = new HashMap<>();
        int add = userDao.add(user);
        return add;

    }

    @RequestMapping("/uploadFile.action")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile pictureFile, Integer id) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String filname = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
        filname = filname + "." + extension;
        String path = "D:\\upload";
        User user = new User();
        user.setHeadpath(filname);
        user.setId(id);
        int i = userDao.updateHeadPath(user);
        if (i > 0) {       //当数据库有记录时才上传文件
            pictureFile.transferTo(new File("D:\\upload\\" + filname));
        }
        File dir = new File(path, filname);
        if (dir.exists()) {    //当有文件时上传记录
            i = userDao.updateHeadPath(user);
        }
        if (dir.exists() && i < 1) {     //数据库有记录但是文件上传失败
            dir.delete();
            map.put("msg", "上传失败");
            map.put("code", 1);
        } else if (!dir.exists() && i > 0) {    //数据库无记录但是文件上传成功
            user.setHeadpath(null);
            userDao.updateHeadPath(user);
            dir.delete();
            map.put("msg", "上传失败");
            map.put("code", 1);
        } else if (!dir.exists() && i < 1) {      //数据库无记录文件也未上传成功
            dir.delete();
            map.put("msg", "上传失败");
            map.put("code", 1);
        } else {             //数据库有记录文件也上传成功
            dir.mkdirs();
            map.put("msg", "上传成功");
            map.put("code", 0);
            map.put("src", "http://localhost:8086/pic/" + filname);
        }
        return map;
    }
}
