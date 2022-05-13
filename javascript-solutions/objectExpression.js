"use strict";

function Expression() {
}

Expression.prototype.evaluate = function(...vars) {
    return this.evaluate(...vars);
}
Expression.prototype.toString = function() {
    return this.toString();
}
Expression.prototype.diff = function(diffVar) {
    return this.diff(diffVar);
}
Expression.prototype.prefix = function() {
    return this.toString();
}
Expression.prototype.postfix = function() {
    return this.toString();
}

function Const(value) {
     this.value = value;
}
Const.prototype = Object.create(Expression.prototype);
Const.prototype.constructor = Const;

const ONE = new Const(1);
const ZERO = new Const(0);

Const.prototype.evaluate = function(...vars) {
    return this.value;
}
Const.prototype.toString = function() {
    return this.value.toString();
}
Const.prototype.diff = function(diffVar) {
    return ZERO;
}

const VARIABLE = {
    'x': 0,
    'y': 1,
    'z': 2
};

function Variable(varName) {
    this.varName = varName;
}
Variable.prototype = Object.create(Expression.prototype);
Variable.prototype.constructor = Variable;
Variable.prototype.evaluate = function(...vars) {
    return vars[VARIABLE[this.varName]];
}
Variable.prototype.toString = function() {
    return this.varName;
}
Variable.prototype.diff = function(diffVar) {
    return ((this.varName === diffVar)? ONE: ZERO);
}

function Operation(...args) {
    this.args = args;
}
Operation.prototype = Object.create(Expression.prototype);
Operation.prototype.constructor = Operation;
Operation.prototype.evaluate = function(...vars) {
    return this.operation(...this.args.map(item => item.evaluate(...vars)));
}
Operation.prototype.toString = function() {
    return this.args.join(' ') + ' ' + this.sign;
}
Operation.prototype.diff = function(diffVar) {
    return this.diffRules(diffVar, ...this.args);
}
Operation.prototype.prefix = function() {
    return '(' + this.sign + ' ' + this.args.map(item => item.prefix()).join(' ') + ')';
}
Operation.prototype.postfix = function() {
    return '(' + this.args.map(item => item.postfix()).join(' ') + ' ' + this.sign + ')';
}

function createOperation(sign, operation, diffRules) {
    let newOperation = function(...args) {
        Operation.call(this, ...args);
    }
//    newOperation.prototype.constructor = newOperation.constructor;
    newOperation.prototype = Object.create(Operation.prototype);
    newOperation.argsCount = operation.length;
    newOperation.prototype.operation = operation;
    newOperation.prototype.sign = sign;
    newOperation.prototype.diffRules = diffRules;
    return newOperation;
}

const Negate = createOperation(
    'negate', operand => -operand,
    (v, operand) => new Negate(operand.diff(v))
);

const Add = createOperation(
    '+', (l, r) => (l + r),
    (v, l, r) => new Add(l.diff(v), r.diff(v))
);

const Subtract = createOperation(
    '-', (a, b) => (a - b),
    (v, l, r) => new Subtract(l.diff(v), r.diff(v))
);

const Multiply = createOperation(
    '*', (a, b) => (a * b),
    (v, l, r) => new Add(
        new Multiply(l.diff(v), r),
        new Multiply(l, r.diff(v))
    )
);

const Divide = createOperation(
    '/', (a, b) => (a / b),
    (v, l, r) => new Divide(
        new Subtract(
            new Multiply(l.diff(v), r),
            new Multiply(l, r.diff(v))
        ),
        new Multiply(r, r)
    )
);

const Gauss = createOperation(
    'gauss', (a, b, c, x) => (a * Math.exp((-(x - b) * (x - b)) / (2 * c * c))),
    // Gauss' = a * Gauss(1, b, c, x)' + a' * Gauss(1, b, c, x)
    // Gauss' = Gauss(a, b, c, x) * (-(x - b)^2 / (2 * c^2))' + a' * Gauss(1, b, c, x)
    (v, a, b, c, x) => (new Add(
                            new Multiply(
                                new Gauss(a, b, c, x),
                                new Negate(
                                    new Divide(
                                        new Multiply(
                                        new Subtract(x, b),
                                        new Subtract(x, b)
                                    ),
                                    new Multiply(
                                        new Const(2),
                                        new Multiply(c, c)
                                    )
                                )).diff(v)
                            ),
                            new Gauss(a.diff(v), b, c, x)
                        ))
);

function foldLeft(f, zero) {
    return (...args) => {
        let result = zero;
        for (const arg of args) {
            result = f(result, arg);
        }
        return result;
    }
}

let sumexp = foldLeft((a, b) => (a + Math.exp(b)), 0);

const Sumexp = createOperation(
    'sumexp', (...args) => sumexp(...args),
    (v, ...args) => {
        let a = [];
        for (const arg of args) {
            a.push(new Multiply(arg.diff(v), new Sumexp(arg)));
        }
        let result = ZERO;
        for (let item of a) {
            result = new Add(result, item);
        }
        return result;
    }
);

const Softmax = createOperation(
    'softmax', ((...args) => (Math.exp(args[0]) / sumexp(...args))),
    (v, ...args) => (
        new Divide(
            new Sumexp(args[0]),
            new Sumexp(...args)
        ).diff(v)
    )
);

const OPERATIONS = {
    '+': Add,
    '-': Subtract,
    '*': Multiply,
    '/': Divide,
    'negate': Negate,
    'gauss': Gauss,
    'sumexp': Sumexp,
    'softmax': Softmax
};

