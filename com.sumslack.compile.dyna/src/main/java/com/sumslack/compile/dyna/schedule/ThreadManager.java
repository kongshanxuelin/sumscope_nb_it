package com.sumslack.compile.dyna.schedule;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumslack.compile.dyna.domain.Task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

public class ThreadManager {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadManager.class);
	private static final AtomicLong JOB_ID = new AtomicLong();
	
	public synchronized static boolean add(Task task) throws Exception, SchedulerException {
		boolean flag = true;
		try {
			flag = QuartzSchedulerManager.addJob(task);
		} catch (SchedulerException e) {
			flag = false;
			LOG.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}
		return flag;
	}
	
	
	public static void run(Task task) throws Exception {
		//如果是while或者一次性任务将不再添加进来
		if (StrUtil.isBlank(task.getScheduleStr()) || "while".equals(task.getScheduleStr().toLowerCase())) {
			if (TaskRunManager.checkTaskExists(task.getName())) {
				LOG.warn("task " + task.getName() + " has been in joblist so skip it ");
				return;
			}
		}
		String threadName = task.getName() + "@" + JOB_ID.getAndIncrement() + "@" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
		TaskRunManager.runTaskJob(new TaskJob(threadName, task));
	}
	
	public static void removeTaskIfOver(String key) {
		TaskRunManager.removeIfOver(key);
	}
	
	public static void flush(Task oldTask, Task newTask) throws Exception {
		if (oldTask != null && StrUtil.isNotBlank(oldTask.getName())) {

			if (oldTask.getType() == 2) {
				LOG.info("to stop oldTask " + oldTask.getName() + " BEGIN! ");
				stopTaskAndRemove(oldTask.getName());
				LOG.info("to stop oldTask " + oldTask.getName() + " OK! ");
			}
		}

		Thread.sleep(1000L);

		if (newTask != null && StrUtil.isNotBlank(newTask.getName()) && newTask.getStatus() == 1) {
			LOG.info("to start newTask " + newTask.getName() + " BEGIN! ");
			add(newTask);
			LOG.info("to start newTask " + newTask.getName() + " BEGIN! ");
		}
	}
	
	/**
	 * 停止调度任务
	 */
	public static void stopScheduler() {
		try {
			QuartzSchedulerManager.stopScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
	}

	/**
	 * 开启调度任务
	 */
	public static void startScheduler() {
		try {
			QuartzSchedulerManager.startScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
	}
	
	
	private static synchronized void stopTaskAndRemove(String taskName) throws Exception {
		try {
			// 从任务中移除
			try {
				TaskRunManager.stopAll(taskName);
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
				e.printStackTrace();
			}
			// 进行二次停止
			TaskRunManager.stopAll(taskName);
			// 从定时任务中移除
			QuartzSchedulerManager.stopTaskJob(taskName);

		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			throw new Exception(e.getMessage());
		}
	}
}
