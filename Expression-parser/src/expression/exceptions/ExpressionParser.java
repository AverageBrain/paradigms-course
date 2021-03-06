package expression.exceptions;

import expression.*;
import expression.generic.evaluate.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
            PartOfExpression curExpr = parsePrior(-1);
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
                "<<", 0,
                ">>", 0,
                ">>>", 0,
                "+", 1,
                "-", 1,
                "*",2,
                "/", 2,
                "//", 3,
                "**", 3
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

        private PartOfExpression parsePrior(int curPrior) {
            List<PartOfExpression> curExpr = new ArrayList<>();
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
                } else if (take('a')) {
                    expect("bs");
                    checkAbs();
                    curExpr.add(new CheckedAbs(parseUnaryOperation()));
                } else if (test('-')) {
                    if (isFirst) {
                        take();
                        if (between('0', '9')) {
                            curExpr.add(parseConst(true));
                        } else {
                            curExpr.add(new CheckedNegate(parseUnaryOperation()));
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

        private PartOfExpression parseUnaryOperation()  {
            skipWhitespace();
            if (take('(')) {
                bal++;
                PartOfExpression t = parsePrior(-1);
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
            if (between('x', 'z')) {
                return new Variable(Character.toString(take()));
            } else {
                throw error("Wrong name by variable");
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
            BiFunction<PartOfExpression, PartOfExpression, PartOfExpression> expr;
            switch (sign) {
                case "+" -> expr = CheckedAdd::new;
                case "-" -> expr = CheckedSubtract::new;
                case "*" -> expr = CheckedMultiply::new;
                case "/" -> expr = CheckedDivide::new;
                case "//" -> expr = CheckedLog::new;
                case "**" -> expr = CheckedPow::new;
                case ">>>" -> expr = CheckedShiftA::new;
                case ">>" -> expr = CheckedShiftR::new;
                case "<<" -> expr = CheckedShiftL::new;
                default -> throw new IllegalArgumentException("Program works wrong");
            }
            curExpr.add(expr.apply(
                    curExpr.remove(curExpr.size() - 1),
                    parsePrior(PRIORS.get(sign))
            ));
        }
    }
}
