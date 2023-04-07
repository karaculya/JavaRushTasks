package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private Path logDir;
    private List<LogEntity> logEntities = new ArrayList<>();
    private DateFormat simpleDateFormat = new SimpleDateFormat("d.M.yyyy H:m:s");

    public LogParser(Path logDir) {
        this.logDir = logDir;
        readLogs();
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before))
                result.add(logEntities.get(i).getIp());
        }
        return result;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user))
                    result.add(logEntities.get(i).getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(event))
                    result.add(logEntities.get(i).getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getStatus().equals(status))
                    result.add(logEntities.get(i).getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getAllUsers() {
        Set<String> allUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            allUsers.add(logEntities.get(i).getUser());
        }
        return allUsers;
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before))
                users.add(logEntities.get(i).getUser());
        }
        return users.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<Event> usersEvents = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user))
                    usersEvents.add(logEntities.get(i).getEvent());
            }
        }
        return usersEvents.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> usersForIP = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getIp().equals(ip))
                    usersForIP.add(logEntities.get(i).getUser());
            }
        }
        return usersForIP;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> loggedUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.LOGIN))
                    loggedUsers.add(logEntities.get(i).getUser());
            }
        }
        return loggedUsers;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> downloadedPluginUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.DOWNLOAD_PLUGIN))
                    downloadedPluginUsers.add(logEntities.get(i).getUser());
            }
        }
        return downloadedPluginUsers;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> wroteMessageUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.WRITE_MESSAGE))
                    wroteMessageUsers.add(logEntities.get(i).getUser());
            }
        }
        return wroteMessageUsers;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> solvedTaskUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.SOLVE_TASK))
                    solvedTaskUsers.add(logEntities.get(i).getUser());
            }
        }
        return solvedTaskUsers;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> solvedTaskUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.SOLVE_TASK)
                        && logEntities.get(i).getEventAdditionalParameter() == task) {
                    solvedTaskUsers.add(logEntities.get(i).getUser());
                }
            }
        }
        return solvedTaskUsers;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> doneTaskUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.DONE_TASK))
                    doneTaskUsers.add(logEntities.get(i).getUser());
            }
        }
        return doneTaskUsers;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> doneTaskUsers = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.DONE_TASK)
                        && logEntities.get(i).getEventAdditionalParameter() == task)
                    doneTaskUsers.add(logEntities.get(i).getUser());
            }
        }
        return doneTaskUsers;
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> datesForUserAndEvent = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user)
                        && logEntities.get(i).getEvent().equals(event))
                    datesForUserAndEvent.add(logEntities.get(i).getDate());
            }
        }
        return datesForUserAndEvent;
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> datesWhenSomethingFailed = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getStatus().equals(Status.FAILED))
                    datesWhenSomethingFailed.add(logEntities.get(i).getDate());
            }
        }
        return datesWhenSomethingFailed;
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> datesWhenErrorHappened = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getStatus().equals(Status.ERROR))
                    datesWhenErrorHappened.add(logEntities.get(i).getDate());
            }
        }
        return datesWhenErrorHappened;
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user)
                        && logEntities.get(i).getEvent().equals(Event.LOGIN)) {
                    dates.add(logEntities.get(i).getDate());
                }
            }
        }
        if (dates.size() == 0)
            return null;
        else {
            Date minDate = dates.iterator().next();
            for (Date date : dates) {
                if (date.getTime() < minDate.getTime())
                    minDate = date;
            }
            return minDate;
        }
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user)
                        && logEntities.get(i).getEvent().equals(Event.SOLVE_TASK)
                        && logEntities.get(i).getEventAdditionalParameter() == task) {
                    dates.add(logEntities.get(i).getDate());
                }
            }
        }
        if (dates.size() == 0)
            return null;
        else {
            Date minDate = dates.iterator().next();
            for (Date date : dates) {
                if (date.getTime() < minDate.getTime())
                    minDate = date;
            }
            return minDate;
        }
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user)
                        && logEntities.get(i).getEvent().equals(Event.DONE_TASK)
                        && logEntities.get(i).getEventAdditionalParameter() == task) {
                    dates.add(logEntities.get(i).getDate());
                }
            }
        }
        if (dates.size() == 0)
            return null;
        else {
            Date minDate = dates.iterator().next();
            for (Date date : dates) {
                if (date.getTime() < minDate.getTime())
                    minDate = date;
            }
            return minDate;
        }
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> datesWhenUserWroteMessage = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user)
                        && logEntities.get(i).getEvent().equals(Event.WRITE_MESSAGE))
                    datesWhenUserWroteMessage.add(logEntities.get(i).getDate());
            }
        }
        return datesWhenUserWroteMessage;
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> datesWhenUserDownloadedPlugin = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user)
                        && logEntities.get(i).getEvent().equals(Event.DOWNLOAD_PLUGIN))
                    datesWhenUserDownloadedPlugin.add(logEntities.get(i).getDate());
            }
        }
        return datesWhenUserDownloadedPlugin;
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        Set<Event> allEvents = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                allEvents.add(logEntities.get(i).getEvent());
            }
        }
        return allEvents.size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> allEvents = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                allEvents.add(logEntities.get(i).getEvent());
            }
        }
        return allEvents;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> eventsForIP = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getIp().equals(ip))
                    eventsForIP.add(logEntities.get(i).getEvent());
            }
        }
        return eventsForIP;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> eventsForUser = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getUser().equals(user))
                    eventsForUser.add(logEntities.get(i).getEvent());
            }
        }
        return eventsForUser;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> failedEvents = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getStatus().equals(Status.FAILED))
                    failedEvents.add(logEntities.get(i).getEvent());
            }
        }
        return failedEvents;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> errorEvents = new HashSet<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getStatus().equals(Status.ERROR))
                    errorEvents.add(logEntities.get(i).getEvent());
            }
        }
        return errorEvents;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        int numberOfAttemptToSolveTask = 0;
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.SOLVE_TASK)
                        && logEntities.get(i).getEventAdditionalParameter() == task)
                    numberOfAttemptToSolveTask++;
            }
        }
        return numberOfAttemptToSolveTask;
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        int numberOfSuccessfulAttemptToSolveTask = 0;
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                if (logEntities.get(i).getEvent().equals(Event.DONE_TASK)
                        && logEntities.get(i).getEventAdditionalParameter() == task)
                    numberOfSuccessfulAttemptToSolveTask++;
            }
        }
        return numberOfSuccessfulAttemptToSolveTask;
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)){
                if (logEntities.get(i).getEvent().equals(Event.SOLVE_TASK)){
                    int task = logEntities.get(i).getEventAdditionalParameter();
                    Integer count = map.containsKey(task) ? map.get(task) : 0;
                    map.put(task, count + 1);
                }
            }
        }
        return map;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < logEntities.size(); i++) {
            if (dateBetweenDates(logEntities.get(i).getDate(), after, before)){
                if (logEntities.get(i).getEvent().equals(Event.DONE_TASK)){
                    int task = logEntities.get(i).getEventAdditionalParameter();
                    Integer count = map.containsKey(task) ? map.get(task) : 0;
                    map.put(task, count + 1);
                }
            }
        }
        return map;
    }

    @Override
    public Set<Object> execute(String query) {
        Set<Object> result = new HashSet<>();
        String field1;
        String field2 = null;
        String value1 = null;
        Date after = null;
        Date before = null;
        Pattern pattern = Pattern.compile("get (ip|user|date|event|status)"
                + "( for (ip|user|date|event|status) = \"(.*?)\")?" 
                + "( and date between \"(.*?)\" and \"(.*?)\")?");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        field1 = matcher.group(1);
        if (matcher.group(2) != null) {
            field2 = matcher.group(3);
            value1 = matcher.group(4);
            if (matcher.group(5) != null) {
                try {
                    after = simpleDateFormat.parse(matcher.group(6));
                    before = simpleDateFormat.parse(matcher.group(7));
                } catch (ParseException e){
                }
            }
        }
        
        if (field2 != null && value1 != null) {
            for (int i = 0; i < logEntities.size(); i++) {
                if (dateBetweenDates(logEntities.get(i).getDate(), after, before)) {
                    if (field2.equals("date")) {
                        try {
                            if (logEntities.get(i).getDate().getTime() == simpleDateFormat.parse(value1).getTime()) {
                                result.add(getCurrentValue(logEntities.get(i), field1));
                            }
                        } catch (ParseException e) {
                        }
                    } else {
                        if (value1.equals(getCurrentValue(logEntities.get(i), field2).toString())) {
                            result.add(getCurrentValue(logEntities.get(i), field1));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < logEntities.size(); i++) {
                result.add(getCurrentValue(logEntities.get(i), field1));
            }
        }
        
        return result;
    }

    private void readLogs() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(logDir)) {
            for (Path file : directoryStream) {
                if (file.toString().toLowerCase().endsWith(".log")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            String[] params = line.split("\t");

                            if (params.length != 5) {
                                continue;
                            }

                            String ip = params[0];
                            String user = params[1];
                            Date date = readDate(params[2]);
                            Event event = readEvent(params[3]);
                            int eventAdditionalParameter = -1;
                            if (event.equals(Event.SOLVE_TASK) || event.equals(Event.DONE_TASK)) {
                                eventAdditionalParameter = readAdditionalParameter(params[3]);
                            }
                            Status status = readStatus(params[4]);

                            LogEntity logEntity = new LogEntity(ip, user, date, event, eventAdditionalParameter, status);
                            logEntities.add(logEntity);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date readDate(String lineToParse) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(lineToParse);
        } catch (ParseException e) {
        }
        return date;
    }

    private Event readEvent(String lineToParse) {
        Event event = null;
        if (lineToParse.contains("SOLVE_TASK")) {
            event = Event.SOLVE_TASK;
        } else if (lineToParse.contains("DONE_TASK")) {
            event = Event.DONE_TASK;
        } else {
            switch (lineToParse) {
                case "LOGIN": {
                    event = Event.LOGIN;
                    break;
                }
                case "DOWNLOAD_PLUGIN": {
                    event = Event.DOWNLOAD_PLUGIN;
                    break;
                }
                case "WRITE_MESSAGE": {
                    event = Event.WRITE_MESSAGE;
                    break;
                }
            }
        }
        return event;
    }

    private int readAdditionalParameter(String lineToParse) {
        if (lineToParse.contains("SOLVE_TASK")) {
            lineToParse = lineToParse.replace("SOLVE_TASK", "").replaceAll(" ", "");
        } else {
            lineToParse = lineToParse.replace("DONE_TASK", "").replaceAll(" ", "");
        }
        return Integer.parseInt(lineToParse);
    }

    private Status readStatus(String lineToParse) {
        Status status = null;
        switch (lineToParse) {
            case "OK": {
                status = Status.OK;
                break;
            }
            case "FAILED": {
                status = Status.FAILED;
                break;
            }
            case "ERROR": {
                status = Status.ERROR;
                break;
            }
        }
        return status;
    }

    private boolean dateBetweenDates(Date current, Date after, Date before) {
        if (after == null) {
            after = new Date(0);
        }
        if (before == null) {
            before = new Date(Long.MAX_VALUE);
        }
        return current.after(after) && current.before(before);
    }

    private Object getCurrentValue(LogEntity logEntity, String field) {
        Object value = null;
        switch (field) {
            case "ip": {
                Command method = new GetIpCommand(logEntity);
                value = method.execute();
                break;
            }
            case "user": {
                Command method = new GetUserCommand(logEntity);
                value = method.execute();
                break;
            }
            case "date": {
                Command method = new GetDateCommand(logEntity);
                value = method.execute();
                break;
            }
            case "event": {
                Command method = new GetEventCommand(logEntity);
                value = method.execute();
                break;
            }
            case "status": {
                Command method = new GetStatusCommand(logEntity);
                value = method.execute();
                break;
            }
        }
        return value;
    }
    
    private class LogEntity {
        private String ip;
        private String user;
        private Date date;
        private Event event;
        private int eventAdditionalParameter;
        private Status status;

        public LogEntity(String ip, String user, Date date, Event event, int eventAdditionalParameter, Status status) {
            this.ip = ip;
            this.user = user;
            this.date = date;
            this.event = event;
            this.eventAdditionalParameter = eventAdditionalParameter;
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public String getUser() {
            return user;
        }

        public Date getDate() {
            return date;
        }

        public Event getEvent() {
            return event;
        }

        public int getEventAdditionalParameter() {
            return eventAdditionalParameter;
        }

        public Status getStatus() {
            return status;
        }
    }

    private abstract class Command {
        protected LogEntity logEntity;

        abstract Object execute();
    }

    private class GetIpCommand extends Command {
        public GetIpCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getIp();
        }
    }

    private class GetUserCommand extends Command {
        public GetUserCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getUser();
        }
    }

    private class GetDateCommand extends Command {
        public GetDateCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getDate();
        }
    }

    private class GetEventCommand extends Command {
        public GetEventCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getEvent();
        }
    }

    private class GetStatusCommand extends Command {
        public GetStatusCommand(LogEntity logEntity) {
            this.logEntity = logEntity;
        }

        @Override
        Object execute() {
            return logEntity.getStatus();
        }
    }
}
