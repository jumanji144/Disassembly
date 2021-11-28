package un.darknet.disassembly.util;

/**
 * Small helper wrapper around an int object.
 */
public class Flags {

    public int backing;

    public Flags() {
    }

    private Flags(int backing) {
        this.backing = backing;
    }

    public boolean has(int flag) {
        return (flag & this.backing) == flag;
    }

    public void set(int flag) {
        this.backing |= flag;
    }

    public int get() {
        return backing;
    }

    public Flags mask(int mask) {
        return new Flags(this.backing & mask);
    }

    public void clear(int flag) {
        this.backing &= ~flag;
    }

    public void clear() {
        this.backing = 0;
    }

    public Flags lshift(int amount) {
        return new Flags(backing << amount);
    }

    public Flags shift(int amount) {
        return new Flags(backing >> amount);
    }

}
