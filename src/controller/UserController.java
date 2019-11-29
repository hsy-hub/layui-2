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
    public Map<String,String> loginlayui(@RequestBody User user){

        HttpSession session = request.getSession();
        User loginUser = userDao.login(user);
        Map<String, String> msg = new HashMap<>();
        if (loginUser != null){
            session.setAttribute("user", loginUser);
            msg.put("msg", "success");
            return msg;
        }else{
            msg.put("msg", "false");
            return msg;
        }
    }

    @RequestMapping("/back.action")
    public String back(){
        return "back";
    }

    @RequestMapping("/userListPage.action")
    public String userListPage(){
        return "userList";
    }


    @RequestMapping("/userList.action")
    @ResponseBody
    public Map<String,Object> userList(){
        List<User> userList = userDao.getUserList();
        return Tool.testLayui(userList,0,0);
    }


    @RequestMapping("/userList2.action")
    @ResponseBody
    public Map<String,Object> UserList2(int page,int limit,User user){
        HashMap<String,Object> map = new HashMap<>();
        int pagestart = (page - 1) * limit;
        map.put("pagestart",pagestart);
        map.put("size",limit);
        map.put("name",user.getName());
        map.put("address",user.getAddress());
        List<User> userList = userDao.getUserList2(map);
        Integer pagecount = userDao.userCount();
        Map<String,Object> returnTable = Tool.testLayui(userList,page,limit);
        returnTable.put("count",pagecount);
        return returnTable;
    }


    @RequestMapping("/updateUserList.action")
    @ResponseBody
    public int updateUserList(User user){
        return userDao.updateUserList(user);
    }

    @RequestMapping("/delete.action")
    public  @ResponseBody int delete(String ids){
        boolean d = ids.endsWith(",");
        if(d){
            ids = ids.substring(0,ids.length()-1);
        }
        String[] all = ids.split(",");

        int result = 0;
        for (String id : all){
            result = userDao.delete(Integer.parseInt(id));
        }
       return result ;
    }

    @RequestMapping("/add.action")
    public String add(){
        return "add";
    }

    @RequestMapping("/addUser.action")
    @ResponseBody
    public Map<String,String> addUser(@RequestBody @RequestParam("file") MultipartFile pictureFile,User user,int id) throws IOException {
        Map<String, String> msg = new HashMap<>();
        String filname = UUID.randomUUID().toString().replaceAll("-","");
        String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
        filname = filname +"."+ extension;
        pictureFile.transferTo(new File("D:\\upload\\" + filname));
        user.setHeadpath(filname);
        user.setId(id);
        int add = userDao.add(user);
        if (add >0){
            msg.put("msg", "success");
            return msg;
        }else{
            msg.put("msg", "false");
            return msg;
        }
    }

    @RequestMapping("/uploadFile.action")
    @ResponseBody
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile pictureFile,Integer id) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String filname = UUID.randomUUID().toString().replaceAll("-","");
        String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
        filname = filname +"."+ extension;
        pictureFile.transferTo(new File("D:\\upload\\" + filname));
        User user = new User();
        user.setHeadpath(filname);
        user.setId(id);
        int i = userDao.updateHeadPath(user);
        map.put("res",i);
        return map;
    }
}
