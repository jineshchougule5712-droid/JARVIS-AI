package com.jinesh.jarvis;
import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/reminders")
public class ReminderController { final JdbcTemplate db; ReminderController(JdbcTemplate d){db=d;}
 @PostMapping public Map<String,Object> create(@RequestBody Map<String,String>b){db.update("INSERT INTO reminders(title,remind_at) VALUES(?,?)",b.get("title"),java.sql.Timestamp.valueOf(b.get("remindAt")));return Map.of("created",true);}
 @GetMapping public List<Map<String,Object>> all(){return db.queryForList("SELECT * FROM reminders WHERE completed=false ORDER BY remind_at");}}
