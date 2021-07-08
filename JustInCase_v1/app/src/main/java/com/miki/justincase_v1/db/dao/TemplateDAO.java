package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Template;

import java.util.List;

@Dao
public interface TemplateDAO {

    @Query("SELECT * FROM template")
    List<Template> getAll();

    @Query("SELECT * FROM template ORDER BY templateName")
    List<Template> selectAll();

    @Update
    void update(Template template);

    @Delete
    void delete(Template template);

    @Insert
    void insert(Template template);
}
