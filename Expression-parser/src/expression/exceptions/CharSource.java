package expression.exceptions;

public interface CharSource {
    boolean hasNext();
    char next();
    char next2();
    void print();
    IllegalArgumentException error(final String message);
}
