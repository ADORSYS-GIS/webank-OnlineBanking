// the parser is dynamically generated from generateParser.js at compile time
import { parser } from './parser.mjs';

// Shared utility functions
const std =
{

    isfn: function(fns, funcName) {
        return fns.hasOwnProperty(funcName) && typeof fns[funcName] === "function";
    },

    unknown: function(funcName) {
        throw ReferenceError('Unknown function: ' + funcName + '()');
    },

    coerceArray: function(value) {
        if (Array.isArray(value))
            return value;
        else
            return [value];
    },

    coerceBoolean: function(value) {
        if (typeof value === 'boolean')
            return +value;
        else
            return value;
    },

    isSubset: function(a, b) {
        const A = std.coerceArray(a);
        const B = std.coerceArray(b);
        return +A.every( val => B.includes(val) );
    },

    buildString: function(quote, literal)
    {
        quote = String(quote)[0];
        literal = String(literal);
        let built = '';

        if (literal[0] !== quote || literal[literal.length-1] !== quote)
            throw new Error(`Unexpected internal error: String literal doesn't begin/end with the right quotation mark.`);

        for (let i = 1; i < literal.length - 1; i++)
        {
            if (literal[i] === "\\")
            {
                i++;
                if (i >= literal.length - 1) throw new Error(`Unexpected internal error: Unescaped backslash at the end of string literal.`);

                if (literal[i] === "\\") built += '\\';
                else if (literal[i] === quote) built += quote;
                else throw new Error(`Unexpected internal error: Invalid escaped character in string literal: ${literal[i]}`);
            }
            else if (literal[i] === quote)
            {
                throw new Error(`Unexpected internal error: String literal contains unescaped quotation mark.`);
            }
            else
            {
                built += literal[i];
            }
        }

        return JSON.stringify(built);
    }
};

parser.yy = Object.create(std);

/**
 * Filtrex provides compileExpression() to compile user expressions to JavaScript.
 *
 * See https://github.com/joewalnes/filtrex for tutorial, reference and examples.
 * MIT License.
 *
 * Includes Jison by Zachary Carter. See http://jison.org/
 *
 * -Joe Walnes
 */
export function compileExpression(expression, options) {

    // Check and coerce arguments

    if (arguments.length > 2) throw new TypeError('Too many arguments.');

    options = typeof options === "object" ? options : {};
    let {extraFunctions, customProp} = options;
    for (let key of Object.getOwnPropertyNames(options))
    {
        if (key !== "extraFunctions" && key !== "customProp") throw new TypeError(`Unknown option: ${key}`);
    }



    // Functions available to the expression

    let functions = {
        abs: Math.abs,
        ceil: Math.ceil,
        floor: Math.floor,
        log: Math.log,
        max: Math.max,
        min: Math.min,
        random: Math.random,
        round: Math.round,
        sqrt: Math.sqrt,
    };

    if (extraFunctions) {
        for (var name in extraFunctions) {
            if (extraFunctions.hasOwnProperty(name)) {
                functions[name] = extraFunctions[name];
            }
        }
    }



    // Compile the expression

    let tree = parser.parse(expression);

    let js = [];
    js.push('return ');
    function toJs(node) {
        if (Array.isArray(node)) {
            node.forEach(toJs);
        } else {
            js.push(node);
        }
    }
    tree.forEach(toJs);
    js.push(';');



    // Metaprogramming functions

    function prop(name, obj) {
        return Object.prototype.hasOwnProperty.call(obj||{}, name) ? obj[name] : undefined;
    }

    function safeGetter(obj) {
        return function get(name) {
            return Object.prototype.hasOwnProperty.call(obj||{}, name) ? obj[name] : undefined;
        }
    }

    if (typeof customProp === 'function') {
        prop = (name, obj) => std.coerceBoolean(customProp(name, safeGetter(obj), obj));
    }



    // Patch together and return

    let func = new Function('fns', 'std', 'prop', 'data', js.join(''));

    return function(data) {
        try {
            return func(functions, std, prop, data);
        }
        catch (e)
        {
            return e;
        }
    };
}
