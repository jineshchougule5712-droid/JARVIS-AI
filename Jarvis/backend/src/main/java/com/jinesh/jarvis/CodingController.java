package com.jinesh.jarvis;
import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/coding")
public class CodingController {
 @PostMapping("/explain") public Map<String,String> explain(@RequestBody Map<String,String>b){
  return Map.of("status","LLM_READY","message","Connect the configured LLM adapter for detailed code explanation.","input",b.getOrDefault("code",""));
 }
}