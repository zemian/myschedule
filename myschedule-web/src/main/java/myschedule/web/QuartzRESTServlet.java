package myschedule.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import myschedule.quartz.extra.SchedulerTemplate;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuartzRESTServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        List<?> list = null;
        if (uri.startsWith("/myschedule/api/schedulers")) {
            list = getSchedulers();
        } else if (uri.startsWith("/myschedule/api/jobs")) {
            String name = uri.substring("/myschedule/api/jobs/".length());
            list = getJobs(name);
        }

        // Send JSON output result.
        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        Gson gson = new GsonBuilder().create();
        JsonArray jarray = gson.toJsonTree(list).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("items", jarray);
        String output = jsonObject.toString();
        out.print(output);
    }

    private List<SchedulerSettings> getSchedulers() {
        MySchedule mySchedule = MySchedule.getInstance();
        List<String> names = mySchedule.getSchedulerSettingsNames();
        List<SchedulerSettings> result = new ArrayList<>();
        for (String name : names) {
            result.add(mySchedule.getSchedulerSettings(name));
        }
        return result;
    }
    private List<JobDetail> getJobs(String name) {
        MySchedule mySchedule = MySchedule.getInstance();
        SchedulerTemplate scheduler = mySchedule.getScheduler(name);
        List<JobDetail> result = new ArrayList<>();
        for (Trigger trigger : scheduler.getAllTriggers()) {
            result.add(scheduler.getJobDetail(trigger.getJobKey()));
        }
        return result;
    }
}
