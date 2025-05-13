const { compileExpression } = require("../dist/cjs/filtrex");

const { describe, it } = require("mocha");
const { expect } = require("chai");

const eval = (str, obj) => compileExpression(str)(obj);




describe('Arithmetics', () => {

    it('can do simple numeric expressions', () => {
        expect( eval('1 + 2 * 3') ).equals(7);
        expect( eval('2 * 3 + 1') ).equals(7);
        expect( eval('1 + (2 * 3)') ).equals(7);
        expect( eval('(1 + 2) * 3') ).equals(9);
        expect( eval('((1 + 2) * 3 / 2 + 1 - 4 + (2 ^ 3)) * -2') ).equals(-19);
        expect( eval('1.4 * 1.1') ).equals(1.54);
        expect( eval('97 % 10') ).equals(7);
    });


    it('does math functions', () => {
        expect( eval('abs(-5)') ).equals(5);
        expect( eval('abs(5)') ).equals(5);
        expect( eval('ceil(4.1)') ).equals(5);
        expect( eval('ceil(4.6)') ).equals(5);
        expect( eval('floor(4.1)') ).equals(4);
        expect( eval('floor(4.6)') ).equals(4);
        expect( eval('round(4.1)') ).equals(4);
        expect( eval('round(4.6)') ).equals(5);
        expect( eval('sqrt(9)') ).equals(3);
    });


    it('supports functions with multiple args', () => {
        expect( eval('random() >= 0') ).equals(1);
        expect( eval('min(2)') ).equals(2);
        expect( eval('max(2)') ).equals(2);
        expect( eval('min(2, 5)') ).equals(2);
        expect( eval('max(2, 5)') ).equals(5);
        expect( eval('min(2, 5, 6)') ).equals(2);
        expect( eval('max(2, 5, 6)') ).equals(6);
        expect( eval('min(2, 5, 6, 1)') ).equals(1);
        expect( eval('max(2, 5, 6, 1)') ).equals(6);
        expect( eval('min(2, 5, 6, 1, 9)') ).equals(1);
        expect( eval('max(2, 5, 6, 1, 9)') ).equals(9);
        expect( eval('min(2, 5, 6, 1, 9, 12)') ).equals(1);
        expect( eval('max(2, 5, 6, 1, 9, 12)') ).equals(12);
    });


    it('can do comparisons', () => {
        expect( eval('foo == 4', {foo: 4}) ).equals(1);
        expect( eval('foo == 4', {foo: 3}) ).equals(0);
        expect( eval('foo == 4', {foo:-4}) ).equals(0);
        expect( eval('foo != 4', {foo: 4}) ).equals(0);
        expect( eval('foo != 4', {foo: 3}) ).equals(1);
        expect( eval('foo != 4', {foo:-4}) ).equals(1);
        expect( eval('foo > 4',  {foo: 3}) ).equals(0);
        expect( eval('foo > 4',  {foo: 4}) ).equals(0);
        expect( eval('foo > 4',  {foo: 5}) ).equals(1);
        expect( eval('foo >= 4', {foo: 3}) ).equals(0);
        expect( eval('foo >= 4', {foo: 4}) ).equals(1);
        expect( eval('foo >= 4', {foo: 5}) ).equals(1);
        expect( eval('foo < 4',  {foo: 3}) ).equals(1);
        expect( eval('foo < 4',  {foo: 4}) ).equals(0);
        expect( eval('foo < 4',  {foo: 5}) ).equals(0);
        expect( eval('foo <= 4', {foo: 3}) ).equals(1);
        expect( eval('foo <= 4', {foo: 4}) ).equals(1);
        expect( eval('foo <= 4', {foo: 5}) ).equals(0);
    });


    it('can do boolean logic', () => {
        expect( eval('0 and 0') ).equals(0);
        expect( eval('0 and 1') ).equals(0);
        expect( eval('1 and 0') ).equals(0);
        expect( eval('1 and 1') ).equals(1);
        expect( eval('0 or 0')  ).equals(0);
        expect( eval('0 or 1')  ).equals(1);
        expect( eval('1 or 0')  ).equals(1);
        expect( eval('1 or 1')  ).equals(1);
        expect( eval('not 0')   ).equals(1);
        expect( eval('not 1')   ).equals(0);
        expect( eval('(0 and 1) or 1') ).equals(1);
        expect( eval('0 and (1 or 1)') ).equals(0);
        expect( eval('0 and 1 or 1')   ).equals(1);
        expect( eval('1 or 1 and 0')   ).equals(1);
        expect( eval('not 1 and 0')    ).equals(0);
    });

});