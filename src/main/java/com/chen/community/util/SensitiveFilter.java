package com.chen.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 敏感词过滤器
 * 步骤：1.定义前缀树 2.根据敏感词，初始化前缀树 3.检索过滤
 * @date 2023-08-07 15:21
 **/
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT = "***";
    //根节点
    private TrieNode rootNode = new TrieNode();

    //根据敏感词，初始化树，在服务启动时初始化，只初始化一次
    @PostConstruct
    public void init(){
        //读取敏感词
        try(
                //是字节流
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //转换成字符流，再转换成缓冲流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword=reader.readLine())!=null){
                //添加到前缀树
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败"+e.getMessage());
        }

    }

    //将敏感词添加到前缀树中
    private void addKeyword(String keyword){
        //临时节点（指针）,指向根节点
        TrieNode tempNode = rootNode;
        for (int i = 0;i < keyword.length();i++){
            //获取字符
            char c = keyword.charAt(i);
            //检查子节点中是否已经有该字符节点
            TrieNode subNode = tempNode.getSubNode(c);
            //若无，初始化子节点并添加
            if (subNode == null){
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //若有，则移动指针指向子节点，进入下一循环
            tempNode = subNode;
            //设置结束标志
            if (i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        //判空
        if (StringUtils.isBlank(text)){
            return null;
        }
        //指针1，指向树
        TrieNode tempNode = new TrieNode();
        //指针2，指向字符串
        int begin = 0 ;
        //指针3，指向字符串
        int position = 0;
        //结果，由于存储结果在不停变化，故用变长字符串
        StringBuilder sb = new StringBuilder();

        while (position < text.length()){
            char c = text.charAt(position);
            //跳过特殊符号
            if (isSymbol(c)){
                //若指针1指向根节点，则将符号存入结果，指针2向后走一步
                if (tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头还是中间，指针3都向后走一步
                position++;
                continue;
            }

            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一位置
                position = ++begin;
                //重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                //从begin到position是敏感词，替换
                sb.append(REPLACEMENT);
                //进入下一位置
                begin = ++position;
                //重新指向根节点
                tempNode = rootNode;
            }else if (position + 1 == text.length()) {
                //特殊情况
                //虽然position指向的字符在树中存在，但不是敏感词结尾，并且position到了目标字符串末尾（这个重要）
                //因此begin-position之间的字符串不是敏感词 但begin+1-position之间的不一定不是敏感词
                //所以只将begin指向的字符放入过滤结果
                sb.append(text.charAt(begin));
                //position和begin都指向begin+1
                position = ++begin;
                //再次过滤
                tempNode = rootNode;
            } else {
                //下级节点有且不是结尾,检查下一字符
                position++;
            }
        }
        //当position先到达结尾时，可能会出现有字符未存入的情况
        //sb.append(text.substring(begin));
        return begin < text.length() ? sb.append(text.substring(begin)).toString() : sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c){
        //0x2E80~0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c<0x2E80 || c > 0x9FFF);
    }

    //定义前缀树
    private class TrieNode{
        //关键词结束标识
        private boolean isKeywordEnd = false;
        //子节点(子节点可能不止一个,key为下级字符，value为下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();
        //

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
