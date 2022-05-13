package expression.generic;

import expression.generic.classes.*;
import expression.exceptions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ExpressionGenericParser<T extends Number> implements GenericParser<T> {
    @Override
    public PartOfExpression<T> parse(final String expression) {
        return parse(new StringSource(expression));
    }

    public PartOfExpression<T> parse(CharSource expression) {
        return new ParserExpression(expression).parseExpression();
    }

    private class ParserExpression extends BaseParser {
        private int bal = 0;

        public ParserExpression(final CharSource source) {
            super(source);
        }

        public PartOfExpression<T> parseExpression() {
            skipWhitespace();
            final PartOfExpression<T> result = parseValue();
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

        private PartOfExpression<T> parseValue() {
            PartOfExpression<T> curExpr = parsePrior(-1);
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

        Map<String, Integer> PRIORS = Map.of(
                "min", 0,
                "max", 0,
                "+", 1,
                "-", 1,
                "*",2,
                "/", 2
        );

        ArrayList<Map.Entry<String, Integer>> OPERATIONS = new ArrayList<>(PRIORS.entrySet());
        {
            OPERATIONS.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        }

        private String getBinOper() {
            for (Map.Entry<String, Integer> operation : OPERATIONS) {
                if (test(operation.getKey())) {
                    return operation.getKey();
                }
            }
            throw new ParsingException("Unknown token");
        }

        private PartOfExpression<T> parsePrior(int curPrior) {
            List<PartOfExpression<T>> curExpr = new ArrayList<>();
            boolean isFirst = true;
            while (true) {
                skipWhitespace();
                if (testEOF()) {
                    break;
                } else if (take('(')) {
                    bal++;
                    curExpr.add(parsePrior(-1));
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
                } else if (take('c')) {
                    expect("ount");
                    curExpr.add(new Count<T>(parseUnaryOperation()));
                } else if (take('a')) {
                    expect("bs");
                    checkAbs();
                    curExpr.add(new Abs<T>(parseUnaryOperation()));
                } else if (test('-')) {
                    if (isFirst) {
                        take();
                        if (between('0', '9')) {
                            curExpr.add(parseConst(true));
                        } else {
                            curExpr.add(new Negate(parseUnaryOperation()));
                        }
                    } else {
                        if (curPrior < PRIORS.get("-")) {
                            take();
                            addBinOper(curExpr, "-");
                        } else {
                            break;
                        }
                    }
                } else {
                    String operation = getBinOper();
                    if (curPrior < PRIORS.get(operation)) {
                        expect(operation);
                        addBinOper(curExpr, operation);
                    } else {
                        break;
                    }
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

        private PartOfExpression<T> parseUnaryOperation()  {
            skipWhitespace();
            if (take('(')) {
                bal++;
                PartOfExpression<T> t = parsePrior(-1);
                checkClosingBracket();
                return t;
            } else if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                } else {
                    return new Negate<T>(parseUnaryOperation());
                }
            } else if (take('c')) {
                expect("ount");
                return new Count<T>(parseUnaryOperation());
            } else if (take('a')) {
                expect("bs");
                checkAbs();
                return new Abs<T>(parseUnaryOperation());
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

        private PartOfExpression<T> parseVariable() {
            if (take('x')) {
                return new Variable<T>("x");
            } else if (take('y')) {
                return new Variable<T>("y");
            } else if (take('z')) {
                return new Variable<T>("z");
            } else {
                throw error("program failed");
            }
        }

        private PartOfExpression<T> parseConst(boolean isNeg) {
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
                return new Const<T>(String.valueOf(res));
            } catch (NumberFormatException e) {
                throw new ParsingException("ConstOverflowException");
            }
        }

        private void checkAbs() throws ParsingException {
            if (!isWhitespace() && !test('(')) {
                throw new ParsingException("Incorrect abs syntax");
            }
        }

        private void addBinOper(List<PartOfExpression<T>> curExpr, String sign) {
            if (curExpr.size() == 0) {
                throw new ParsingException("missing first argument");
            }
            BiFunction<PartOfExpression<T>, PartOfExpression<T>, PartOfExpression<T>> expr;
            switch (sign) {
                case "+" -> expr = Add<T>::new;
                case "-" -> expr = Subtract<T>::new;
                case "*" -> expr = Multiply<T>::new;
                case "/" -> expr = Divide<T>::new;
                case "min" -> expr = Min<T>::new;
                case "max" -> expr = Max<T>::new;
                default -> throw new IllegalArgumentException("Program works wrong");
            }
            curExpr.add(expr.apply(
                    curExpr.remove(curExpr.size() - 1),
                    parsePrior(PRIORS.get(sign))
            ));
        }
    }
}
