package expression.exceptions;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public char next2() {
        return data.charAt(pos);
    }

    @Override
    public char changePos(int shift) {
        pos += shift;
        if (pos < 0 || pos > data.length()) {
            throw new Error("Wrong shift: StringSource.java");
        }
        return data.charAt(pos - 1);
    }

    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }

    public void print() {
        System.out.println(data + " " +  pos);
    }
}
