package com.wpx.async;


import java.util.HashMap;
import java.util.Map;

/**
 * 事件
 */
public class EventModel {
    private EventType type;//事件类型
    private int actorId;//谁触发的
    private int entityType;//触发的对象
    private int entityId;
    private int entityOwnerId;//对象的拥有者
    private Map<String, String> exts = new HashMap<>();//触发的现场拥有什么数据需要保存下来。

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel() {

    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
