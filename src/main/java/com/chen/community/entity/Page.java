package com.chen.community.entity;

/**
 * @Description 封装分页的相关信息
 * @date 2023-07-29 17:11
 **/
public class Page {
    //当前的页码
    private Integer current = 1;
    //显示上限
    private Integer limit = 10;
    //数据的总数（计算总的页码数）
    private Integer rows;
    //查询路径(用于复用分页链接)
    private String path;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (current >=1){
            this.current = current;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit >=1 && limit <=100){
            this.limit = limit;
        }
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows >= 0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     */
    public int getOffset(){
        // current*limit-limit
        return (current - 1)*limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal(){
        if (rows % limit == 0){
            return rows/limit;
        }else {
            return rows/limit+1;
        }
    }

    /**
     * 获取起始页码
     */
    public int getForm(){
        int form = current - 2;
        return form < 1 ? 1 : form;
    }

    /**
     * 获取结束页码
     */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to ;
    }
}