const parse = input => {
    let stack = []
    for (let item of input.split(' ')) {
        if (item.trim() !== '') {
            if (item in OPERATIONS) {
                stack.push(new OPERATIONS[item](...stack.splice(stack.length - OPERATIONS[item].argsCount)));
            } else if (item in VARIABLE) {
                stack.push(new Variable(item));
            } else {
                stack.push(new Const(parseInt(item)));
            }
        }
    }
    return stack.pop();
}

function ParsingError(message) {
    this.message = message;
}
ParsingError.prototype = Object.create(Error.prototype);
ParsingError.prototype.name = "ParsingError";
ParsingError.prototype.constructor = ParsingError;

function StringSource(data) {
    this.data = data;
    this.pos = 0;
}
StringSource.prototype.hasNext = function() {
    return this.pos < this.data.length;
}
StringSource.prototype.next = function() {
    return this.data[this.pos++];
}
StringSource.prototype.getPosition = function() {
    return this.pos;
}
StringSource.prototype.changePos = function(shift) {
    this.pos += (shift);
    if (this.pos < 0 || this.pos > this.data.length) {
        throw new Error("Wrong shift: StringSource.prototype.changePos" + this.pos + ' ' + shift);
    }
    return this.data[this.pos - 1];
}

function BaseParser(source) {
    this.source = new StringSource(source);
    this.take();
    this.END = '\0';
}
BaseParser.prototype.take = function() {
    let result = this.ch;
    this.ch = (this.source.hasNext() ? this.source.next() : this.END);
    return result;
}
BaseParser.prototype.test = function(expected) {
    return (this.ch === expected);
}
BaseParser.prototype.takeExpected = function(expected) {
    if (this.ch === expected) {
        this.take();
        return true;
    }
    return false;
}
BaseParser.prototype.isWhiteSpace = function() {
    if (this.ch === ' ') {
        this.take();
        return true;
    }
    return false;
}
BaseParser.prototype.eof = function() {
    return this.takeExpected(this.END);
}
BaseParser.prototype.getPosition = function() {
    return this.source.getPosition();
}
BaseParser.prototype.changePos = function(shift) {
    this.ch = this.source.changePos(shift);
    return this.ch;
}

const TYPES = {
    PREFIX: "prefix",
    POSTFIX: "postfix"
}

function parseWithType(source, type) {
    const parser = new BaseParser(source);

    parser.skipWhiteSpace = function() {
        while (this.isWhiteSpace()) {
            // do nothing
        }
    }

    parser.nextToken = function() {
        this.skipWhiteSpace();
        let curToken = "";
        if (parser.test('(') || parser.test(')')) {
            curToken = parser.take();
        } else {
            while (!parser.eof() && !parser.isWhiteSpace() && !(parser.test('(') || parser.test(')'))) {
                curToken += parser.take();
            }
        }
        return (curToken === ""? parser.END: curToken);
    }

    parser.testToken = function() {
        let posBefore = parser.getPosition();
        let curToken = parser.nextToken();
        parser.changePos(posBefore - parser.getPosition());
        return curToken;
    }

    parser.parseArgs = function() {
        let a = [];
        while (true) {
            let curToken = parser.testToken();
            if (curToken === parser.END) {
                throw new ParsingError("Enf of file no expected");
            }
            if ((curToken in OPERATIONS && type === TYPES.POSTFIX) ||
                (curToken === ')' && type === TYPES.PREFIX)) {
                return a;
            }
            curToken = parser.nextToken();
            if (curToken === '(') {
                a.push(parser.parseBrackets());
                if (parser.nextToken() !== ')') {
                    throw new ParsingError(
                        `Missing ending bracket: ${this.getPosition()}`
                    );
                }
            } else if (curToken in VARIABLE) {
                a.push(new Variable(curToken));
            } else if (!isNaN(+curToken)) {
                a.push(new Const(+curToken));
            } else {
                throw new ParsingError(`Unexpected token on pos ${this.getPosition()} - \`${curToken}\``);
            }
        }
    }

    parser.parseOperation = function() {
        let curToken = parser.nextToken();
        if (!(curToken in OPERATIONS)) {
            throw new ParsingError(
                `Expected operation: actual \`${curToken}\` on pos ${this.getPosition()}`
            );
        }
        return curToken;
    }

    parser.parseBrackets = function() {
        let oper, args;
        if (type === TYPES.POSTFIX) {
            args = parser.parseArgs();
            oper = parser.parseOperation();
        } else {
            oper = parser.parseOperation();
            args = parser.parseArgs();
        }
        let cnt = OPERATIONS[oper].argsCount;
        if (cnt !== 0 && cnt !== args.length) {
             throw new ParsingError(
                 `Incorrect count of function arguments:
                        for \`${oper}\` on pos ${this.getPosition()} : expected - ${cnt}, actual - ${args.length}`
             );
        }
        return new OPERATIONS[oper](...args);
    }

    parser.parseValue = function() {
        let curToken = parser.nextToken();
        if (curToken === '(') {
            let expr = parser.parseBrackets();
            if (parser.nextToken() !== ')') {
                throw new ParsingError(
                    `Missing ending bracket on pos ${this.getPosition()}`
                );
            }
            return expr;
        } else if (curToken in VARIABLE) {
            return new Variable(curToken);
        } else if (!isNaN(+curToken)) {
            return new Const(+curToken);
        } else {
            if (curToken == undefined) {
                throw new ParsingError("Empty input");
            } else {
                throw new ParsingError(
                    `Unknown token on pos ${this.getPosition()} : found \`${curToken}\``
                );
            }
        }
    }

    let ans = parser.parseValue();
    parser.skipWhiteSpace();
    if (!parser.eof()) {
        throw new ParsingError("End of expression expected");
    }
    return ans;
}

const parsePostfix = (string) => parseWithType(string, TYPES.POSTFIX);
const parsePrefix = (string) => parseWithType(string, TYPES.PREFIX);