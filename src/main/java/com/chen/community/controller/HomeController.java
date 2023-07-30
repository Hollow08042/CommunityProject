package com.chen.community.controller;

import com.chen.community.entity.DiscussPost;
import com.chen.community.entity.Page;
import com.chen.community.entity.User;
import com.chen.community.service.IDiscussPostService;
import com.chen.community.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 社区首页
 * @date 2023-07-28 17:57
 **/
@Controller
public class HomeController {
    @Autowired
    private IDiscussPostService discussPostService;

    @Autowired
    private IUserService userService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        //方法调用前，springMVC会自动实例化Model和Page，并将Page注入Model中
        //所以在thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> discussPostList = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        //用map来存整合了帖子和用户信息的数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        //遍历帖子，将数据存入map并加入到list集合中
        for (DiscussPost post:discussPostList){
            Map<String, Object> map = new HashMap<>();
            //将帖子存入
            map.put("post",post);
            //根据帖子的用户id查询用户信息
            User user = userService.findUserById(post.getUserId());
            //将用户存入
            map.put("user",user);
            //将map存入集合中
            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
