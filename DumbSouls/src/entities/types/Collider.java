package entities.types;

import java.awt.Rectangle;

public abstract class Collider {
    Vector pos;

    public Collider(Vector ref) {
        pos = ref;
    }

    public abstract boolean collide(Collider coll);
    public abstract boolean collideSquare(ColliderSquare sqrColl);
    public abstract boolean collideCircle(ColliderCircle cirColl);

    public abstract void update(int... args);

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

        public void update(int... args) {
            offsetX = args[0];
            offsetY = args[1];
            width = args[2];
            height = args[3];
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
        
        public void update(int... args) {
            radius = args[0];
        }
    }
}