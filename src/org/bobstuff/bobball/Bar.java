/*
  Copyright (c) 2012 Richard Martin. All rights reserved.
  Licensed under the terms of the BSD License, see LICENSE.txt
*/

package org.bobstuff.bobball;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;


public class Bar implements Parcelable {
	private BarDirection barDirection;
	
	private BarSection sectionOne;
	private BarSection sectionTwo;
	
	private boolean active;
	
	public BarSection getSectionOne() {
		return sectionOne;
	}
	
	public BarSection getSectionTwo() {
		return sectionTwo;
	}
	
	public void start(final BarDirection barDirectionIn, final Rect gridSquareFrame) {
		if (active) {
			throw new IllegalStateException("Cannot start an already started bar");
		}
		
		active = true;
		barDirection = barDirectionIn;
		
		if (barDirection == BarDirection.VERTICAL) {
			sectionOne = new BarSection(gridSquareFrame.left, 
										gridSquareFrame.top-1,
										gridSquareFrame.right, 
										gridSquareFrame.top, 
										BarSection.MOVE_UP);
			sectionTwo = new BarSection(gridSquareFrame.left, 
										gridSquareFrame.top+1,
										gridSquareFrame.right,
										gridSquareFrame.bottom,
										BarSection.MOVE_DOWN);
		} else {
			sectionOne = new BarSection(gridSquareFrame.left-1,
										gridSquareFrame.top,
										gridSquareFrame.left, 
										gridSquareFrame.bottom, 
										BarSection.MOVE_LEFT);
			sectionTwo = new BarSection(gridSquareFrame.left+1, 
										gridSquareFrame.top,
										gridSquareFrame.right, 
										gridSquareFrame.bottom, 
										BarSection.MOVE_RIGHT);
		}
	}
	
	public void move() {
		if (!active) {
			return;
		}
		
		if (sectionOne != null) {
			sectionOne.move();
		}
		if (sectionTwo != null) {
			sectionTwo.move();
		}
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean collide(final Ball ball) {
		if (!active) {
			return false;
		}
		
		if (sectionOne != null && ball.collide(sectionOne.getFrame())) {
			sectionOne = null;
			if (sectionTwo == null) {
				active = false;
			}
			return true;
		}
		if (sectionTwo != null && ball.collide(sectionTwo.getFrame())) {
			sectionTwo = null;
			if (sectionOne == null) {
				active = false;
			}
			return true;
		}
		
		if (sectionOne == null && sectionTwo == null) {
			active = false;
		}
		
		return false;
	}
	
	public List<Rect> collide(final List<Rect> collisionRects) {
		boolean sectionOneCollision = false;
		boolean sectionTwoCollision = false;
		
		for (int i=0; i<collisionRects.size(); ++i) {
			Rect collisionRect = collisionRects.get(i);
			if (sectionOne != null && !sectionOneCollision && 
					Rect.intersects(sectionOne.getFrame(), collisionRect)) {
				sectionOneCollision = true;
			}
			if (sectionTwo != null && !sectionTwoCollision &&
					Rect.intersects(sectionTwo.getFrame(), collisionRect)) {
				sectionTwoCollision = true;
			}
		}
		
		if (!sectionOneCollision && !sectionTwoCollision) {
			return null;
		}
		
		List<Rect> sectionCollisionRects = new ArrayList<Rect>(2);
		if (sectionOneCollision) {
			sectionCollisionRects.add(sectionOne.getFrame());
			sectionOne = null;
		} 
		if (sectionTwoCollision) {
			sectionCollisionRects.add(sectionTwo.getFrame());
			sectionTwo = null;
		}
		
		return sectionCollisionRects;
	}

	//implement parcelable

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(barDirection == BarDirection.VERTICAL ? 0 : 1);
		dest.writeInt(active ? 1 : 0);

		dest.writeParcelable(sectionOne, 0);
		dest.writeParcelable(sectionTwo, 0);
	}

	public static final Parcelable.Creator<Bar> CREATOR
			= new Parcelable.Creator<Bar>() {
		public Bar createFromParcel(Parcel in) {
			ClassLoader classLoader = getClass().getClassLoader();

			int bd = in.readInt();
			int active = in.readInt();


			Bar bar = new Bar();
			bar.sectionOne = in.readParcelable(classLoader);
			bar.sectionTwo = in.readParcelable(classLoader);
			bar.barDirection = (bd == 0) ? BarDirection.VERTICAL : BarDirection.HORIZONTAL;
			bar.active = active > 0;

			return bar;
		}

		public Bar[] newArray(int size) {
			return new Bar[size];
		}

	};

}
