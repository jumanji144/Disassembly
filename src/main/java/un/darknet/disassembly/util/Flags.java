package un.darknet.disassembly.util;

/**
 * Small helper wrapper around an int object.
 */
public class Flags {

    public long backing;

    public Flags() {
    }

    private Flags(long backing) {
        this.backing = backing;
    }

    public boolean has(long flag) {
        return (flag & this.backing) == flag;
    }

    public void set(long flag) {
        this.backing |= flag;
    }

    public void unset(long flag) {
        this.backing &= ~flag;
    }

    public long get() {
        return backing;
    }

    public Flags mask(long mask) {
        return new Flags(this.backing & mask);
    }

    public void clear(long flag) {
        this.backing &= ~flag;
    }

    public void clear() {
        this.backing = 0;
    }

    public Flags lshift(long amount) {
        return new Flags(backing << amount);
    }

    public Flags shift(long amount) {
        return new Flags(backing >> amount);
    }

}
