package com.wpx.async;

import com.alibaba.fastjson.JSONObject;
import com.wpx.util.JedisAdapter;
import com.wpx.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 序列化，然后放入队列
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 把事件放到队列里面
     *
     * @param model
     * @return
     */
    public boolean fireEvent(EventModel model) {
        try {
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("fireEvent发生异常" + e.getMessage());
            return false;
        }

    }
}
