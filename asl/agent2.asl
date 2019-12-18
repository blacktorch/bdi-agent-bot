// Agent agent1 in project bdiRobot

/* Initial beliefs and rules */

/* Initial goals */


/*
 * When Robot has started
 */
+start : orientation(Direction) & targetLoc(X,Y)<-
	+orientation(Direction);+currentLoc(0,0);+targetLoc(X,Y);started;!goToTarget.

/*
 * If current location is target location
 */
+!goToTarget : targetLoc(X1,Y1)[source(self)] & currentLoc(X,Y)[source(self)] & (X1 == X & Y1 == Y )
	<-targetReach.

/*
 * Three plans for each direction 
 * North
 */
+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='north') & ((X1 == X) & (Y1 == Y+1))
			   <-!findObstacle; -currentLoc(X,Y);-orientation(Direction);+orientation(north);+currentLoc(X1,Y1);.print('north');forward;!goToTarget.
	
+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
				& (Direction =='north') & ((Y1 == Y) & (X1 == X-1))
				<- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(west);+currentLoc(X1,Y1);.print('west');left;!goToTarget.

+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
				& (Direction =='north') & ((Y1 == Y) & (X1 == X+1))
				<- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(east);+currentLoc(X1,Y1);.print('east');right;!goToTarget.

/*
 * Three plans for each direction 
 * East
 */
+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='east') & ((Y1 == Y) & (X1 == X+1))
			   <- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(east);+currentLoc(X1,Y1);.print('east');forward;!goToTarget.
			   
+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='east') & ((X1 == X) & (Y1 == Y+1))
			   <-!findObstacle; -currentLoc(X,Y);-orientation(Direction);+orientation(north);+currentLoc(X1,Y1);.print('north');left;!goToTarget.

+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='east') & ((X1 == X) & (Y1 == Y-1))
			   <-!findObstacle; -currentLoc(X,Y);-orientation(Direction);+orientation(south);+currentLoc(X1,Y1);.print('south');right;!goToTarget.

/*
 * Three plans for each direction 
 * West
 */
+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='west') & ((Y1 == Y) & (X1 == X-1))
			   <- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(west);+currentLoc(X1,Y1);.print('west');forward;!goToTarget.

+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='west') & ((X1 == X) & (Y1 == Y-1))
			   <-!findObstacle; -currentLoc(X,Y);-orientation(Direction);+orientation(south);+currentLoc(X1,Y1);.print('south');left;!goToTarget.

+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='west') & ((X1 == X) & (Y1 == Y+1))
			   <-!findObstacle; -currentLoc(X,Y);-orientation(Direction);+orientation(north);+currentLoc(X1,Y1);.print('north');right;!goToTarget.

/*
 * Three plans for each direction 
 * South
 */
+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='south') & ((X1 == X) & (Y1 == Y-1))
			   <- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(south);+currentLoc(X1,Y1);.print('south');forward;!goToTarget.

+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='south') & ((Y1 == Y) & (X1 == X+1))
			   <- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(east);+currentLoc(X1,Y1);.print('east');right;!goToTarget.

+!goToTarget : nextLoc(X1,Y1) & currentLoc(X,Y)[source(self)] & orientation(Direction)[source(self)]
			   & (Direction =='south') & ((Y1 == Y) & (X1 == X-1))
			   <- !findObstacle;-currentLoc(X,Y);-orientation(Direction);+orientation(west);+currentLoc(X1,Y1);.print('west');left;!goToTarget.

+!goToTarget 
	<-noplanfound.
	
+!findObstacle : obstacle(Distance) & (Distance > 0 & Distance < 30)
		<- brake.

+!findObstacle : obstacle(Distance) & (Distance > 30)
		<-.print('no obstacle').

+!findObstacle : obstacle(Distance) & (Distance <= 0)
		<-.print('improper voltage').
