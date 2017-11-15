package com.clownvin.lumina.entity;

public class TileEntity extends StaticEntity {
	protected final String texture;
	
	public TileEntity(float x, float y, String texture) {
		super(x, y);
		this.texture = texture;
	}
	
	@Override
	public String getTexture() {
		return texture;
	}
}
