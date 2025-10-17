package entities.types;

import java.awt.Color;
import java.awt.Rectangle;

import main.Game;
import world.Camera;

public abstract class Collider {
    Vector pos;

    public Collider(Vector ref) {
        pos = ref;
    }

    public abstract boolean collide(Collider coll);
    public abstract boolean collideSquare(ColliderSquare sqrColl);
    public abstract boolean collideCircle(ColliderCircle cirColl);

    public abstract void debug(Color c);

    public static class ColliderSquare extends Collider {
        protected int offsetX, offsetY, width, height;

        public ColliderSquare(Vector ref, int x, int y, int w, int h) {
            super(ref);
            offsetX = x;
            offsetY = y;
            width = w;
            height = h;
        }

        public boolean collide(Collider coll) {
            return coll.collideSquare(this);
        }
        public boolean collideSquare(ColliderSquare sqrColl){
            return new Rectangle(pos.getX() + offsetX, pos.getY() + offsetY, width, height).intersects(
                new Rectangle(sqrColl.pos.getX() + sqrColl.offsetX,
                sqrColl.pos.getY() + sqrColl.offsetY, sqrColl.width, sqrColl.height)
            );
        }
        public boolean collideCircle(ColliderCircle cirColl){
            return cirColl.collideSquare(this);
        }

        public void debug(Color c){
            Game.gameGraphics.setColor(c);
            Game.gameGraphics.drawRect(pos.getX() - Camera.getX() + offsetX, pos.getY() - Camera.getY() + offsetY, width, height);
        }
    }

    public static class ColliderCircle extends Collider {
        public int radius;

        public ColliderCircle(Vector ref, int r) {
            super(ref);
            radius = r;
        }

        public boolean collide(Collider coll){
            return coll.collideCircle(this);
        }
        
        public boolean collideSquare(ColliderSquare sqrColl){
            int deltaX = Math.abs(pos.getX() + radius / 2 - sqrColl.pos.getX() - sqrColl.offsetX);
            int deltaY = Math.abs(pos.getY() + radius / 2 - sqrColl.pos.getY() - sqrColl.offsetY);
            if(deltaX > sqrColl.width / 2 + radius)
            return false;
            if(deltaY > sqrColl.height / 2 + radius)
            return false;
            if(deltaX > sqrColl.width / 2)
            return true;
            if(deltaY > sqrColl.height / 2)
            return true;
            int sqrX = deltaX - sqrColl.width / 2;
            int sqrY = deltaY - sqrColl.height / 2;
            return sqrX * sqrX + sqrY * sqrY <= radius * radius;
        }

        public boolean collideCircle(ColliderCircle cirColl){
            int deltaR = radius + cirColl.radius;
            return Vector.squareDist(pos.offset(radius / 2, radius / 2), cirColl.pos.offset(cirColl.radius / 2, cirColl.radius / 2)) > deltaR * deltaR;
        }

        public void debug(Color c){
            Game.gameGraphics.setColor(c);
            Game.gameGraphics.drawOval(pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 2 * radius, 2 * radius);
        }
    }

    public static class ColliderNone extends Collider {
        public ColliderNone(Vector ref){
            super(ref);
        }

        public boolean collide(Collider coll){
            return false;
        }

        public boolean collideSquare(ColliderSquare sqrColl){
            return false;
        }

        public boolean collideCircle(ColliderCircle cirColl){
            return false;
        }

        public void debug(Color c){
            Game.gameGraphics.setColor(c);
            Game.gameGraphics.drawRect(pos.getX() - Camera.getX(), pos.getY() - Camera.getY(), 1, 1);
        }
    }
}