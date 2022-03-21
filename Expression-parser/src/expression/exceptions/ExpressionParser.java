package expression.exceptions;

import expression.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser implements TripleParser {
    @Override
    public PartOfExpression parse(final String expression) {
        return parse(new StringSource(expression));
    }

    public PartOfExpression parse(CharSource expression) {
        return new ParserExpression(expression).parseExpression();
    }

    private static class ParserExpression extends BaseParser {
        private int bal = 0;

        public ParserExpression(final CharSource source) {
            super(source);
        }

        public PartOfExpression parseExpression() {
            skipWhitespace();
            final PartOfExpression result = parseValue();
            skipWhitespace();
            if (eof()) {
                return result;
            }
            throw error("End of Expression expected");
        }

        private void skipWhitespace() {
            while (isWhitespace()) {
                // do nothing
            }
        }

        private void checkClosingBracket() {
            if (!take(')')) {
                throw new BracketsException("No closing parenthesis");
            }
            bal--;
            if (bal < 0) {
                print();
                throw new BracketsException("No opening parenthesis");
            }
        }

        private PartOfExpression parseValue() {
            PartOfExpression curExpr = parseBrackets();
            if (!testEOF()) {
                throw error("program failed");
            }
            return curExpr;
        }

        private boolean checkOperation() {
            return test('+') || test('-') ||
                    test('*') || test('/') ||
                    test('>') || test('<');
        }

        private PartOfExpression parseBrackets() {
            List<PartOfExpression> curExpr = new ArrayList<>();
            boolean isFirst = true;
            while (true) {
                skipWhitespace();
                if (testEOF()) {
                    break;
                } else if (test(')')) {
                    if (bal == 0) {
                        throw new BracketsException("No opening parenthesis");
                    }
                    break;
                } else if (take('(')) {
                    bal++;
                    curExpr.add(parseBrackets());
                    checkClosingBracket();
                } else if (between('0', '9')) {
                    curExpr.add(parseConst(false));
                } else if (between('x', 'z')) {
                    curExpr.add(parseVariable());
                } else if (take('a')) {
                    expect("bs");
                    checkAbs();
                    curExpr.add(new CheckedAbs(parseUnaryOperation()));
                } else if (take('-')) {
                    if (isFirst) {
                        if (between('0', '9')) {
                            curExpr.add(parseConst(true));
                        } else {
                            curExpr.add(new CheckedNegate(parseUnaryOperation()));
                        }
                    } else {
                        addBinOper(curExpr, "-");
                    }
                } else if (take('+')) {
                    addBinOper(curExpr, "+");
                } else if (take('*')) {
                    if (take('*')) { // pow
                        addBinOper(curExpr, "**");
                    } else { // multiply
                        addBinOper(curExpr, "*");
                    }
                } else if (take('/')) {
                    if (take('/')) { // log
                        addBinOper(curExpr, "//");
                    } else { // divide
                        addBinOper(curExpr, "/");
                    }
                } else if (take('>')) {
                    expect('>');
                    if (take('>')) {
                        addBinOper(curExpr, ">>>");
                    } else {
                        addBinOper(curExpr, ">>");
                    }
                } else if (take('<')) {
                    expect('<');
                    addBinOper(curExpr, "<<");
                } else {
                    throw new ParsingException("Unknown symbol");
                }
                isFirst = false;
            }
            if (curExpr.size() != 1) {
                if (curExpr.size() > 1) {
                    throw new ParsingException("Missing binary operation");
                }
                throw new BracketsException("Empty brackets");
            }
            return curExpr.get(0);
        }

        private PartOfExpression parse3Prior() {
            skipWhitespace();
            if (testEOF()) {
                throw new ArgumentException("Missing end argument");
            } else if (take('(')) {
                bal++;
                PartOfExpression t = parseBrackets();
                checkClosingBracket();
                return t;
            } else if (between('0', '9')) {
                return parseConst(false);
            } else if (between('x', 'z')) {
                return parseVariable();
            } else if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                } else {
                    return new CheckedNegate(parseUnaryOperation());
                }
            } else if (take('a')) {
                expect("bs");
                checkAbs();
                return new CheckedAbs(parseUnaryOperation());
            } else {
                if (take(')')) {
                    throw new ParsingException("Missing last argument");
                }
                if (checkOperation()) {
                    throw new ParsingException("Missing middle argument");
                }
                throw new ParsingException("Unknown symbol");
            }
        }

        private PartOfExpression parse2Prior() { // multiply and divide
            List<PartOfExpression> curExpr = new ArrayList<>();
            boolean isFirst = true;
            while (true) {
                skipWhitespace();
                if (testEOF()) {
                    break;
                } else if (take('(')) {
                    bal++;
                    curExpr.add(parseBrackets());
                    checkClosingBracket();
                } else if (test(')')) {
                    if (bal == 0) {
                        throw new BracketsException("No opening parenthesis");
                    }
                    break;
                } else if (between('0', '9')) {
                    curExpr.add(parseConst(false));
                } else if (between('x', 'z')) {
                    curExpr.add(parseVariable());
                } else if (test('-')) {
                    if (isFirst) {
                        take();
                        if (between('0', '9')) {
                            curExpr.add(parseConst(true));
                        } else {
                            curExpr.add(new CheckedNegate(parseUnaryOperation()));
                        }
                    } else {
                        break;
                    }
                } else if (take('a')) {
                    expect("bs");
                    checkAbs();
                    curExpr.add(new CheckedAbs(parseUnaryOperation()));
                } else if (take2("**")) { // pow
                    addBinOper(curExpr, "**");
                } else if (take2("//")) { // log
                    addBinOper(curExpr, "//");
                } else if (checkOperation()) {
                    break;
                } else {
                    throw new ParsingException("Unknown symbol");
                }
                isFirst = false;
            }
            if (curExpr.size() != 1) {
                throw new ParsingException("Missing argument");
            }
            return curExpr.get(0);
        }

        private PartOfExpression parse1Prior() { // add and subtract
            List<PartOfExpression> curExpr = new ArrayList<>();
            boolean isFirst = true;
            while (true) {
                skipWhitespace();
                if (testEOF()) {
                    break;
                } else if (take('(')) {
                    bal++;
                    curExpr.add(parseBrackets());
                    checkClosingBracket();
                } else if (test(')')) {
                    if (bal == 0) {
                        throw new BracketsException("No opening parenthesis");
                    }
                    break;
                } else if (between('0', '9')) {
                    curExpr.add(parseConst(false));
                } else if (between('x', 'z')) {
                    curExpr.add(parseVariable());
                } else if (test('-')) {
                    if (isFirst) {
                        take();
                        if (between('0', '9')) {
                            curExpr.add(parseConst(true));
                        } else {
                            curExpr.add(new CheckedNegate(parseUnaryOperation()));
                        }
                    } else {
                        break;
                    }
                } else if (take('a')) {
                    expect("bs");
                    checkAbs();
                    curExpr.add(new CheckedAbs(parseUnaryOperation()));
                } else if (take('*')) { // multiply
                    if (take('*')) {
                        addBinOper(curExpr, "**");
                    } else {
                        addBinOper(curExpr, "*");
                    }
                } else if (take('/')) { // divide
                    if (take('/')) {
                       addBinOper(curExpr, "//");
                    } else {
                        addBinOper(curExpr, "/");
                    }
                } else if (test('+')) {
                    break;
                } else if (test('>') || test('<')) {
                    break; // end term
                } else {
                    throw new ParsingException("Unknown symbol");
                }
                isFirst = false;
            }
            if (curExpr.size() != 1) {
                throw new ParsingException("Missing argument");
            }
            return curExpr.get(0);
        }

        private PartOfExpression parse0Prior() { // shifts
            List<PartOfExpression> curExpr = new ArrayList<>();
            boolean isFirst = true;
            while (true) {
                skipWhitespace();
                if (testEOF()) {
                    break;
                } else if (take('(')) {
                    bal++;
                    curExpr.add(parseBrackets());
                    checkClosingBracket();
                } else if (test(')')) {
                    if (bal == 0) {
                        throw new BracketsException("No opening parenthesis");
                    }
                    break;
                } else if (between('0', '9')) {
                    curExpr.add(parseConst(false));
                } else if (between('x', 'z')) {
                    curExpr.add(parseVariable());
                } else if (take('-')) {
                    if (isFirst) {
                        if (between('0', '9')) {
                            curExpr.add(parseConst(true));
                        } else {
                            curExpr.add(new CheckedNegate(parseUnaryOperation()));
                        }
                    } else {
                        addBinOper(curExpr, "-");
                    }
                } else if (take('a')) {
                    expect("bs");
                    checkAbs();
                    curExpr.add(new CheckedAbs(parseUnaryOperation()));
                } else if (take('+')) {
                    addBinOper(curExpr, "+");
                } else if (take('*')) { // multiply
                    if (take('*')) {
                        addBinOper(curExpr, "**");
                    } else {
                        addBinOper(curExpr, "*");
                    }
                } else if (take('/')) { // divide
                    if (take('/')) {
                        addBinOper(curExpr, "//");
                    } else {
                        addBinOper(curExpr, "/");
                    }
                } else if (test('>') || test('<')) {
                    break; // end term
                } else {
                    throw new ParsingException("Unknown Symbol");
                }
                isFirst = false;
            }
            if (curExpr.size() != 1) {
                throw new ParsingException("Missing argument");
            }
            return curExpr.get(0);
        }

        private PartOfExpression parseUnaryOperation()  {
            skipWhitespace();
            if (take('(')) {
                bal++;
                PartOfExpression t = parseBrackets();
                checkClosingBracket();
                return t;
            } else if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                } else {
                    return new CheckedNegate(parseUnaryOperation());
                }
            } else if (take('a')) {
                expect("bs");
                checkAbs();
                return new CheckedAbs(parseUnaryOperation());
            } else if (between('0', '9')) {
                return parseConst(false);
            } else if (between('x', 'z')) {
                return parseVariable();
            } else {
                if (testEOF() || checkOperation()) {
                    throw new ParsingException("Bare unary operation");
                }
                throw new ParsingException("Unknown symbol");
            }
        }

        private PartOfExpression parseVariable() {
            if (take('x')) {
                return new Variable("x");
            } else if (take('y')) {
                return new Variable("y");
            } else if (take('z')) {
                return new Variable("z");
            } else {
                throw error("program failed");
            }
        }

        private PartOfExpression parseConst(boolean isNeg) {
            try {
                StringBuilder res = new StringBuilder("");
                if (isNeg) {
                    res.append('-');
                }
                while (between('0', '9')) {
                    res.append(take());
                }
                if (res.length() == 1 && res.charAt(0) == '-') {
                    throw new ParsingException("Incorrect const symbol");
                }
                return new Const(Integer.parseInt(String.valueOf(res)));
            } catch (NumberFormatException e) {
                throw new ParsingException("ConstOverflowException");
            }
        }

        private void checkAbs() throws ParsingException {
            if (!isWhitespace() && !test('(')) {
                throw new ParsingException("Incorrect abs syntax");
            }
        }

        private void addBinOper(List<PartOfExpression> curExpr, String sign) {
            if (curExpr.size() == 0) {
                throw new ParsingException("missing first argument");
            }
            switch (sign) {
                case "+" -> {
                    curExpr.add(new CheckedAdd(
                            curExpr.remove(curExpr.size() - 1),
                            parse1Prior()
                    ));
                }
                case "-" -> {
                    curExpr.add(new CheckedSubtract(
                            curExpr.remove(curExpr.size() - 1),
                            parse1Prior()
                    ));
                }
                case "*" -> {
                    curExpr.add(new CheckedMultiply(
                            curExpr.remove(curExpr.size() - 1),
                            parse2Prior()
                    ));
                }
                case "/" -> {
                    curExpr.add(new CheckedDivide(
                            curExpr.remove(curExpr.size() - 1),
                            parse2Prior()
                    ));
                }
                case "//" -> {
                    curExpr.add(new CheckedLog(
                            curExpr.remove(curExpr.size() - 1),
                            parse3Prior()
                    ));
                }
                case "**" -> {
                    curExpr.add(new CheckedPow(
                            curExpr.remove(curExpr.size() - 1),
                            parse3Prior()
                    ));
                }
                case ">>>" -> {
                    curExpr.add(new CheckedShiftA(
                            curExpr.remove(curExpr.size() - 1),
                            parse0Prior()
                    ));
                }
                case ">>" -> {
                    curExpr.add(new CheckedShiftR(
                            curExpr.remove(curExpr.size() - 1),
                            parse0Prior()
                    ));
                }
                case "<<" -> {
                    curExpr.add(new CheckedShiftL(
                            curExpr.remove(curExpr.size() - 1),
                            parse0Prior()
                    ));
                }
                default -> throw new IllegalArgumentException("Program works wrong");
            }
        }
    }
}
