package com.jinesh.jarvis;
import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api")
public class CommandController {
 private final PlanService planner;
 public CommandController(PlanService p){planner=p;}
 @PostMapping("/command") public Map<String,Object> command(@RequestBody Map<String,String> b){
  String text=b.getOrDefault("text",""); Map<String,Object> plan=planner.plan(text);
  String reply=(Boolean)plan.get("confirmationRequired")?"I prepared a plan. I need your confirmation before the final action.":"I can safely start that task.";
  Map<String,Object> out=new HashMap<>(plan); out.put("reply",reply); out.put("text",text); return out;
 }
}