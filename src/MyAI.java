// ======================================================================
// FILE:        MyAI.java
//
// AUTHOR:      Abdullah Younis
//
// DESCRIPTION: This file contains your agent class, which you will
//              implement. You are responsible for implementing the
//              'getAction' function and any helper methods you feel you
//              need.
//
// NOTES:       - If you are having trouble understanding how the shell
//                works, look at the other parts of the code, as well as
//                the documentation.
//
//              - You are only allowed to make changes to this portion of
//                the code. Any changes to other portions of the code will
//                be lost when the tournament runs your code.
// ======================================================================

import java.util.*;

public class MyAI extends Agent {
	private enum Direction {
		EAST, NORTH, WEST, SOUTH;
		private static Direction[] vals = Direction.values();

		public Direction next1() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
		public Direction next2(){
			return vals[(this.ordinal() + vals.length - 1) % vals.length];
		}
	}

	private int x, y;
	private Direction direction;
	private List<int[]> path;
	private boolean goBack;
	private boolean isClimb;


	public MyAI() {
		x = 0;
		y = 0;
		direction = Direction.EAST;
		path = new ArrayList<>();
	}

	public Action getAction(
			boolean stench,
			boolean breeze,
			boolean glitter,
			boolean bump,
			boolean scream
	) {
		if (glitter) {
			goBack = true;
			return Action.GRAB;
		}
		if (breeze || stench) {
			goBack = true;
		}

		if (goBack) {
			if(path.isEmpty() && isClimb == true){
				return Action.CLIMB;
			}
			if (path.isEmpty()){
				goBack = false;
				isClimb = true;
				direction = Direction.NORTH;
				return Action.TURN_RIGHT;
			}
			int[] prev = path.get(path.size() - 1);
			Direction nextDirection;
			if (x - prev[0] > 0) {
				nextDirection = Direction.WEST;
			} else if (y - prev[1] > 0) {
				nextDirection = Direction.SOUTH;
			} else if (x - prev[0] < 0) {
				nextDirection = Direction.EAST;
			}  else {
				nextDirection = Direction.NORTH;
			}

			if (direction != nextDirection) {
				direction = direction.next1();
				return Action.TURN_LEFT;
			} else {
				path.remove(path.size() - 1);
				x = prev[0];
				y = prev[1];
				return Action.FORWARD;
			}
		}
		if (bump && isClimb) {
			x = path.get(path.size() - 1)[0];
			y = path.get(path.size() - 1)[1];
			path.remove(path.size() - 1);
			direction = direction.next2();
			return Action.TURN_RIGHT;
		}
		if (bump && !isClimb) {
			x = path.get(path.size() - 1)[0];
			y = path.get(path.size() - 1)[1];
			path.remove(path.size() - 1);
			direction = direction.next1();
			return Action.TURN_LEFT;
		}

		path.add(new int[] {x, y});
		switch (direction) {
			case EAST:
				x++;
				break;
			case WEST:
				x--;
				break;
			case NORTH:
				y++;
				break;
			case SOUTH:
				y--;
				break;
			default:
				break;
		}
		return Action.FORWARD;
	}
}
