package com.clownvin.lumina.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class TaskManager {
	private static final LinkedList<Task> taskList = new LinkedList<Task>();
	
	public static Task startTask(Task task) {
		taskList.add(task);
		return task;
	}
	
	public static void doTasks() {
		List<Task> toRemove = new ArrayList<Task>();
		for (Task task : taskList) {
			task.run();
			if (task.finished) {
				toRemove.add(task);
			}
		}
		taskList.removeAll(toRemove);
	}
}
