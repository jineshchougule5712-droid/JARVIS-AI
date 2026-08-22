package com.jinesh.jarvis;
import org.springframework.stereotype.Service; import java.util.*;
@Service public class PlanService {
 public Map<String,Object> plan(String text){
  String t=text.toLowerCase();
  boolean risky=t.matches(".*\\b(send|delete|purchase|pay|submit application|apply to|call)\\b.*");
  String category=t.contains("job")||t.contains("career")?"JOB":t.contains("code")||t.contains("error")?"CODING":t.contains("remind")?"REMINDER":t.contains("open")||t.contains("search")?"BROWSER":"GENERAL";
  return Map.of("category",category,"confirmationRequired",risky,"action",risky?"PREPARE_AND_CONFIRM":"EXECUTE_SAFE_ACTION");
 }
}