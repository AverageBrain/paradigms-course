package expression.generic;

import expression.generic.classes.PartOfExpression;

interface GenericParser<T extends Number> {
    PartOfExpression<T> parse(String expression) throws Exception;
}
