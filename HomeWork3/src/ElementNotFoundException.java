public class ElementNotFoundException extends NullPointerException {

    int index;

    public ElementNotFoundException(String s) {
        super(s);
    }
    public ElementNotFoundException(String s, int index) {
        super(s);
        this.index = index;
    }
}