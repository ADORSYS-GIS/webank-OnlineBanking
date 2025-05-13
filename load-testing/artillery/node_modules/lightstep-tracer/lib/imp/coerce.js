'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});
exports.toString = toString;
exports.toNumber = toNumber;
exports.toBoolean = toBoolean;
function toString(value) {
    return '' + value; // eslint-disable-line prefer-template
}

function toNumber(value) {
    return Number(value);
}

function toBoolean(value) {
    return !!value;
}

//# sourceMappingURL=coerce.js.map