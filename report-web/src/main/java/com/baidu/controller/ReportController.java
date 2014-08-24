package com.baidu.controller;

import com.baidu.model.Report;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by edwardsbean on 2014/8/24 0024.
 */

@Controller
public class ReportController {
    @RequestMapping(value="/reports", method= RequestMethod.GET)
    public String  listReports(){
        return null;
    }

    @RequestMapping(value="/reports/new", method=RequestMethod.GET)
    public String newReport(){
        return null;
    }

    @RequestMapping(value="/reports/save", method=RequestMethod.POST)
    public String saveReport(@ModelAttribute("report") Report report){
        return null;
    }

    @RequestMapping(value="/reports/get", method=RequestMethod.GET)
    public String getReport(@RequestParam int reportId){
        return null;
    }
}
