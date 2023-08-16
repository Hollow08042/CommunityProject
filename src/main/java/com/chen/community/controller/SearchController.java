package com.chen.community.controller;

import com.chen.community.entity.DiscussPost;
import com.chen.community.entity.Page;
import com.chen.community.service.ILikeService;
import com.chen.community.service.IUserService;
import com.chen.community.service.Impl.ElasticsearchServiceImpl;
import com.chen.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 搜索
 * @date 2023-08-16 18:07
 **/
@Controller
public class SearchController implements CommunityConstant {
    @Autowired
    private ElasticsearchServiceImpl elasticsearchService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILikeService likeService;

    // /search?keyword=?
    @GetMapping("/search")
    public String search(String keyword, Page page, Model model){
        //搜索帖子
        org.springframework.data.domain.Page<DiscussPost> searchResult =
                elasticsearchService.searchDiscussPost(keyword, page.getCurrent()-1, page.getLimit());
        //聚合数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (searchResult!=null){
            for (DiscussPost post : searchResult){
                Map<String,Object> map = new HashMap<>();
                //帖子
                map.put("post",post);
                //作者
                map.put("user",userService.findUserById(post.getUserId()));
                //点赞数量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);
        //分页信息
        page.setPath("/search?keyword="+keyword);
        page.setRows(searchResult==null?0: (int) searchResult.getTotalElements());

        return "/site/search";
    }
}
