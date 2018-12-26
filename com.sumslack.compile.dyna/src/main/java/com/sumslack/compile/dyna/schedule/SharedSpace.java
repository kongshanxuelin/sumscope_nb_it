package com.sumslack.compile.dyna.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SharedSpace {

	// task_list job 队列
	private static LinkedBlockingQueue<String> taskQueue = new LinkedBlockingQueue<>();

	private static final Map<Long, String> TASK_MESSAGE = new HashMap<>();

	private static final Map<Long, AtomicLong> TASK_SUCCESS = new HashMap<>();

	private static final Map<Long, AtomicLong> TASK_ERR = new HashMap<>();
	
	
	/**
	 * 发布一个taskqueue
	 *
	 * @param name
	 */
	public static void add2TaskQueue(String name) {
		taskQueue.add(name);
	}

	/**
	 * 获得任务队列
	 * 
	 * @return
	 */
	public static LinkedBlockingQueue<String> getTaskQueue() {
		return taskQueue;
	}

	/**
	 * add message for a task
	 * 
	 * @param id
	 * @param message
	 */
	public static void setTaskMessage(Long id, String message) {
		if (id != null)
			TASK_MESSAGE.put(id, message);
	}

	public static String getTaskMessage(Long id) {
		if (id == null) {
			return "";
		}
		return TASK_MESSAGE.get(id);
	}

	public static void removeTaskMessage(Long id) {
		if (id != null)
			TASK_MESSAGE.remove(id);
	}

	private static final AtomicLong COUNT_NULL = new AtomicLong();

	private static AtomicLong getSuccessOrCreate(Long id) {
		if (id == null) {
			return COUNT_NULL;
		}

		AtomicLong result = TASK_SUCCESS.get(id);

		if (result == null) {
			result = new AtomicLong();
			TASK_SUCCESS.put(id, result);
		}
		return result;
	}

	private static AtomicLong getErrOrCreate(Long id) {
		if (id == null) {
			return COUNT_NULL;
		}

		AtomicLong result = TASK_ERR.get(id);

		if (result == null) {
			result = new AtomicLong();
			TASK_ERR.put(id, result);
		}
		return result;
	}

	public static long getSuccess(Long id) {
		return getSuccessOrCreate(id).get();
	}

	public static long getError(Long id) {
		return getErrOrCreate(id).get();
	}

	public static void updateError(Long id) {
		getErrOrCreate(id).incrementAndGet();
	}

	public static void updateSuccess(Long id) {
		getSuccessOrCreate(id).incrementAndGet();
	}

	
}
