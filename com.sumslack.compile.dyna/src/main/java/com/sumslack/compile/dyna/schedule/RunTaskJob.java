package com.sumslack.compile.dyna.schedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumslack.compile.dyna.domain.Task;

import cn.hutool.core.util.StrUtil;

public class RunTaskJob implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(RunTaskJob.class) ;
	public static Map<String,Task> tasks = new ConcurrentHashMap<>();
	@Override
	public void run() {
		LinkedBlockingQueue<String> taskQueue = SharedSpace.getTaskQueue() ;
		while (true) {
			try {
				String taskName = taskQueue.poll(Integer.MAX_VALUE,TimeUnit.DAYS);
				if (StrUtil.isNotBlank(taskName)) {
					LOG.info("get " + taskName + " to task_quene ! wil be run!");
					Task task = tasks.get(taskName);
					if (task == null) {
						LOG.error("task " + taskName + " is not found in task cache!");
					} else if (task.getStatus() == 0) {
						LOG.error("task " + taskName + " status is 0 so skip !");
					} else {
						ThreadManager.run(task);
					}
				}
				Thread.sleep(50L);
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("run task fail",e);
			}
		}
	}
}