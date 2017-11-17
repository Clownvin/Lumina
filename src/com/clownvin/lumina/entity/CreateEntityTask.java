package com.clownvin.lumina.entity;

import com.clownvin.lumina.task.Task;

public abstract class CreateEntityTask extends Task {
	protected Entity entity = null;

	public Entity getEntity() {
		while (!finished) {
			synchronized (this) {
				try {
					this.wait(10);
				} catch (InterruptedException e) {
				}
			}
		}
		return entity;
	}
}
