package com.wpx.service;

import com.github.pagehelper.PageHelper;
import com.wpx.dao.NewsDAO;
import com.wpx.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xyuser on 2017/4/21.
 */
@Service
public class ToutiaoService {
    public String say(){
        return "This is from ToutiaoService";
    }

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }
}
