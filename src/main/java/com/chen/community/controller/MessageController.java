package com.chen.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.chen.community.entity.Message;
import com.chen.community.entity.Page;
import com.chen.community.entity.User;
import com.chen.community.service.IMessageService;
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
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.model.IModel;

import java.util.*;

/**
 * @Description 私信的表现层
 * @date 2023-08-10 16:22
 **/
@Controller
public class MessageController implements CommunityConstant {
    @Autowired
    private IMessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IUserService userService;

    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        //登录的用户信息
        User user = hostHolder.getUsers();
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        //会话列表
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        //存储会话，未读私信数等
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                //存储每一个会话的要表现的信息
                Map<String, Object> map = new HashMap<>();
                //会话
                map.put("conversation", message);
                //每一会话的未读私信数
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                //每一会话的私信总数
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                //因要获取会话对方的用户头像，故查出该用户
                int target = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(target));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        //查询当前用户所有的未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/letter";
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,
                                  Page page, Model model) {
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        //查询会话的私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                //获取发送信息的人
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        //查询发信人
        User letterTarget = getLetterTarget(conversationId);
        model.addAttribute("target", letterTarget);

        //修改信息状态，提取未读信息，将其状态改成已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId) {
        String ids[] = conversationId.split("_");
        Integer id0 = Integer.parseInt(ids[0]);
        Integer id1 = Integer.parseInt(ids[1]);
        if (hostHolder.getUsers().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUsers().getId().equals(message.getToId())) {
                    if (message.getStatus() == 0) {
                        ids.add(message.getId());
                    }
                }
            }
        }
        return ids;
    }

    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content) {
        User targetUser = userService.findUserByName(toName);
        if (targetUser == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUsers().getId());
        message.setToId(targetUser.getId());
        //拼接会话id
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }

    @GetMapping("/notice/list")
    public String getNoticeList(Model model) {
        //当前用户
        User user = hostHolder.getUsers();

        //查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
            //将content的内容转换为map,再分别放入messageVo中
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            //存入
            messageVo.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            messageVo.put("postId", data.get("postId"));
            //查询通知数量并存入
            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("count", count);
            //查询未读通知数量
            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("unread", unread);
            //存入模板
            model.addAttribute("commentNotice", messageVo);
        }
        //查询点赞类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
            //将content的内容转换为map,再分别放入messageVo中
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            //存入
            messageVo.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            messageVo.put("postId", data.get("postId"));
            //查询通知数量并存入
            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVo.put("count", count);
            //查询未读通知数量
            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            messageVo.put("unread", unread);
            //存入模板
            model.addAttribute("likeNotice", messageVo);
        }
        //查询关注类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
            //将content的内容转换为map,再分别放入messageVo中
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            //存入
            messageVo.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            //查询通知数量并存入
            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            messageVo.put("count", count);
            //查询未读通知数量
            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            messageVo.put("unread", unread);
            //存入模板
            model.addAttribute("followNotice", messageVo);
        }
        //查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    @GetMapping("/notice/detail/{topic}")
    public String getNoticeDetail(@PathVariable("topic")String topic,Model model,Page page) {
        //当前用户
        User user = hostHolder.getUsers();
        //分页信息
        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));
        //查询某个主题的通知列表
        List<Message> notices = messageService.findNotices(user.getId(),topic,page.getOffset(),page.getLimit());
        List<Map<String,Object>> noticeVoList = new ArrayList<>();
        if (notices!=null){
            for (Message notice:notices){
                Map<String,Object> map = new HashMap<>();
                //通知
                map.put("notice",notice);
                //内容
                 //将content的内容转换为map
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                 //存入
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                 //通知的作者（系统用户）
                map.put("fromUser",userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices",noticeVoList);
        //修改信息状态，提取未读信息，将其状态改成已读
        List<Integer> ids = getLetterIds(notices);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        return "/site/notice-detail";
    }
}