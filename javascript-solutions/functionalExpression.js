"use strict";

const oper = (f, cntArgs) => {
    let cur = (...args) => (...vars) => f(...args.map(item => item(...vars)));
    cur.argsCount = ((cntArgs === undefined)? f.length: cntArgs);
    return cur;
}
const add = oper((a, b) => (a + b));
const subtract = oper((a, b) => (a - b));
const multiply = oper((a, b) => (a * b));
const divide = oper((a, b) => (a / b));
const negate = oper(a => -a);
const avg3 = oper((first, second, third) => ((first + second + third)/ 3));
const med5 = oper((...args) => args.sort((a, b) => (a - b))[2], 5);

const OPERATIONS = {
    '+': add,
    '-': subtract,
    '*': multiply,
    '/': divide,
    'negate': negate,
    'avg3': avg3,
    'med5': med5
};

const VARIABLE = {
    'x': 0,
    'y': 1,
    'z': 2
};
const variable = input => (...vars) => vars[VARIABLE[input]];

const cnst = value => (...vars) => value;
const pi = cnst(Math.PI);
const e = cnst(Math.E);

const CONST = {
    'pi': pi,
    'e': e
};

const parse = input => {
    let stack = []
    for (let item of input.split(' ')) {
        if (item.trim() !== '') {
            if (item in OPERATIONS) {
                stack.push(OPERATIONS[item](...stack.splice(stack.length - OPERATIONS[item].argsCount)));
            } else if (item in VARIABLE) {
                stack.push(variable(item));
            } else if (item in CONST) {
                stack.push(CONST[item]);
            } else {
                stack.push(cnst(parseInt(item)));
            }
        }
    }
    return stack.pop();
}
