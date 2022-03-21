package expression.exceptions;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test2(String expected) {
        String cur = "";
        cur += ch;
        if (!source.hasNext()) {
            return false;
        }
        cur += source.next2();
        return cur.equals(expected);
    }

    protected boolean take2(String expected) {
        if (test2(expected)) {
            take();
            take();
            return true;
        }
        return false;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean isWhitespace() {
        if (Character.isWhitespace(ch)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean testEOF() {
        return test(END);
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected void print() {
        source.print();
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
