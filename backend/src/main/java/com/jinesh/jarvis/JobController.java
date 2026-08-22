package com.jinesh.jarvis;
import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/jobs")
public class JobController {
 @PostMapping("/score") public Map<String,Object> score(@RequestBody Map<String,String>b){
  String jd=b.getOrDefault("jobDescription","").toLowerCase(); String[] skills={"java","spring boot","sql","postgresql","rest","git","docker"};
  int hit=0; for(String s:skills) if(jd.contains(s)) hit++; double score=Math.round(hit*1000.0/skills.length)/10.0;
  return Map.of("matchScore",score,"recommendApply",score>=70,"missingSkills",Arrays.stream(skills).filter(s->!jd.contains(s)).toList());
 }
 @PostMapping("/application-plan") public Map<String,Object> plan(@RequestBody Map<String,String>b){
  return Map.of("status","READY_FOR_REVIEW","message","Application package can be prepared. Final submission requires confirmation.","jobUrl",b.getOrDefault("url",""));
 }
}