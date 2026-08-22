package com.jinesh.jarvis;
import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/memory")
public class MemoryController { final JdbcTemplate db; MemoryController(JdbcTemplate d){db=d;}
 @PostMapping public Map<String,Object> save(@RequestBody Map<String,String>b){db.update("INSERT INTO memories(content) VALUES(?)",b.get("content"));return Map.of("saved",true);}
 @GetMapping public List<Map<String,Object>> all(){return db.queryForList("SELECT * FROM memories ORDER BY id DESC LIMIT 100");}}
