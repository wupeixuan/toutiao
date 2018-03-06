package com.wpx.dao;

import com.wpx.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 评论
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "user_id, content, created_date, entity_id, entity_type, status";
    String SELECT_FIELDS = "id, "+INSERT_FIELDS;

    //增加一条评论
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);


    //看某个实体多少评论
    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId")int entityId,
                                 @Param("entityType")int entityType);
    //评论数量
    @Select({"select count(id) from ",TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId")int entityId,
                        @Param("entityType")int entityType);

    //删除评论，把status设为1
    @Update({"update ",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId")int entityId,
                      @Param("entityType")int entityType,
                      @Param("status")int status);

}
