/*
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.noosa;

import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Quad;

public class SystemTextLine extends Visual {

	public SmartTexture texture;
	protected RectF frame;
	
	protected float[] vertices;
	protected FloatBuffer verticesBuffer;
	
	protected boolean dirty;
	
	public SystemTextLine() {
		super( 0, 0, 0, 0 );
		
		vertices = new float[16];
		verticesBuffer = Quad.create();
	}
		
	public SystemTextLine(Bitmap bitmap) {
		this();
		texture = new SmartTexture(bitmap);
		frame( new RectF( 0, 0, 1, 1 ) );
	}

	@Override
	public void destroy() {
		super.destroy();
		if(texture != null) {
			texture.delete();
			texture = null;
		}
	}

	public void texture( Object tx ) {
		texture = tx instanceof SmartTexture ? (SmartTexture)tx : TextureCache.get( tx );
		frame( new RectF( 0, 0, 1, 1 ) );
	}
	
	public void frame( RectF frame ) {
		this.frame = frame;
		
		width = frame.width() * texture.width;
		height = frame.height() * texture.height;
		
		updateFrame();
		updateVertices();
	}
	
	public void frame( int left, int top, int width, int height ) {
		frame( texture.uvRect( left, top, left + width, top + height ) );
	}
	
	public RectF frame() {
		return new RectF( frame );
	}

	public void copy( SystemTextLine other ) {
		texture = other.texture;
		frame = new RectF( other.frame );
		
		width = other.width;
		height = other.height;
		
		updateFrame();
		updateVertices();
	}
	
	protected void updateFrame() {
		vertices[2]		= frame.left;
		vertices[6]		= frame.right;
		vertices[10]	= frame.right;
		vertices[14]	= frame.left;

		vertices[3]		= frame.top;
		vertices[7]		= frame.top;
		vertices[11]	= frame.bottom;
		vertices[15]	= frame.bottom;
		
		dirty = true;
	}
	
	protected void updateVertices() {
		
		vertices[0] 	= 0;
		vertices[1] 	= 0;
		
		vertices[4] 	= width;
		vertices[5] 	= 0;
		
		vertices[8] 	= width;
		vertices[9] 	= height;
		
		vertices[12]	= 0;
		vertices[13]	= height;
		
		dirty = true;
	}
	
	@Override
	public void draw() {
		if(texture == null) {
			//GLog.i("null texture");
			return;
		}
		
		super.draw();

		NoosaScript script = NoosaScript.get();

		texture.bind();
		
		script.camera( camera() );
		
		script.uModel.valueM4( matrix );
		script.lighting( 
			rm, gm, bm, am, 
			ra, ga, ba, aa );
		
		if (dirty) {
			verticesBuffer.position( 0 );
			verticesBuffer.put( vertices );
			dirty = false;
		}
		script.drawQuad( verticesBuffer );	
	}
}
