package myschedule.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import myschedule.quartz.extra.SchedulerTemplate;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api")
public class MyScheduleRESTServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        String appPath = contextPath + "/api";
        String uri = req.getRequestURI();
        List<?> list = null;
        if (uri.startsWith(appPath + "/scheduler-settings")) {
            list = getSchedulerSettings();
        } else if (uri.startsWith(appPath + "/jobs")) {
            String name = uri.substring((appPath + "/jobs/").length());
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

    private List<SchedulerSettings> getSchedulerSettings() {
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
