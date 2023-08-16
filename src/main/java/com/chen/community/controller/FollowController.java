package com.chen.community.controller;

import com.chen.community.Event.EventProducer;
import com.chen.community.annotation.LoginRequired;
import com.chen.community.entity.Event;
import com.chen.community.entity.Page;
import com.chen.community.entity.User;
import com.chen.community.service.IFollowService;
import com.chen.community.service.IUserService;
import com.chen.community.util.CommunityConstant;
import com.chen.community.util.CommunityUtil;
import com.chen.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description 关注的表现层
 * @date 2023-08-12 18:59
 **/
@Controller
public class FollowController implements CommunityConstant {
    @Autowired
    private IFollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IUserService userService;
    @Autowired
    private EventProducer eventProducer;

    @LoginRequired
    @PostMapping("/follow")
    @ResponseBody
    public String follow(Integer entityType,Integer entityId){
        User user = hostHolder.getUsers();

        followService.follow(user.getId(), entityType,entityId);

        //触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUsers().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0,"已关注！");
    }

    @LoginRequired
    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(Integer entityType,Integer entityId){
        User user = hostHolder.getUsers();

        followService.unfollow(user.getId(), entityType,entityId);

        return CommunityUtil.getJSONString(0,"已取消关注！");
    }

    @GetMapping("/followees/{userId}")
    public String getFolowees(@PathVariable("userId")Integer userId, Page page, Model model){
        //查出当前用户
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        //设置分页信息
        page.setPath("/followees"+userId);
        page.setLimit(5);
        page.setRows((int) followService.findFolloweeCount(userId,ENTITY_TYPE_USER));
        //查询关注列表
        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        // 将关注状态存入
        if (userList!=null){
            for (Map<String, Object> map:userList){
                User u = (User) map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);
        return "/site/followee";
    }

    @GetMapping("/followers/{userId}")
    public String getFolowers(@PathVariable("userId")Integer userId, Page page, Model model){
        //查出当前用户
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        //设置分页信息
        page.setPath("/followers"+userId);
        page.setLimit(5);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER,userId));
        //查询关注列表
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        // 将关注状态存入
        if (userList!=null){
            for (Map<String, Object> map:userList){
                User u = (User) map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);
        return "/site/follower";
    }

    //关注状态
    private Boolean hasFollowed(Integer userId){
        if (hostHolder.getUsers().getId()==null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUsers().getId(),ENTITY_TYPE_USER,userId);
    }
}
